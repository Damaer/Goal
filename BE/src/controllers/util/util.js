exports.ONE_DAY = 86400000; // 一天=86400000ms

/**
 * @return {[int]} A Number representing the milliseconds elapsed since the UNIX epoch
 */
exports.getToday = () => {
	let date = new Date();
	date.setHours(0);
	date.setMinutes(0);
	date.setSeconds(0);
	date.setMilliseconds(0);
	return date.getTime();
};