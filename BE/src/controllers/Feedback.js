import Feedback from '../models/Feedback'

let codeMsg = {

}

exports.save = (req, res, next) => {
	let {content, contact} = req.body;
	let feedback = new Feedback;
	feedback.contact = contact;
	feedback.content = content;
	feedback.save(err => {
		if (err) return res.json({code: 10500, msg: '数据库保存失败,请重新尝试'});
		res.json({code: 10000, msg: '反馈成功'});
	})
}