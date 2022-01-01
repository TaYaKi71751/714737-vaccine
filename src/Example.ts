import { standby } from '@corcc/nvr';
import { URL } from 'url';
console.log(standby);

function testRequest () {
	const standByResponse: any = standby({
		orgCd: process.env.orgCd || '',
		sid: process.env.sid || ''
	});
	let params = (function (_u: string) {
		return new URL(_u).search;
	})(standByResponse.headers.location).split('&').map((param: string) => {
		const _eq = param.indexOf('=');
		const _n = function (_u: string): string {
			return _u.substring(0, _eq);
		};
		const _v = function (_u: string): string {
			return _u.substring(_eq + 1, _u.length);
		};
		const [name, value]: string[] = [_n(param), _v(param)];
		return [name, value];
	});
	params = Object.fromEntries(params);
	console.log(params);
}

testRequest();
