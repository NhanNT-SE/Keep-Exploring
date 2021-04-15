const express = require("express");
const notificationController = require("../Controllers/NotificationController");

const router = express.Router();
//GetMethod
router.get("/", notificationController.getAllByUser);
//Patch Method
router.patch("/status", notificationController.changeSeenStatusNotify);
router.patch("/status/update", notificationController.updateStatusNotify);
//Delete Method
router.delete("/delete", notificationController.deleteAllNotify);
router.delete("/delete/:idNotify", notificationController.deleteNotifyById);

module.exports = router;
