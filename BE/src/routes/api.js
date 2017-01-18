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

// ///////////////////
// // note
// router.route('/note/user/:id')
// 	.get(Note.findByUserId)

// // goal
// router.route('/goal/user/:id')
// 	.get(Goal.findByUserId)

// // record
// router.route('/record/user/:id')
// 	.get(Record.findByUserId)

module.exports = router;