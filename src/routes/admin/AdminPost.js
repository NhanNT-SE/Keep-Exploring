import express from "express";
import * as controller from "../../controllers/admin/AdminPost.js";
const router = express.Router();
router.route("").get(controller.getAllPost).patch(controller.updateStatus);
router.delete("/:idPost", controller.deletePost);

export default router;
