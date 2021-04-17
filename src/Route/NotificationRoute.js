const express = require("express");
const notificationController = require("../Controllers/NotificationController");

const router = express.Router();
router.get("/", notificationController.getAllByUser);
router.patch("/status", notificationController.changeSeenStatusNotify);
router.patch("/status/:idNotify", notificationController.changeNewStatusNotify);
router.delete("/delete/:idNotify", notificationController.deleteNotifyById);

module.exports = router;
