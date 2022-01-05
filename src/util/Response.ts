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
	const vaccineRadioItems = infoCheerio('ul > li.radio_item');
	const vaccines: any = ov(vaccineRadioItems).filter((_) =>
		(typeof _.attribs != 'undefined')).map((vaccineRadioItem): {
			[x: string]: string | {
				[x: string]: string
			}
		} => {
		const vaccineRadioItemCheerio = loadCheerio(vaccineRadioItem);
		const vaccineInput = vaccineRadioItemCheerio('input')[0];
		const vaccineInputAttribs = vaccineInput.attribs;
		let vaccine: {
				[x: string]: string
			} = { '': '' };
		const cd = vaccineInputAttribs['data-cd'];
		const name = vaccineInputAttribs['data-name'];
		const quantity = vaccineRadioItemCheerio('label > .num_box > .num').text().trim();
		const notice = vaccineRadioItemCheerio('label > .num_box > .notice').text().trim();
		const { disabled } = vaccineInputAttribs;
		vaccine = {
			cd,
			name,
			disabled,
			quantity,
			notice
		};
		console.log(vaccine);
		return vaccine;
	});
	return {
		key: infoCheerio('[data-key]').attr()['data-key'],
		vaccines
	};
}
