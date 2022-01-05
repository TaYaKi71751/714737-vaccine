
import { LightResponse } from '@corcc/nvr/lib/util/type';
import { getVaccinesFromResponseBody } from '../util/Response';

export async function vaccineQuantity (infoResponse: LightResponse) {
	const result = getVaccinesFromResponseBody(infoResponse);
	return result;
}
