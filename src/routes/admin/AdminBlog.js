import express from "express";
import * as controller from "../../controllers/admin/AdminBlog.js";
const router = express.Router();
router.get("/blogs", controller.getAllBlog);
router
  .route("/:idBlog")
  .patch(controller.updateStatus)
  .delete(controller.deleteBlog);

export default router;
