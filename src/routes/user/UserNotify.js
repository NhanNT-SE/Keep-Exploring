import express from "express";
import * as controller from "../../controllers/user/UserNotify.js";

const router = express.Router();
router.get("/", controller.getAllByUser);
router.patch("/status", controller.changeSeenStatusNotify);
router.patch("/status/:idNotify", controller.changeNewStatusNotify);
router.delete("/delete/:idNotify", controller.deleteNotifyById);

export default router;
