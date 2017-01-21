'use strict';

import express from 'express'

import User from '../controllers/User'
import DailySentence from '../controllers/DailySentence'
import Authentication from '../controllers/Authenticate'
import Record from '../controllers/Record'
import Note from '../controllers/Note'
import Goal from '../controllers/Goal'

let router = express.Router();

// authentication
router.route('/login')
	.post(Authentication.login)
router.route('/logout')
	.get(Authentication.logout)
router.route('/register')
	.post(User.save)

///////////////////admin api////////////
// upload avatar img
router.route('/user/avatar')
	.post(Authentication.auth, User.upload_img)
// user
router.route('/user/info')
	.get(Authentication.auth, User.get_user_info)
	.put(Authentication.auth, User.update_user_info)
router.route('/user/password')
	.put(Authentication.auth, User.update_password)
router.route('/user/count')
	.get(Authentication.auth, Authentication.auth_admin, User.count)

router.route('/user')
	.get(Authentication.auth, Authentication.auth_admin, User.index)
	.post(Authentication.auth, Authentication.auth_admin, User.save)
router.route('/user/:id')
	.get(Authentication.auth, Authentication.auth_admin, User.read)
	.put(Authentication.auth, Authentication.auth_admin, User.update)
	.delete(Authentication.auth, Authentication.auth_admin, User.delete)

// dailySentence
router.route('/dailySentence/count')
	.get(Authentication.auth, Authentication.auth_admin, DailySentence.count)
router.route('/dailySentence')
	.get(Authentication.auth, Authentication.auth_admin, DailySentence.index)
	.post(Authentication.auth, Authentication.auth_admin, DailySentence.save)
router.route('/dailySentence/:id')
	.get(Authentication.auth, Authentication.auth_admin, DailySentence.read)
	.put(Authentication.auth, Authentication.auth_admin, DailySentence.update)
	.delete(Authentication.auth, Authentication.auth_admin, DailySentence.delete)

//////////////login required//////////////

// email
router.route('/email')
	.put(Authentication.auth, User.update_email)
// phone
router.route('/phone')
	.put(Authentication.auth, User.update_phone)

// note
router.route('/note')
	.get(Authentication.auth, Note.index)
	.post(Authentication.auth, Note.save)
router.route('/note/:id')
	.get(Authentication.auth, Note.read)
	.put(Authentication.auth, Note.update)
	.delete(Authentication.auth, Note.delete)

// goal
router.route('/goal')
	.get(Authentication.auth, Goal.index)
	.post(Authentication.auth, Goal.save)
router.route('/goal/:id')
	.get(Authentication.auth, Goal.read)
	.put(Authentication.auth, Goal.update)
	.delete(Authentication.auth, Goal.delete)

// record
router.route('/record')
	.get(Authentication.auth, Record.index)
router.route('/record/today')
	.get(Authentication.auth, Record.today)
router.route('/record/goalsFinished')
	.get(Authentication.auth, Record.getGoalsFinished)
	.post(Authentication.auth, Record.markGoalsFinished)
router.route('/record/goalsFinishedNums')
	.get(Authentication.auth, Record.goalsFinishedNums)
router.route('/record/dailySentence')
	.get(Authentication.auth, Record.dailySentence)

module.exports = router;