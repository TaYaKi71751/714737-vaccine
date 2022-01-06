import { VaccineInfo } from '../type';
import { loadConfig } from '../util/Config';
const oe = Object.entries;
const fe = Object.fromEntries;
export function filterSelected (vaccineQuantities: Array<VaccineInfo>) {
	const config = loadConfig();
	const selectedVaccines = config.vaccines;
	let _a = vaccineQuantities;
	_a = _a.filter((vaccineQuantity) => {
		return fe(oe(vaccineQuantity).filter(([k, v]) => (typeof v == 'string')).filter(([k, v]: any) => {
			return selectedVaccines.filter((s: string): any => {
				return v.includes(s);
			})[0];
		}));
	});
	return _a;
}
