import express from "express";
import * as controller from "../../controllers/admin/AdminPost.js";
const router = express.Router();
router.get("", controller.getAllPost);
router
  .route("/:postId")
  .get(controller.getPostById)
  .patch(controller.updateStatus)
  .delete(controller.deletePost);

export default router;
