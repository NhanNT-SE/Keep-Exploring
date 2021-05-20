import express from "express";
import * as controller from "../../controllers/public/PublicPost.js";
const router = express.Router();

router.get("/posts", controller.getPostList);
router.get("/filter", controller.getPostByQuery);
router.get("/:idPost", controller.getPostById);
router.get("/:idPost/comments", controller.getPostComment);
router.post("/likes", controller.getLikeListPost);

export default router;
