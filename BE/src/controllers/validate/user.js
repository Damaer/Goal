import User from '../../models/User'

let codeMsg = {
	10000: 'create successfully',
	10200: 'params error',
	10400: 'name error',
	10401: 'password error',
	10402: 'email error',
	10403: 'phone error',
	10404: 'unkown error'
}

/**
 * 创建账号
 * @param  {json} data 要验证的数据
 * @return {boolean}      若注册账号为邮箱，返回true,反之返回false
 */
exports.create = data => new Promise((resolve, reject) => {
	const {name, password} = data;
	if (!name || !password) {
		return reject({code: 10200, msg: 'params error'});
	}
	new Promise((resolve, reject) => {
		CemailFormat(name).then(() => {
			// 注册账号名为邮箱
			resolve(true);
		}, err => {
			CphoneFormat(name).then(() => {
				// 注册账号为手机号
				resolve(false);
			}, err => {
				reject();// 注册账号格式不符合
			})
		})
	}).then(isEmail => {
		new Promise((resolve, reject) => {
			if (isEmail) {
				// 检查邮箱是否被注册
				Cemail(name).then(() => {
					resolve(isEmail);
				}, err => {
					reject(err)
				});
			} else {
				// 检查手机号是否被注册
				Cphone(name).then(() => {
					resolve(isEmail);
				}, err => {
					reject(err);
				})
			}
		}).then(isEmail => {
			// 判断
			Cpassword(password).then(() => {
				resolve(isEmail); // 数据验证成功
			}, err => {
				// 密码格式错误
				reject(err);
			})
		}, err => {
			reject(err);
		})
	}, err => {
		reject({code: 10402, msg: "账号格式不符合"});
	})
})

exports.update = (id, data) => new Promise((resolve, reject) => {
	const {name} = data;
	User.findOne({_id: id}, (err, user) => {
		if (err || !user) {
			return reject({code: 10200, msg: 'params error'});
		}
		if (user.name == name) {
			return resolve(user);
		}
		Cname(name).then(() => {
			resolve(user);
		}, err => {
			reject(err);
		})
	})
})

exports.updateInfo = (user, data) => new Promise((resolve, reject) => {
	const {name} = data;
	if (name == user.name) {
		return resolve();
	}
	Cname(name).then(() => {
		resolve();
	}, err => {
		reject(err);
	})
})

let CemailFormat = _email => new Promise((resolve, reject) => {
	let reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	if (reg.test(_email)) {
		return resolve();
	}
	reject();
})

let Cemail = _email => new Promise((resolve, reject) => {
	User.findOne({email: _email.toLowerCase()}, (err, user) => {
		if (err || user) {
			return reject({code: 10402, msg: '邮箱已被注册'});
		}
		resolve();
	})
})

let CphoneFormat = _phone => new Promise((resolve, reject) => {
	let reg = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
	if (reg.test(_phone)) {
		return resolve();
	}
	reject();
})

let Cphone = _phone => new Promise((resolve, reject) => {
	User.findOne({phone: _phone}, (err, user) => {
		if (err || user) {
			return reject({code: 10403, msg: '手机号已被注册'});
		}
		resolve();
	})
})

let Cname = _name => new Promise((resolve, reject) => {
	User.findOne({name: _name}, (err, user) => {
		if (err || user) {
			reject({code: 10400, msg: '用户名已存在'});
		} else {
			if (_name.length > 5) {
				resolve();
			} else {
				reject({code: 10400, msg: '用户名长度过短'});
			}
		}
	})
})

let Cpassword = _password => new Promise((resolve, reject) => {
	if (_password.length > 5) {
		resolve();
	} else {
		reject({code: 10401, msg: '密码长度过短'});
	}
})