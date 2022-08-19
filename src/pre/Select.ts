import { VaccineInfo } from '../type';
import { loadConfig } from '../util/Config';
const oe = Object.entries;

export function filterSelected (vaccineQuantities: Array<VaccineInfo>) {
	const config = loadConfig();
	const selectedVaccines = config.vaccines;
	let _a = vaccineQuantities;
	_a = _a.filter((vaccineQuantity) => {
		return (
			oe(vaccineQuantity)
				.filter(([k, v]) => typeof v == 'string')
				.filter(([k, v]: any) => {
					return selectedVaccines.filter((s: string): any => {
						return v.includes(s);
					}).length;
				})
		).length;
	});
	return _a;
}
