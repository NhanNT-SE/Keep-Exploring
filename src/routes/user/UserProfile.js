import express from "express";
import * as controller from "../../controllers/user/UserProfile.js";
import { storage } from "../../helpers/Storage.js";
const router = express.Router();
router
  .route("")
  .get(controller.getMyProfile)
  .patch(storage.single("avatar"), controller.updateProfile);
router.get("/:idUser", controller.getAnotherProfile);

router.patch("/changePass", controller.changePass);

export default router;
