import User from '../models/User'
import validate from './validate/user'

exports.index = (req, res, next) => {
	User.find({}, (err, users) => {
		if (err || !users) {
			res.json({code: 10404, msg: 'not found users'});
		} else {
			res.json({code: 10000, data: users});
		}
	})
}

exports.save = (req, res, next) => {
	validate.create(req.body).then(() => {
		let {name, password} = req.body;
		let user = new User;
		user.name = name;
		user.password = password;
		user.save((err, product) => {
			if (err) {
				console.log(err);
				res.json({code: 10500, msg: '用户创建失败'});
			} else {
				res.json({code: 10000, msg: '注册成功', data: product._id});
			}
		})
	}, err => {
		res.json(err);
	})
}

exports.read = (req, res, next) => {
	User.findById(req.params.id, (err, user) => {
		if (err || !user) {
			res.json({code: 10404, msg: 'not found'});
		} else {
			res.json({code: 10000, data: user});
		}
	})
}

exports.update = (req, res, next) => {
	res.json({code: 104040, msg: '功能开发中...'});
}

exports.delete = (req, res, next) => {
	User.remove({id: req.params.id}, err => {
		if (err) {
			res.json({code: 10404, msg: '删除失败: 未找到要删除的用户'});
		} else {
			res.json({code: 10000, msg: '删除成功'});
		}
	})
}