import Goal from '../models/Goal'
import validate from './validate/goal'

let codeMsg = {
	10000: 'success',
	10404: 'unkown error'
}

exports.index = (req, res, next) => {
	let userId = req.user._id;
	Goal.find({user: userId}, (err, goals) => {
		if (err) {
			console.log(err);
			return res.json({code: 10404, msg: '未找到当前用户的目标'});
		}
		res.json({code: 10000, msg: '查找成功', data: goals});
	})
}

exports.save = (req, res, next) => {
	validate.create(req.body).then(() => {
		const {title, content, begin, plan} = req.body;
		let goal = new Goal();
		goal.user = req.user._id;
		goal.title = title;
		goal.content = content;
		goal.time.begin = begin;
		goal.time.plan = plan;
		goal.save((err, product) => {
			if (err) {
				console.log(err);
				res.json({code: 10404, msg: '目标创建失败'});
			} else {
				res.json({code: 10000, msg: '目标创建成功', data: product._id});
			}
		})
	}, err => {
		console.log(err);
		res.json(err);
	})
}

exports.read = (req, res, next) => {
	let userId = req.user._id,
			id = req.params.id;
	Goal.find({user: userId, _id: id}, (err, goal) => {
		if (err || !goal) {
			return res.jsoo({code: 10404, msg: 'not found'});
		}
		res.json({code: 10000, msg: '', data: goal});
	})
}

exports.update = (req, res, next) => {
	validate.update(req.user._id, req.params.id, req.body).then(goal => {
		const {title, content, begin, plan} = req.body;
		goal.title = title;
		goal.content = content;
		goal.time.begin = begin;
		goal.time.plan = plan;
		goal.save(err => {
			if (err) {
				return res.json({code: 10404, msg: '修改信息失败'});
			}
			res.json({code: 10000, msg: '修改信息成功'});
		})
	}, err => {
		res.json(err);
	})
}

exports.delete = (req, res, next) => {
	let userId = req.user._id
			id = req.params.id;
	Goal.remove({user: userId, _id: id}, err => {
		if (err) {
			return res.json({code: 10404, msg: '删除失败'});
		}
		res.json({code: 10000, msg: '删除成功'});
	})
}