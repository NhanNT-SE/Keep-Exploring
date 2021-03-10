const express = require('express');
const multer = require('multer');
const passport = require('passport');
const commentController = require('../Controllers/CommentController');
require('../middleware/passport');

const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storage = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/public/images/comment');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const upload = multer({ storage: storage });

// GET Method
router.get('/getByPost', commentController.getCommentByPost);

//POST Method
router.post(
	'/post',
	passport.authenticate('jwt'),
	upload.array('image_comment', 10),
	commentController.createCommentPost
);
router.post('/deleteByPost', commentController.deleteCommentByPost);
router.post('/deletebyId/:idComment', commentController.deleteCommentByID);

module.exports = router;
