import express from "express";
import multer from "multer";
import * as controller from "../../controllers/user/UserBlog.js";
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

router.get("/:idUser", controller.getBlogListByUser);
router.post("/add", upload.single("image_blog"), controller.createBlog);
router.patch("/like", controller.likeBlog);
router
  .route("/:idBlog")
  .patch(upload.single("image_blog"), controller.updateBlog)
  .delete(controller.deleteBlog);

export default router;
