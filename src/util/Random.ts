export function randomNumber (percent: number): number {
	const randNumString: string = Math.random().toString().split('.')[1];
	const randNum: number = Number(randNumString);
	return randNum % percent;
}
