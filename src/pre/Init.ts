import { standbyRequest } from '@corcc/nvr';
import { LightResponse } from '@corcc/nvr/lib/util/type';
import { loadConfig } from '../util/Config';

export async function Init (): Promise<LightResponse> {
	const { orgCd, sid }: any = loadConfig();
	const res = await standbyRequest({
		orgCd: orgCd,
		sid: sid
	});
	const { responseCode } = res;

	switch ((responseCode / 100).toFixed(0)) {
	case '3':
		return res;
	}
	throw new Error(`${responseCode}`);
}
