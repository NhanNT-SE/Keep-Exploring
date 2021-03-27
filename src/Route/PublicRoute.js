const express = require("express");
const publicController = require("../Controllers/PublicController");
const router = express.Router();

router.get("/blog", publicController.getBlogList);
router.get("/post", publicController.getPostList);
router.get("/post/province", publicController.getPostByAddress);
router.get("/post/:idPost", publicController.getPostById);
router.get("/post/comments/:idPost", publicController.getPostComment);
router.get("/blog/:idBlog", publicController.getBlogByID);
router.get("/blog/comments/:idBlog", publicController.getBlogComment);
router.post("/post/like", publicController.getLikeListPost);

module.exports = router;
