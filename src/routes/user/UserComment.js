import express from "express";
import multer from "multer";
import * as controller from "../../controllers/user/UserComment.js";
const router = express.Router();
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
router.post(
  "/post",
  uploadPost.single("image_comment"),
  controller.createCommentPost
);
router.post(
  "/blog",
  uploadBlog.single("image_comment"),
  controller.createCommentBlog
);

router.patch(
  "/editBlog",
  uploadBlog.single("image_comment"),
  controller.editCommentBlog
);
router.patch(
  "/editPost",
  uploadBlog.single("image_comment"),
  controller.editCommentPost
);
router.delete("/delete/:idComment", controller.deleteCommentByID);

export default router;
