const express = require("express");
const multer = require("multer");
const blogController = require("../Controllers/blogController");
const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storage = multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, "src/public/images/blog/");
    // const { userId } = req.body;
    // const dir = 'src/public/images/blog/';
    // fs.exists(dir, (exist) => {
    // 	if (!exist) {
    // 		return fs.mkdir(dir, (error) => cb(error, dir));
    // 	}
    // 	return callback(null, dir);
    // });
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
router.patch(
  "/update/:idBlog",
  upload.single("image_blog"),
  blogController.updateBlog
);

router.patch("/like", blogController.likeBlog);
//Delete Method
router.delete("/delete/:idBlog", blogController.deleteBlog);

module.exports = router;
