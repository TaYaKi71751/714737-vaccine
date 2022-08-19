import path from 'path';
import qs from 'querystring';

export type Space = `${' '|''}`;

export type Is = `${Space}${'='|':'}${Space}`;

export type And = `${Space}${'&'|','|'|'}${Space}`;

export type VaccineTarget = {
	cd?: RegExp | string;
	name?: RegExp | string;
} | RegExp | string;

export type VaccineOrganizationTarget = {
	sid?: number|string;
	orgCd?: number|string;
	id?: number|string;
	vaccineOrganizationCode?: number|string;
} | `${'orgCd'|'sid'}${Is}${string}${And}${'orgCd'|'sid'}${Is}${string}`;

export class VaccineOrganizationTargetProp {
	private __id__!:string;
	private __vaccineOrganizationCode__!:string;

	constructor (v?:VaccineOrganizationTarget) {
		if (v) {
			switch (typeof v) {
			case 'string': {
				const _v = qs.parse(`${v}`);
				this.__id__ = `${_v.id || _v.sid}`;
				this.__vaccineOrganizationCode__ = `${_v.orgCd || _v.vaccineOrganizationCode}`;
			} break;
			case 'object': {
				this.__id__ = `${v.id || v.sid}`;
				this.__vaccineOrganizationCode__ = `${v.orgCd || v.vaccineOrganizationCode}`;
			}
			}
		}
	}

	get id () { return this.__id__; }
	get sid () { return this.__id__; }
	set id (value) { this.__id__ = `${value}`; }
	set sid (value) { this.__id__ = `${value}`; }

	get orgCd () { return this.__vaccineOrganizationCode__; }
	get vaccineOrganizationCode () { return this.__vaccineOrganizationCode__; }
	set orgCd (value) { this.__vaccineOrganizationCode__ = `${value}`; }
	set vaccineOrganizationCode (value) { this.__vaccineOrganizationCode__ = `${value}`; }
}

export type Config = {
	organization: VaccineOrganizationTarget[];
	vaccine?: VaccineTarget[];
};

export function loadConfig ():Config {
	const cwd = process.cwd();
	const configPath = path.join(cwd, 'config');
	const { config }: {
		config: Config
	} = require(configPath);
	return config;
}
