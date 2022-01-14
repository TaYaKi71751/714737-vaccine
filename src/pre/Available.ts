import { VaccineInfo } from '../type';

export function filterAvailable (
	vaccineQuantities: Array<VaccineInfo>
): Array<VaccineInfo> {
	return (function (r) {
		let _r = r;
		_r = _r.filter((_: any) => !_.disabled);
		return _r;
	})(vaccineQuantities);
}
