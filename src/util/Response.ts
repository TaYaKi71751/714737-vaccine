import { LightResponse } from '@corcc/nvr/lib/util/type';
import cheerio, { CheerioAPI } from 'cheerio';
const loadCheerio = cheerio.load;
const ov = Object.values;
const fe = Object.fromEntries;

export function getDataKeyFromResponseBody ({
	responseCode,
	body,
	headers
}: LightResponse): string {
	const infoCheerio: CheerioAPI = loadCheerio(body);
	return infoCheerio('[data-key]').attr()['data-key'];
}

export function getVaccinesFromResponseBody ({
	responseCode,
	body,
	headers
}: LightResponse): {
	key: string,
	vaccines: any
} {
	const infoCheerio: CheerioAPI = loadCheerio(body);
	const vaccineInputs = infoCheerio('ul > li.radio_item > input');
	const vaccines: any = ov(vaccineInputs).filter((_) =>
		(typeof _.attribs != 'undefined')).map((vaccineInput): {
			[x: string]: string | {
				[x: string]: string
			}
		} => {
		const vaccineInputAttribs = vaccineInput.attribs;
		let vaccine: {
				[x: string]: string
			} = { '': '' };
		const cd = vaccineInputAttribs['data-cd'];
		const name = vaccineInputAttribs['data-name'];
		const { disabled } = vaccineInputAttribs;
		vaccine = {
			cd,
			name,
			disabled
		};
		console.log(vaccine);
		return vaccine;
	});
	return {
		key: infoCheerio('[data-key]').attr()['data-key'],
		vaccines
	};
}
