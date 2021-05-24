import express from "express";
import * as controller from "../../controllers/admin/AdminPost.js";
const router = express.Router();
router.get("/pots", controller.getAllPost);
router
  .route("/:idPost")
  .patch(controller.updateStatus)
  .delete(controller.deletePost);

export default router;