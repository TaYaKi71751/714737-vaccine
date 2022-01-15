import {
	confirmRequest,
	errorRequest,
	failureRequest,
	progressRequest,
	successRequest
} from '@corcc/nvr';
import { LightResponse } from '@corcc/nvr/lib/util/type';
import { Auth, Init, Info, vaccineQuantity, filterAvailable } from './pre';
import { filterSelected } from './pre/Select';
import { ReservationInfo, VaccineInfo } from './type';
import { randomNumber } from './util/Random';
import { getDataKeyFromResponseBody } from './util/Response';

type Response = {
	standby?: LightResponse;
	auth?: LightResponse;
	info?: LightResponse;
	progress?: LightResponse;
	confirm?: LightResponse;
	success?: LightResponse;
	failure?: LightResponse;
	_error?: LightResponse;
};
function InitResponse () {
	return {
		standby: undefined,
		auth: undefined,
		info: undefined,
		progress: undefined,
		confirm: undefined,
		success: undefined,
		failure: undefined,
		error: undefined
	};
}

function getKey (res: any | LightResponse): string {
	const { location, body }: any = res;
	const { params }: any = location;
	const { key }: any = params;

	if (key && typeof key == 'string') {
		return res.location.params.key;
	}
	if (body) {
		return getDataKeyFromResponseBody(res);
	}
	return '';
}

export async function Example () {
	let key = '';
	let all: Array<VaccineInfo>;
	let available: Array<VaccineInfo>;
	let selected: Array<VaccineInfo>;
	let description: ReservationInfo;
	const res: Response = InitResponse();
	res.standby = await Init();
	key = getKey(res.standby);
	res.auth = await Auth(res.standby);
	do {
		const twoSec: number = 2000;
		const randomTwoSec: number = twoSec + randomNumber(randomNumber(0x7ff));
		await new Promise((resolve) => setTimeout(resolve, randomTwoSec));
		res.info = await Info(res.auth);
		description = await vaccineQuantity(res.info);
		all = await description.vaccines;
		available = filterAvailable(all);
		selected = filterSelected(available);
	} while (!selected.length);
	const vaccine: any = selected[randomNumber(selected.length)];
	const { cd } = vaccine;
	res.progress = await progressRequest({
		key,
		cd
	});
	try {
		res.confirm = await confirmRequest({
			key,
			cd
		});
		switch (res.confirm?.responseCode) {
		case 200:
			return await (async function (body: any) {
				const { code }: any = JSON.parse(body ?? '{"":""}');
				if (code.includes('SUCCESS')) {
					res.success = await successRequest({
						key,
						cd
					});
					return res.success;
				}
				res.failure = await failureRequest({
					key,
					cd,
					code
				});
				return res.failure;
			})(res.confirm.body);
		}
	} catch (err) {
		console.error(err);
		return await errorRequest({
			key,
			cd
		});
	}
}

Example();
