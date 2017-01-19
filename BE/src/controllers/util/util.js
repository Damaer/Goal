/**
 * @return {[int]} A Number representing the milliseconds elapsed since the UNIX epoch
 */
exports.getToday = () => Date.now() / (24 * 60 * 60 * 1000);