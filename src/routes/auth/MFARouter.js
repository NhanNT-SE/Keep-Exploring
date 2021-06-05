import express from "express";
import * as controller from "../../controllers/auth/MFAController.js";

const router = express.Router();

router.post("/qr-code", controller.getQRCode);
router.post("/verify", controller.verifyOTP);
router.patch("/enable", controller.enableMFA);
router.patch("/disable", controller.disableMFA);

export default router;
