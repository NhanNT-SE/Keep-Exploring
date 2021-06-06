import express from "express";
import multer from "multer";
import * as controller from "../../controllers/auth/AuthController.js";
const storage = multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, "src/public/images/user");
  },
  filename: function (req, file, callback) {
    callback(null, Date.now() + "-" + file.originalname);
  },
});

const upload = multer({ storage: storage });
const router = express.Router();

router.post("/sign-in", controller.signIn);
router.post("/sign-in/verify", controller.verifyOTPSignIn);
router.post("/sign-out", controller.signOut);
router.post("/sign-up", upload.single("image_user"), controller.signUp);
router.post("/refresh-token", controller.refreshToken);
router.patch("/forget-pass", controller.forgetPassword);
router.patch("/new-pass", controller.getNewPassword);
export default router;
