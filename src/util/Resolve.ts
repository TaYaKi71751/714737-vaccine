import { infoRequest } from '@corcc/nvr';
import { LightResponse } from '@corcc/nvr/lib/util/type';

export async function Resolve (res: any | LightResponse): Promise<any | LightResponse> {
	if (res.responseCode == 200) {
		const { location } = res;
		if (!location) {
			const result = await res;
			return result;
		}
		if (res.location.pathname.indexOf('auth') > -1) {
			throw new Error(`${res.responseCode}`);
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
		if (res.location.pathname.indexOf('info') > -1) {
			_res = await infoRequest({
				key
			});
		}
		if (_res.responseCode == 0) {
			throw new Error();
		}
		return await Resolve(_res);
	}
}
