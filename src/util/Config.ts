import path from 'path';
export function loadConfig (): any {
	const cwd = process.cwd();
	const configPath = path.join(cwd, 'config');
	const { config }: any = require(configPath);
	return config;
}
