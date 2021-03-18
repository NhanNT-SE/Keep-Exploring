const express = require('express');
const multer = require('multer');
const passport = require('passport');
const commentController = require('../Controllers/CommentController');
require('../middleware/passport');

const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storagePost = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/public/images/comment/post');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const storageBlog = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/public/images/comment/blog');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const uploadPost = multer({ storage: storagePost });
const uploadBlog = multer({ storage: storageBlog });

// GET Method
<<<<<<< HEAD
router.get('/getByPost', commentController.getCommentbyPost);
router.get('/getByBlog', commentController.getCommentbyPost);
=======
router.get('/getByPost', commentController.getCommentByPost);
>>>>>>> back-end

//POST Method
router.post(
	'/post',
	passport.authenticate('jwt'),
	uploadPost.single('image_comment'),
	commentController.createCommentPost
);
<<<<<<< HEAD
router.post(
	'/blog',
	passport.authenticate('jwt'),
	uploadBlog.single('image_comment'),
	commentController.createCommentBlog
);

//DELETE Method
router.delete('/deleteByPost/:idPost', commentController.deleteCommentbyPost);
router.delete('/deleteByBlog/:idBlog', commentController.deleteCommentbyBlog);
router.delete('/deletebyId/:idComment', commentController.deleteCommentbyID);
=======
router.post('/deleteByPost', commentController.deleteCommentByPost);
router.post('/deletebyId/:idComment', commentController.deleteCommentByID);
>>>>>>> back-end

module.exports = router;
