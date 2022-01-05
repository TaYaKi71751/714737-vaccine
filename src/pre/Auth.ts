import { authRequest } from '@corcc/nvr';
import { LightResponse } from '@corcc/nvr/lib/util/type';
import { TypeCheck } from '../util/Type';

export async function Auth (initResponse: LightResponse): Promise<LightResponse> {
	const res = await (async function (r): Promise<LightResponse> {
		let { location }: any = r;
		location = TypeCheck(location);
		const params: any = TypeCheck(location.params);
		const { key }: any = params;
		return await authRequest({
			key
		});
	})(initResponse);
	const { responseCode, location }: any = res;

	switch ((responseCode / 100).toFixed(0)) {
	case '3': {
		if (location.pathname.includes('/auth')) {
			return await Auth(res);
		}
		return res;
	}
	case '2': return res;
	}
	throw new Error(`${responseCode}`);
}
