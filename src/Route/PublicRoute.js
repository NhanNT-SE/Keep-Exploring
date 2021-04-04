const express = require("express");
const publicController = require("../Controllers/PublicController");
const router = express.Router();

router.get("/blog", publicController.getBlogList);
router.get("/post", publicController.getPostList);
router.get("/post/address", publicController.getPostByAddress);
router.get("/post/:idPost", publicController.getPostById);
router.get("/blog/:idBlog", publicController.getBlogByID);
router.get("/post/comments/:idPost", publicController.getPostComment);
router.get("/blog/comments/:idBlog", publicController.getBlogComment);
router.get("/address", publicController.getProvinceList);
router.post("/address", publicController.getAddress);
router.post("/blog/like", publicController.getLikeListBlog);
router.post("/post/like", publicController.getLikeListPost);

module.exports = router;
