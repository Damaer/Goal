import mongoose from 'mongoose'

let Schema = mongoose.Schema;

let FeedbackSchema = new Schema({
	content: {
		type: String
	},
	contact: {
		type: String
	}
})

let Feedback = mongoose.model('Feedback', FeedbackSchema, 'feedback');

module.exports = Feedback;