import User from '../models/User'
import Token from '../models/Token'
import bcrypt from 'bcrypt-nodejs'

const AUTHORITY_ADMIN = 666;

let codeMsg = {
	10000: 'ok',
	10200: 'param error',
	10400: 'username error',
	10401: 'password error',
	10402: 'token error',
	10403: 'auth error'
}

exports.login = (req, res, next) => {
	const {username, password} = req.body;
	if (!username || !password) {
		return res.json({code: 10200, msg: 'params error'});
	}
	User.findOne({name: username}, (err, user) => {
		if (err || !user) {
			return res.json({code: 10400, msg: '账号不存在'});
		}

		user.auth(password).then(() => {
			create_access_token(user._id).then(tokenString => {
				res.header('Authorization', tokenString);
				res.json({code: 10000, msg: '登录成功', data: {
					username: user.name,
					avatar: user.avatar,
					description: user.description,
					authorization: user.authority
				}})
			}, err => {
				console.log(err);
				res.json({code: 10404, msg: '登录失败，请重新尝试'});
			})
		}, err => {
			res.json(err);
		})
	})
}

exports.logout = (req, res, next) => {
	let tokenString = req.headers.authorization;
	if (tokenString) {
		Token.findOne({token: tokenString}, (err, token) => {
			if (!err && token) {
				token.used = true;
				token.save(err => {
					if (err) {
						console.log(err);
					}
				})
			}
		})
	}
	res.json({code: 10000});
}

let create_access_token = userId => new Promise((resolve, reject) => {
	let token = new Token(),
			time = Date.now() + 3 * 24 * 60 * 60 * 1000, // 3 day
			tokenString = bcrypt.hashSync(userId + time);
	token.token = tokenString;
	token.user = userId;
	token.expire = time;
	token.save(err => {
		if (err) {
			reject(err);
		} else {
			resolve(tokenString);
		}
	})
})

let refresh_access_token = oldToken => new Promise((resolve, reject) => {
	let time = Date.now() + 3 * 24 * 60 * 60 * 1000;
	oldToken.expire = time;
	oldToken.save(err =>  {
		if (err) {
			reject(err);
		} else {
			resolve();
		}
	})
})
exports.refresh_access_token = refresh_access_token;

exports.auth = (req, res, next) => {
	let tokenString = req.headers.authorization,
			time = Date.now();
	if (!tokenString) {
		return res.json({code: 10200, msg: '请先登录'});
	}
	Token.findOne({token: tokenString}, (err, token) => {
		if (err || !token || token.expire < time || token.used) {
			return res.json({code: 10402, msg: '登录超时'});
		}

		User.findById(token.user, (err, user) => {
			if (err || !user) {
				return res.json({code: 10404, msg: '未找到当前用户'});
			}
			
			refresh_access_token(token).then(() => {}, err => {
				console.log(err);
			})

			req.user = user;
			next();
		})
	})
}

exports.auth_current_user = (req, res, next) => {
	let id = req.params.id;
	if (!req.user || req.user._id != id) {
		return res.json({code: 10403, msg: '权限不足'});
	}
	next();
}

exports.auth_admin = (req, res, next) => {
	if (!req.user || req.user.authority < AUTHORITY_ADMIN) {
		return res.json({code: 10403, msg: '权限不足'});
	}
	next();
}