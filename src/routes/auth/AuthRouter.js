import express from "express";
import * as controller from "../../controllers/auth/AuthController.js";
import { storage } from "../../helpers/Storage.js";

const router = express.Router();

router.post("/sign-in", controller.signIn);
router.post("/sign-in/verify", controller.verifyOTPSignIn);
router.post("/sign-out", controller.signOut);
router.post("/sign-up", storage.single("avatar"), controller.signUp);
router.post("/refresh-token", controller.refreshToken);
router.patch("/forget-pass", controller.forgetPassword);
router.patch("/new-pass", controller.getNewPassword);
export default router;
