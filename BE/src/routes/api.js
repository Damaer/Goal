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
	.get(Note.index)
	.post(Note.save)
router.route('/note/:id')
	.get(Note.read)
	.put(Note.update)
	.delete(Note.delete)

// goal
router.route('/goal')
	.get(Goal.index)
	.post(Goal.save)
router.route('/goal/:id')
	.get(Goal.read)
	.put(Goal.update)
	.delete(Goal.delete)

// record
router.route('/record')
	.get(Record.index)
router.route('/record/today')
	.get(Record.today)
router.route('/record/goalsFinished')
	.get(Record.getGoalsFinished)
	.post(Record.markGoalsFinished)
router.route('/record/goalsFinishedNums')
	.get(Record.goalsFinishedNums)
router.route('/record/dailySentence')
	.get(Record.dailySentence)

module.exports = router;