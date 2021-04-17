const express = require("express");
const multer = require("multer");
const blogController = require("../Controllers/BlogController");
const router = express.Router();

const storage = multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, "src/public/images/blog/");
  },
  filename: function (req, file, callback) {
    callback(null, Date.now() + "-" + file.originalname);
  },
});

const upload = multer({ storage: storage });

router.get("/:idUser", blogController.getBlogListByUser);
//Post Method
router.post("/add", upload.single("image_blog"), blogController.createBlog);

//Patch method
router.patch("/like", blogController.likeBlog);
router.patch(
  "/update/:idBlog",
  upload.single("image_blog"),
  blogController.updateBlog
);

//Delete Method
router.delete("/delete/:idBlog", blogController.deleteBlog);

module.exports = router;
