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

// user
router.route('/user')
	.get(User.index)
	.post(User.save)
router.route('/user/:id')
	.get(User.read)
	.put(User.update)
	.delete(User.delete)

// email
router.route('/email/:id')
	.put(User.update_email)
// phone
router.route('/phone/:id')
	.put(User.update_phone)

// dailySentence
router.route('/dailySentence')
	.get(DailySentence.index)
	.post(DailySentence.save)
router.route('/dailySentence/:id')
	.get(DailySentence.read)
	.put(DailySentence.update)
	.delete(DailySentence.delete)

//////////////login required//////////////
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