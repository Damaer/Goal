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

exports.create = data => new Promise((resolve, reject) => {
	const {name, password} = data;
	if (!name || !password) {
		return reject({code: 10200, msg: '参数错误'});
	}
	let promises = [];
	promises.push(Cname(name));
	promises.push(Cpassword(password));
	Promise.all(promises).then(() => {
		resolve();
	}, err => {
		reject(err);
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
	const {name, password} = data;
	user.auth(password).then(() => {
		if (name == usre.name) {
			return resolve();
		}
		Cname(name).then(() => {
			resolve();
		}, err => {
			reject(err);
		})
	}, err => {
		reject(err);
	})
})

exports.Cemail = _email => new Promise((resolve, reject) => {
	let reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	if (reg.tset(_email)) {
		User.findOne({email: _email}, (err, user) => {
			if (err || user) {
				return reject({code: 10402, msg: '邮箱已被注册'});
			}
			resolve();
		})
	} else {
		reject({code: 10402, msg: '邮箱格式不正确'});
	}
})

exports.Cphone = _phone => new Promise((resolve, reject) => {
	let reg = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$/;
	if (reg.test(_phone)) {
		User.findOne({phone: _phone}, (err, user) => {
			if (err || user) {
				return reject({code: 10403, msg: '手机号已被注册'});
			}
			resolve();
		})
	} else {
		reject({code: 10403, msg: '手机号格式不正确'})
	}
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