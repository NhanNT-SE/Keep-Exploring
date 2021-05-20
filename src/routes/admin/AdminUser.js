import express from "express";
import * as controller from "../../controllers/admin/AdminUser.js";
const router = express.Router();
router.get("/users", controller.getAllUser);
router.post("/send-notify", controller.sendNotify);
router.delete("/:idUser", controller.deleteUser);

export default router;
