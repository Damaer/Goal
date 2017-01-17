'use strict';

import express from 'express'

import User from '../controllers/User'

let router = express.Router();

// user
router.route('/user')
	.get(User.index)
	.post(User.save)
router.route('/user/:id')
	.get(User.read)
	.put(User.update)
	.delete(User.delete)

module.exports = router;