import DailySentence from '../models/DailySentence'
import validate from './validate/dailySentence'

exports.index = (req, res, next) => {
	DailySentence.find({}, (err, dailySentences) => {
		if (err || !dailySentences) {
			res.json({code: 10404, msg: 'not found dailySentence'});
		} else {
			res.json({code: 10000, data: dailySentences});
		}
	})
}

exports.save = (req, res, next) => {
	validate.create(req.body).then(() => {
		let {sentence, backImg} = req.body;
		let dailySentence = new DailySentence;
		dailySentence.sentence = sentence;
		dailySentence.backImg = backImg;
		dailySentence.save((err, product) => {
			if (err) {
				console.log(err);
				res.json({code: 10500, msg: '创建失败'});
			} else {
				res.json({code: 10000, msg: '创建成功', data: product._id});
			}
		})
	}, err => {
		res.json(err);
	})
}

exports.read = (req, res, next) => {
	DailySentence.findById(req.params.id, (err, dailySentence) => {
		if (err || !dailySentence) {
			res.json({code: 10404, msg: 'not found'});
		} else {
			res.json({code: 10000, data: dailySentence});
		}
	})
}

exports.update = (req, res, next) => {
	validate.update(req.params.id, res.body).then(dailySentence => {
		const {sentence, backImg} = req.body;
		dailySentence.sentence = sentence;
		dailySentence.backImg = backImg;
		dailySentence.save(err => {
			if (err) {
				console.log(err);
				res.json({code: 10404, msg: '修改信息失败'});
			} else {
				res.json({code: 10000, msg: '修改信息成功'});
			}
		})
	}, err => {
		res.json(err);
	})
}

exports.delete = (req, res, next) => {
	DailySentence.remove({_id: req.params.id}, err => {
		if (err) {
			res.json({code: 10404, msg: '删除失败: 未找到要删除的用户'});
		} else {
			res.json({code: 10000, msg: '删除成功'});
		}
	})
}