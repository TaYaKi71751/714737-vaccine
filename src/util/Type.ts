export function TypeCheck (v: any): any {
	if (!v && typeof v != 'undefined') {
		throw new TypeError(`Invalid Type ${v}:${typeof v}`);
	}
	return v;
}
