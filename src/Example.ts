
import { confirmRequest, errorRequest, failureRequest, progressRequest, successRequest } from '@corcc/nvr';
import { config } from 'dotenv';
import { vaccineQuantity } from './info/Request';
config();

export async function reservationSubmit ({
	org,
	key,
	vaccines
}: {
	org: string,
	key: string,
	vaccines: any
}): Promise<any> {
	config();
	const name = process.env.VACCINE_NAME;
	let { cd }: any = vaccines.filter((vaccine: any) => (vaccine.name.indexOf(name) == 0));
	cd = cd ? cd[0] : cd;
	const progressResult = await progressRequest({
		key,
		cd
	});
	const progressResponseCode = progressResult.responseCode;
	const confirmResult = await (async function (r: number) {
		switch ((r / 100)) {
		case 2: return await confirmRequest({
			key,
			cd
		});
		default: throw new Error();
		}
	})(Number(progressResponseCode));
	const { code } = JSON.parse(confirmResult.body);
	switch (code) {
	case 'success': return successRequest({
		key,
		cd
	});
	case 'failure': return failureRequest({
		key,
		cd,
		code
	});
	case 'undefined': return errorRequest({
		key,
		cd
	});
	}
}

(async function () {
	const vaccinesQuantities = await vaccineQuantity();
	const reservationResult = await reservationSubmit(vaccinesQuantities);
})();
