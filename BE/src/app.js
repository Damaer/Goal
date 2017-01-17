'use strict';

import express from 'express'
import path from 'path'
import logger from 'morgan'
import apiRoutes from './routes/api'

// 3rd middleware
import bodyParser from 'body-parser'
import mongoose from 'mongoose'

// database
import config from './config'
let dbUrl = `mongodb://${config.USERNAME}:${config.PASSWORD}@${config.HOST}:${config.PORT}/${config.DB}`;
mongoose.Promise = global.Promise;
mongoose.connect(dbUrl)

process.on('uncaughtException', function (err) {
	res.status(500);
	res.json({code: 10500, msg: 'server error'});
  console.log('Caught exception: ', err);
});

var app = express();

app.use(logger('dev'));
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }))

app.use('/api', apiRoutes);

// catch 404 and forward to error handler
app.use((req, res, next) => {
	var err = new Error('Not Found');
	err.status = 404;
	next(err);
});

if (app.get('env') === 'dev') {
	app.set('showStackError', true)
	app.locals.pretty = true
	app.use((err, req, res, next) => {
		res.status(err.status || 500);
		res.json({msg: err.message, error: err})
	});
}

module.exports = app;