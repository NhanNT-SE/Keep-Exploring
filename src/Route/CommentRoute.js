const express = require("express");
const multer = require("multer");
const passport = require("passport");
const commentController = require("../Controllers/CommentController");
require("../middleware/passport");

const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storagePost = multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, "src/public/images/comment/post");
  },
  filename: function (req, file, callback) {
    callback(null, Date.now() + "-" + file.originalname);
  },
});

const storageBlog = multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, "src/public/images/comment/blog");
  },
  filename: function (req, file, callback) {
    callback(null, Date.now() + "-" + file.originalname);
  },
});

const uploadPost = multer({ storage: storagePost });
const uploadBlog = multer({ storage: storageBlog });

// GET Method
router.get('/getByPost/:idPost', commentController.getCommentbyPost);
router.get('/getByBlog/:idBlog', commentController.getCommentbyPost);

//POST Method
router.post(
	'/post',
	passport.authenticate('jwt'),
	uploadPost.single('image_comment'),
	commentController.createCommentPost
);
router.post(
	'/blog',
	passport.authenticate('jwt'),
	uploadBlog.single('image_comment'),
	commentController.createCommentBlog
);

//PATCH Method
router.patch('/editBlog', passport.authenticate('jwt'), uploadBlog.single('image_comment'), commentController.editCommentBlog);
router.patch('/editPost', passport.authenticate('jwt'), uploadBlog.single('image_comment'), commentController.editCommentPost);

//DELETE Method
router.delete('/deleteByPost/:idPost', commentController.deleteCommentbyPost);
router.delete('/deleteByBlog/:idBlog', commentController.deleteCommentbyBlog);
router.delete('/deletebyId/:idComment', commentController.deleteCommentbyID);

module.exports = router;
