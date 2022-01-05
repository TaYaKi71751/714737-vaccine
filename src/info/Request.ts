import {
	authRequest,
	standbyRequest,
	infoRequest
} from '@corcc/nvr';
import { LightResponse } from '@corcc/nvr/lib/util/type';
import { config } from 'dotenv';
import { randomNumber } from '../util/Random';
import { getVaccinesFromResponseBody } from '../util/Response';

async function followRedirect ({
	res
}: {
    res: any
}): Promise<LightResponse | any> {
	if (res.responseCode == 200) {
		const { location } = res;
		if (!location) {
			let result = await res;
			result = getVaccinesFromResponseBody(res);
			return result;
		}
	}
	if (res.responseCode == 302) {
		const u: any = new URL('https://example.com');
		u.params = { '': '' };
		let _res: LightResponse = {
			responseCode: 0,
			headers: {},
			body: '',
			location: u
		};
		const { key } = res.location.params;
		if (res.location.pathname.indexOf('auth') > -1) {
			_res = await authRequest({
				key
			});
		}
		if (res.location.pathname.indexOf('info') > -1) {
			_res = await infoRequest({
				key
			});
		}
		if (_res.responseCode == 0) {
			throw new Error();
		}
		return await followRedirect({
			res: _res
		});
	}
}

export async function vaccineQuantity () {
	const standByResponse: LightResponse = await standbyRequest({
		orgCd: process.env.orgCd,
		sid: process.env.sid
	});
	let result = await followRedirect({
		res: standByResponse
	});
	const filterAvailable:Function = function (r:any):any {
		const _r = r;
		_r.vaccines = _r.vaccines.filter((_: any) => (!_.disabled));
		return _r;
	};
	result = filterAvailable(result);
	let infoResponse: any;
	while (!result.vaccines.length) {
		const twoSec: number = 2000;
		const randomTwoSec:number = twoSec + randomNumber(randomNumber(0x7FF));
		await new Promise(resolve => setTimeout(resolve, randomTwoSec));
		infoResponse = await infoRequest({
			key: result.key
		});
		result = getVaccinesFromResponseBody(infoResponse);
		result = filterAvailable(result);
	}
	return result;
}
