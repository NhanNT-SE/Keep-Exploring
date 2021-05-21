import express from "express";
import * as controller from "../../controllers/public/PublicBlog.js";
const router = express.Router();

router.get("", controller.getBlogList);
router.get("/filter", controller.getBlogByQuery);
router.get("/:idBlog", controller.getBlogByID);
router.get("/:idBlog/comments", controller.getBlogComment);
router.post("/likes", controller.getLikeListBlog);

export default router;
