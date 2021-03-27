const express = require("express");
const notificationController = require("../Controllers/NotificationController");

const router = express.Router();
//GetMethod
router.get("/", notificationController.getAllByUser);
//Patch Method
router.patch("/status", notificationController.changeSeenStatusNotify);
router.patch("/status/new", notificationController.changeNewStatusNotify);
//Delete Method
router.delete("/:idNoti", notificationController.deleteNotify);

module.exports = router;
