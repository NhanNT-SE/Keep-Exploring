const express = require("express");
const multer = require("multer");
const commentController = require("../Controllers/CommentController");
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
//POST Method
router.post(
  "/post",
  uploadPost.single("image_comment"),
  commentController.createCommentPost
);
router.post(
  "/blog",
  uploadBlog.single("image_comment"),
  commentController.createCommentBlog
);

//PATCH Method
router.patch(
  "/editBlog",
  uploadBlog.single("image_comment"),
  commentController.editCommentBlog
);
router.patch(
  "/editPost",
  uploadBlog.single("image_comment"),
  commentController.editCommentPost
);
//DELETE Method
router.delete("/delete/:idComment", commentController.deleteCommentByID);

module.exports = router;
