import express from "express";
import multer from "multer";
import * as controller from "../../controllers/user/UserPost.js";
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

router.get("/:idUser", controller.getPostListByUser);
router.post("/add", upload.single("image_blog"), controller.createPost);
router.patch("/like", controller.likePost);
router
  .route("/:idPost")
  .patch(upload.single("image_post"), controller.updatePost)
  .delete(controller.deletePost);

export default router;
