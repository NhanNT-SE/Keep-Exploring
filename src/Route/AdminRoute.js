const express = require("express");

const adminController = require("../Controllers/AdminController");
const router = express.Router();

router.get("/users", adminController.getAllUser);
router.get("/blogs", adminController.getAllBlog);
router.get("/posts", adminController.getAllPost);
router.get("/statistics", adminController.getStatistics);
router.patch("/update-status", adminController.updateStatus);
router.post("/send-notify", adminController.sendNotify);
router.delete("/user/:idUser", adminController.deleteUser);
router.delete("/post/comment/:idPost", adminController.deleteAllCommentPost);
router.delete("/blog/comment/:idBlog", adminController.deleteAllCommentBlog);

module.exports = router;
