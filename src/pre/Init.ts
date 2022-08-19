import { standbyRequest } from '@corcc/nvr';
import { LightResponse } from '@corcc/nvr/lib/util/type';
import { VaccineOrganizationTarget, VaccineOrganizationTargetProp } from '../util/Config';

export async function Init (organizationConfig:VaccineOrganizationTarget): Promise<LightResponse> {
	const {orgCd,sid} = new VaccineOrganizationTargetProp(organizationConfig);
	const res = await standbyRequest({
		orgCd,
		sid
	});
	const { responseCode } = res;

	switch ((responseCode / 100).toFixed(0)) {
	case '3':
		return res;
	}
	throw new Error(`${responseCode}`);
}
