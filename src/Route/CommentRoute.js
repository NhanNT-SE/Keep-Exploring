const express = require('express');
<<<<<<< HEAD
const commentController = require('../Controllers/CommentController');

const router = express.Router();

=======
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

>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331
// GET Method
router.get('/getByPost', commentController.getCommentbyPost);

//POST Method
<<<<<<< HEAD
router.post('/add', commentController.createComment);
=======
router.post(
	'/post',
	passport.authenticate('jwt'),
	upload.array('image_comment', 10),
	commentController.createCommentPost
);
>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331
router.post('/deleteByPost', commentController.deleteCommentbyPost);
router.post('/deletebyId/:idComment', commentController.deleteCommentbyID);

module.exports = router;
