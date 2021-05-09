const express = require("express");
const blogController = require("../../Controllers/admin/AdminBlog");
const router = express.Router();
router.get("/blogs", blogController.getAllBlog);
router.delete("/:idBlog", blogController.deleteBlog);


module.exports = router;
