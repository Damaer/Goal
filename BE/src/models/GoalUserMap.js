import mongoose from 'mongoose'

let Schema = mongoose.Schema
let ObjectId = Schema.Types.ObjectId

let GoalUserMapSchema = new Schema({
	user: {
		type: ObjectId,
		ref: 'User'
	},
	goal: {
		type: ObjectId,
		ref: 'Goal'
	},
	begin: { // 用户计划开始时间
		type: Date
	},
	plan: { // 用户计划结束时间
		type: Date
	},
	end: { // 用户实际完成时间
		type: Date,
		default : 0 // 0表示未完成
	},
	finish: { // 目标是否已完成
		type: Boolean,
		default: false
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
	public: { // 目标能否被其他用户查看到 
		type: Boolean,
		default: false
	}
})

GoalUserMapSchema.pre('save', function (next) {
	if (this.isNew) {
		this.meta.createAt = this.meta.updateAt = Date.now();
	} else {
		this.meta.updateAt = Date.now();
	}

	next();
})

let GoalUserMap = mongoose.model('GoalUserMap', GoalUserMapSchema, 'goalUserMap');

module.exports = GoalUserMap;