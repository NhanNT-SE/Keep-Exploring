import express from "express";
import * as controller from "../../controllers/user/UserProfile.js";
import { storage } from "../../helpers/Storage.js";
const router = express.Router();
router
  .route("")
  .get(controller.getMyProfile)
  .patch(storage.single("avatar"), controller.updateProfile);
router.get("/posts", controller.getMyPostList);
router.patch("/changePass", controller.changePass);
router.get("/:idUser", controller.getAnotherProfile);

export default router;
