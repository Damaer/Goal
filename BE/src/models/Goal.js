import mongoose from 'mongoose'

let Schema = mongoose.Schema
let ObjectId = Schema.Types.ObjectId

let GoalSchema = new Schema({
	user: {
		type: ObjectId,
		ref: 'User'
	},
	title: {
		type: String
	},
	content: {
		type: String
	},
	time: {
		begin: {
			type: Date
		},
		plan: {
			type: Date
		},
		finish: {
			type: Date
		}
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
	finish: {
		type: Boolean,
		default: false
	},
	delete: {
		type: Boolean,
		default: false
	}
})

GoalSchema.pre('save', function(next) {
	if (this.isNew) {
		this.meta.createAt = this.meta.updateAt = Date.now();
	} else {
		this.meta.updateAt = Date.now();
	}

	next();
})

let Goal = mongoose.model('Goal', GoalSchema, 'goal')

module.exports = Goal;