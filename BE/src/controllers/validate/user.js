import User from '../../models/User'

let codeMsg = {
	10000: 'create successfully',
	10200: 'params error',
	10400: 'name error',
	10401: 'password error',
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

let Cname = _name => new Promise((resolve, reject) => {
	User.findOne({name: _name}, (err, user) => {
		if (err || user) {
			reject({code: 10400, msg: '用户名已存在'});
		} else {
			resolve();
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