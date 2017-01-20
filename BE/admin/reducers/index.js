import { combineReducers } from 'redux'
import admin from './admin'
import commonModule from './commonModule'
import sideBar from './sideBar'

const rootReducer = combineReducers({
	admin,
	commonModule,
	sideBar
})

export default rootReducer