import fs from 'fs'
import crypto from 'crypto'
import path from 'path'

import User from '../models/User'
import validate from './validate/user'

const uploadConfig = {
	distDir: '/public/avatar'
}

exports.index = (req, res, next) => {
	const {page, rows} = req.query;
	// 分页查询
	if (page && rows) {
		User.find({}, {password: 0})
				.skip((page - 1) * rows)
				.limit(rows)
				.exec((err, users) => {
					if (err) {
						res.json({code: 10404, msg: 'params error'});
					} else {
						res.json({code: 10000, msg: '', data: users});
					}
				})
	} else {
		User.find({}, {password: 0}, (err, users) => {
			if (err || !users) {
				res.json({code: 10404, msg: 'not found users'});
			} else {
				res.json({code: 10000, data: users});
			}
		})
	}
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
	validate.update(req.params.id, req.body).then(user => {
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

exports.uploadImg = (req, res, next) => {
	if (!req.files || !req.files.avatar) {
		return res.json({code: 10200, msg: 'params error'});
	}
	let file = req.files.avatar
			tmpPath = file.path;
	// get md5 of the upload file
	let rs = fs.createReadStream(tmpPath),
			hash = crypto.createHash('md5');
	rs.on('data', hash.update.bind(hash));
	rs.on('end', function () {
		let md5 = hash.digest('hex');
		let newPath = path.join(uploadConfig.distDir, md5);
		fs.rename(tmpPath, newPath, err => {
			if (err) {
				console.log(err);
				return res.json({code: 10404, msg: '上传图片失败，请尝试重新上传'});
			}
			res.json({code: 10000, msg: '上传图片成功', data: newPath});
			// delete tmp file
			fs.unlink(tmpPath, err => {
				if (err) {
					console.log(err);
				}
			})
		})
	});
}

exports.count = (req, res, next) => {
	User.count({}, (err, count) => {
		if (err) {
			console.log(err);
			return res.json({code: 10404, msg: '查询失败'});
		}
		res.json({code: 10000, msg: '', data: count});
	})
}