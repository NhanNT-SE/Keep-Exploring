import express from "express";
import * as controller from "../../controllers/auth/MFAController.js";

const router = express.Router();

router.post("/enable", controller.enableMFA);
router.post("/disable", controller.disableMFA);
router.post("/verify", controller.verifyOTP);

export default router;
