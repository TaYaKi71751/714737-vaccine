export type VaccineInfo = {
	name?: string;
	cd?: string;
	disabled?: string;
	quantity?: string;
	notice?: string;
};
export type ReservationInfo = {
	org: string;
	key: string;
	vaccines: Array<VaccineInfo>;
};
