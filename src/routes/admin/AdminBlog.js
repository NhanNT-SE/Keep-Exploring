import express from "express";
import * as controller from "../../controllers/admin/AdminBlog.js";
const router = express.Router();
router.route("").get(controller.getAllBlog).patch(controller.updateStatus);
router.delete("/:idBlog", controller.deleteBlog);

export default router;
