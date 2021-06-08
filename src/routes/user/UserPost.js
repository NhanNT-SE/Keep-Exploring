import express from "express";
import * as controller from "../../controllers/user/UserPost.js";
import { storage } from "../../helpers/Storage.js";
const router = express.Router();
router.get("/:idUser", controller.getPostListByUser);
router.post("/album", storage.array("images"), controller.createAlbum);
router.post("/story", storage.array("images"), controller.createStory);
router.patch("/like", controller.likePost);
router
  .route("/:idPost")
  .patch(storage.single("image_post"), controller.updatePost)
  .delete(controller.deletePost);

export default router;
