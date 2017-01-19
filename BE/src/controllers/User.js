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

exports.getUserInfo = (req, res, next) => {
	res.json({code: 10000, msg: '', data: req.user});
}

exports.updateUserInfo = (req, res, nect) => {
	let user = req.user;
	validate.updateInfo(user, req.body).then(() => {
		const {name, avatar} = req.body;
		user.name = name;
		user.avatar = avatar;
		user.save(err => {
			if (err) {
				return res.json({code: 10404, msg: '更新信息失败，请重新尝试'});
			}
			res.json({code: 10000, msg: '更新信息成功'});
		})
	}, err => {
		res.json(err);
	})
}

exports.updatePassword = (req, res, next) => {
	let user = req.user;
	const {oldPassword, password} = req.body;
	// 检查oldPassword是否正确
	user.auth(oldPassword).then(() => {
		user.password = password;
		user.save(err => {
			if (err) {
				return res.json({code: 10404, msg: '密码更新失败，请重新尝试'});
			}
			res.json({code: 10000, msg: '密码更新成功'});
		})
	}, err => {
		res.json(err);
	})
}

exports.update = (req, res, next) => {
	// 不支持更改密码
	let user = req.user;
	validate.update(user, req.body).then(() => {
		const {name, avatar, email, phone, description} = req.body;
		user.name = name;
		user.avatar = avatar;
		if (email != "null") user.email = email;
		if (phone != "null") user.phone = phone;
		user.description = description;
		user.save(err => {
			if (err) {
				console.log(err);
				return res.json({code: 10404, msg: '修改信息失败'});
			}
			res.json({code: 10000, msg: '修改信息成功'});
		})
	}, err => {
		res.json(err);
	})
	res.json({code: 10404, msg: '功能开发中'});
}

exports.update_email = (req, res, next) => {
	// 暂不加入邮箱验证功能
	let user = req.user;
	const {email} = req.body;
	validate.Cemail(email).then(() => {
		user.email = email;
		user.save(err => {
			if (err) {
				return res.json({code: 10404, msg: '邮箱绑定失败，请重新尝试'});
			}
			res.json({code: 10000, msg: '邮箱绑定成功'});
		})
	}, err => {
		res.json(err);
	})
}
 
exports.update_phone = (req, res, next) => {
	// 暂不加入手机验证功能
	let user = req.user;
	const {phone} = req.body;
	validate.Cphone(phone).then(() => {
		user.phone = phone;
		user.save(err => {
			if (err) {
				return res.json({code: 10404, msg: '手机号绑定失败，请重新尝试'});
			}
			res.json({code: 10000, msg: '手机号绑定成功'});
		})
	}, err => {
		res.json(err);
	})
}

exports.delete = (req, res, next) => {
	User.remove({_id: req.params.id}, err => {
		if (err) {
			res.json({code: 10404, msg: '删除失败: 未找到要删除的用户'});
		} else {
			res.json({code: 10000, msg: '删除成功'});
		}
	})
}