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
import { loadConfig, VaccineOrganizationTargetProp } from './util/Config';
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

const key = '';
let all: Array<VaccineInfo>;
let available: Array<VaccineInfo>;
let selected: Array<VaccineInfo>;
let description: ReservationInfo;
const res: Response = InitResponse();
const { organization } = loadConfig();
const __org_var__:any[] = [];
let __index__:number = -1;

export async function Example () {
	do {
		for (var i = 0; i < organization.length; i++) {
			__org_var__[i] = __org_var__[i] || {};
			__org_var__[i].organization = __org_var__[i].organization || {};
			__org_var__[i].res = __org_var__[i].res || {};
			const o = __org_var__[i].organization = organization[i];
			if (
				!(__org_var__[i]?.key) || !__org_var__[i].res.standby
			) {
				__org_var__[i].res.standby = await Init(o);
				__org_var__[i].key = getKey(__org_var__[i].res.standby);
			}
			__org_var__[i].res.auth = await Auth(__org_var__[i].res.standby);
			const twoSec: number = 2000;
			const randomTwoSec: number = twoSec + randomNumber(randomNumber(0x7ff));
			await new Promise((resolve) => setTimeout(resolve, randomTwoSec));
			__org_var__[i].res.info = await Info(__org_var__[i].res.auth);
			__org_var__[i].description = await vaccineQuantity(__org_var__[i].res.info);
			__org_var__[i].all = await __org_var__[i].description.vaccines;
			__org_var__[i].available = filterAvailable(__org_var__[i].all);
			__org_var__[i].selected = filterSelected(__org_var__[i].available);
			__index__ = i;
			if (__org_var__[i].selected && __org_var__[i].selected.length) { break; }
		}
	} while (!(__org_var__[i = __index__].selected.length));
	const vaccine: any = __org_var__[i].selected[randomNumber(selected.length)];
	const { cd } = vaccine;
	__org_var__[i].res.progress = await progressRequest({
		key: __org_var__[i].key,
		cd
	});
	try {
		__org_var__[i].res.confirm = await confirmRequest({
			key: __org_var__[i].key,
			cd
		});
		switch (__org_var__[i].res.confirm?.responseCode) {
		case 200:
			return await (async function (body: any) {
				const { code }: any = JSON.parse(body ?? '{"":""}');
				if (code.includes('SUCCESS')) {
					__org_var__[i].res.success = await successRequest({
						key: __org_var__[i].key,
						cd
					});
					return __org_var__[i].res.success;
				}
				__org_var__[i].res.failure = await failureRequest({
					key: __org_var__[i].key,
					cd,
					code
				});
				return __org_var__[i].res.failure;
			})(__org_var__[i].res.confirm.body);
		}
	} catch (err) {
		console.error(err);
		return await errorRequest({
			key: __org_var__[i].key,
			cd
		});
	}
}

Example();
