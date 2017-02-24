import Comment from '../models/Comment'
import CommentLikeMap from '../models/CommentLikeMap'

let codeMsg = {

}

/**
 * 获取指定目标Id下的直接评论信息
 */
exports.index = (req, res, next) => {
	let goalId = req.params.id;
	Comment.find({goal: goalId}).populate({path: 'user', select: ['name', 'avatar']}).exec((err, comments) => {
		if (err) return res.json({code: 10500, msg: '数据库查询失败'});
		goalComments = comments.toJSON();

		let promises = [];
		// 查询评论下的回复数量及点赞的用户id数组
		for (var i = 0; i < goalComments.length; i++) {
			promises.push(new Promise(resolve, reject) => {
				Comment.count({reply: goalComments[i]._id}, (err, count) => {
					if (err) count = 0;
					goalComments[i].reply = count;
					resolve();
				})
			})
			promises.push(new Promise(resolve, reject) => {
				CommentLikeMap.find({comment: goalComments[i]._id}, (err, commentLikeMaps) => {
					if (err) commentLikeMaps = [];
					goalComments[i].like = commentLikeMaps.map(commentLikeMap => commentLikeMap.user);
					resolve();
				})
			})
		}

		new Promise.all(promises).then(() => {
			res.json({code: 10000, msg: '', data: goalComments});
		}, err => {
			res.json({code :10500, msg: '服务器出错'});
			console.log(err);
		})
	})
}

/*
	评论指定目标
 */
exports.save = (req, res, next) => {
	let goalId = req.params.id
			userId = req.user._id
			content = req.body.content;
	let comment = new Comment;
	comment.user = userId;
	comment.goal = goalId;
	comment.content = content;
	comment.save((err, product) => {
		if (err) return res.json({code: 10500, msg: '评论失败,请检查参数是否合法'});
		res.json({code: 10000, msg: '', data: product._id});
	})
}

/*
	点赞
 */
exports.like = (req, res, next) => {
	let id = req.params.id,
			userId = req.user._id;
	CommentLikeMap.findOne({_id: id, user: userId}, (err, comment) => {
		if (err) return res.json({code: 10500, msg: '数据库查询错误'});
		if (comment) return res.json({code: 10200, msg: '您已经赞过该评论'});
		let commentLikeMap = new CommentLikeMap;
		commentLikeMap.user = userId;
		commentLikeMap.comment = id;
		commentLikeMap.save((err, product) => {
			if (err) return res.json({code: 10500, msg: '数据库错误,请重新尝试'});
			res.json({code: 10000, msg: '', data: product});
		})
	})
}

exports.getReplyList = (req, res, next) => {
	let id = req.params.id;
	Comment.find({reply: id}).populate({path: 'user', select: ['name', 'avatar']}).exec((err, comments) => {
		if (err) return res.json({code: 10500, msg: '数据库查询失败'});
		comments = comments.toJSON();

		let promises = [];
		// 查询评论下的回复数量及点赞的用户id数组
		for (var i = 0; i < comments.length; i++) {
			promises.push(new Promise(resolve, reject) => {
				Comment.count({reply: comments[i]._id}, (err, count) => {
					if (err) count = 0;
					comments[i].reply = count;
					resolve();
				})
			})
			promises.push(new Promise(resolve, reject) => {
				CommentLikeMap.find({comment: comments[i]._id}, (err, commentLikeMaps) => {
					if (err) commentLikeMaps = [];
					comments[i].like = commentLikeMaps.map(commentLikeMap => commentLikeMap.user);
					resolve();
				})
			})
		}

		new Promise.all(promises).then(() => {
			res.json({code: 10000, msg: '', data: comments});
		}, err => {
			res.json({code :10500, msg: '服务器出错'});
			console.log(err);
		})
	})
}

exports.reply = (req, res, next) => {
	let id = req.params.id,
			userId = req.user._id,
			content = req.body.content;
	let comment = new Comment;
	comment.reply = id;
	comment.user = userId;
	comment.content = content;
	comment.save((err, product) => {
		if (err) return res.json({code: 10500, msg: '评论失败,请检查参数是否合法'});
		res.json({code: 10000, msg: '', data: product._id});
	})
}

exports.delete = (req, res, next) => {
	let id = req.params.id,
			userId = req.user._id;
	Comment.remove({_id: id, user: userId}, err => {
		if (err) return res.json({code: 10500, msg: '删除失败'});
		res.json({code: 10000, msg: ''});
	})
}