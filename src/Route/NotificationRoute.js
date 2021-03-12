const express = require('express');
const { authenticate } = require('passport');
const passport = require('passport');
const notificationController = require('../Controllers/NotificationController');
require('../middleware/passport.js');

const router = express.Router();

//GetMethod
router.get('/', passport.authenticate('jwt', { session: false }), notificationController.getAllbyUser);

//Patch Method
router.patch('/status', passport.authenticate('jwt', { session: false }), notificationController.changeSeenStatusNoti);
router.patch(
	'/status/new',
	passport.authenticate('jwt', { session: false }),
	notificationController.changeNewStatusNoti
);

//Delete Method
router.delete('/:idNoti', passport.authenticate('jwt', { session: false }), notificationController.deleteNoti);

module.exports = router;
