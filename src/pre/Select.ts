import { VaccineInfo } from '../type';
import { Config, loadConfig, VaccineTarget } from '../util/Config';
const oe = Object.entries;

function select (one:VaccineTarget, ...from: Array<VaccineInfo>):boolean {
	for (const e of from) {
		for (const [k, v] of oe(e)) {
			switch (one?.constructor) {
			case String: if (v == one) { return v == one; } else { continue; }
			case RegExp: if (one instanceof RegExp) { return !!(v.match(one)?.length); } else { continue; }
			case Object: if (oe(one).length) { return oe(one).filter(([k, v]:[k:string, v:RegExp|string]) => select(v, e)).length == oe(one).length; } else { continue; }
			default: continue;
			}
		}
	}
	return false;
}

export function filterSelected (from: Array<VaccineInfo>) {
	const config:Config = loadConfig();
	const result = from.filter(() => (config?.vaccine?.filter((e) => select(e, ...from))));
	return result;
}
