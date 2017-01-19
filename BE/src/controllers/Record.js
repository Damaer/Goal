import Record from '../models/Record'
import Goal from '../models/Goal'
import DailySentence from '../models/DailySentence'
import {getToday} from 'util/util'

let codeMsg = {
	10000: 'success',
	10404: 'unkown error'
}

exports.index = (req, res, next) => {
	let userId = req.user._id;
	Record.find({user: userId}, (err, records) => {
		if (err) {
			console.log(err);
			return res.json({code: 10404, msg: '未找到当前用户记录'});
		}
		res.json({code: 10000, msg: '查找成功', data: records});
	})
}

exports.today = (req, res, next) => {
	let userId = req.user._id,
			today = getToday();
	Record.findOne({user: userId, date: today}, (err, record) => {
		if (err) {
			return res.json({code: 10404, msg: '查询失败'});
		}
		new Promise((resolve, reject) => {
			if (!record) {
				createRecord(userId).then(newRecord => resolve(newRecord), err => reject(err));
			} else {
				resolve(record);
			}
		}).then(record => {
			record.populate({path: 'dailySentence'})
				.exec((err, record) => {
					if (err) {
						console.log(err);
						return res.json({code: 10404, msg: '查询失败'});
					}
					res.json({code: 10000, msg: '', data: record});
				})
		}, err => {
			console.log(err);
			res.json({code: 10404, msg: '查询失败'});
		})
		
	})
}

exports.dailySentence = (req, res, next) => {
	let userId = req.user._id,
			today = getToday();
	Record.find({date: today, user: userId}, (err, record) => {
		if (err) {
			return res.json({code: 10404, msg: '查询失败'});
		}
		if (!record) {
			createRecord(userId).then((record, dailySentence) => res.json({code: 10000, msg: '', data: dailySentence}), err => res.json({code: 10404, msg: '查询失败'}));
		} else {
			res.json({code: 10000, msg: ''})
		}
	})
}

exports.getGoalsFinished = (req, res, next) => {
	let userId = req.user._id,
			today = getToday();
	getGoalsFinishedId(userId, today).then(goalsId => {
		let promises = [];
		let goals = [];
		goalsId.map(goalId => {
			promises.push(new Promise((resolve, reject) => {
				Goal.findById(goalId, (err, goal) => {
					if (!err && goal) {
						goals.push(goal);
					}
					resolve();
				})
			}))
		})
		Promise.all(promises).then(() => {
			res.json({code: 10000, msg: '', data: goals});
		}, err => {
			console.log(err);
		})
	})
}

exports.goalsFinishedNums = (req, res, next) => {
	let userId = req.user._id,
			today = getToday();
	getGoalsFinishedId(userId, today).then(goalsId => {
		res.json({code: 10000, msg: '', data: goalsId.length});
	})
}

exports.markGoalsFinished = (req, res, next) => {
	let userId = req.user._id,
			today = getToday();
	const {goalId} = req.body;
	Goal.findById(goalId, (err, goal) => {
		if (err || !goal) {
			return res.json({code: 10404, msg: '目标不存在'});
		}
		Record.findOne({user: userId, date: today}, (err, record) => {
			if (err) {
				console.log(err);
				return res.json({code: 10404, msg: '标记失败'});
			}
			new Promise((resolve, reject) => {
				if (!record) {
					createRecord(userId).then(record => resolve(record), err => reject(err));
				} else {
					resolve(record);
				}
			}).then(record => {
				record.goalsFinished.push(goal);
				record.save(err => {
					if (err) {
						console.log(err);
						res.json({code: 10404, msg: '目标达成标记失败'});
					} else {
						res.json({code: 10000, msg: '目标完成成功'});
					}
				})
			}, err => {
				res.json({code: 10404, msg: '查询失败'});
			})
		})
	})
}

let getGoalsFinishedId = (userId, today) => new Promise((resolve, reject) => {
	Record.find({date: today, user: userId}, (err, record) => {
		if (err || !record) {
			resolve([]);
		} else {
			resolve(record.goalsFinished);
		}
	})
})

let createRecord = userId => new Promise((resolve, reject) => {
	let today = getToday();
	let record = new Record();
	record.user = userId;
	record.date = today;
	getSentenceRandomly().then(dailySentence => {
		record.dailySentence = dailySentence._id;
		record.save((err, product) => {
			if (err) {
				return reject({code: 10404, msg: '保存失败'});
			}
			resolve(product, dailySentence);
		})
	}, err => {
		reject(err);
	})
})

let getSentenceRandomly = () => new Promise((resolve, reject) => {
	DailySentence.find({}, (err, dailySentence) => {
		if (err) {
			return reject({code: 10404, msg: 'error in getSentenceRandomly'});
		}
		if (dailySentence.length === 0) {
			return reject({code: 10404, msg: 'database error'});
		}
		let r = parseInt(Math.random() * dailySentence.length);
		resolve(dailySentence[r]);
	})
})