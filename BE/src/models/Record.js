import mongoose from 'mongoose'

let Schema = mongoose.Schema
let ObjectId = Schema.Types.ObjectId

let RecordSchema = new Schema({
	user: {
		type: ObjectId,
		ref: 'User'
	},
	date: {
		type: Date
	},
	dailySentence: {
		type: ObjectId,
		ref: 'DailySentence'
	},
	goalsFinished: {
		type: Number,
		default: 0
	}
})

let Record = mongoose.model('Record', RecordSchema, 'record');

module.exports = Record;