import mongoose from 'mongoose'
import path from 'path'
import bcrypt from 'bcrypt-nodejs'

let Schema = mongoose.Schema
let ObjectId = Schema.Types.ObjectId

let UserSchema = new Schema({
	name: {
		type: String,
		unique: true,
		require: true
	},
	password: {
		type: String,
		require: true
	},
	email: {
		type: String,
		unique: true
	},
	phone: {
		type: String,
		unique: true
	},
	avatar: {
		type: String,
		default: 'avatar.png'
	},
	meta: {
		createAt: {
			type: Date,
			default: Date.now()
		},
		updateAt: {
			type: Date,
			default: Date.now()
		}
	},
	description: {
		type: String,
		default: "你永远不知道自己可以做得多好"
	},
	authority: {
		type: String,
		default: 0
	}
})

UserSchema.pre('save', function(next) {
	if (this.isNew) {
		this.meta.createAt = this.meta.updateAt = Date.now();
	} else {
		this.meta.updateAt = Date.now();
	}

	if (!this.isModified('password')) {
		return next();
	}

	let hash = bcrypt.hashSync(this.password);
	this.password =  hash;

	next();
})

UserSchema.methods = {
	auth: function (_password) {
		return new Promise((resolve, reject) => {
			let hash = this.password;
			let isMatch = bcrypt.compareSync(_password, hash);
			if (isMatch) {
				resolve();
			} else {
				reject("password error");
			}
		})
	}
}

let User = mongoose.model('User', UserSchema, 'user')

module.exports = User;