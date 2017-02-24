import mongoose from 'mongoose'

let Schema = mongoose.Schema;
let ObjectId = Schema.Types.ObjectId;

let CommentSchema = new Schema({
	user: {
		type: ObjectId,
		ref: 'User'
	},
	goal: {
		type: ObjectId,
		ref: 'Goal',
		default: null
	},
	content: {
		type: String
	},
	reply: {
		type: ObjectId,
		ref: 'Comment',
		default: null
	}
})

let Comment = mongoose.model('Comment', CommentSchema, 'comment');
module.exports = Comment;