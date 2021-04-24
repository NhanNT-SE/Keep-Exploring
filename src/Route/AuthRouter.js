const express = require("express");
const multer = require("multer");
const authController = require("../Controllers/AuthController");
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

router.post("/sign-in", authController.signIn);
router.post("/sign-out", authController.signOut);
router.post("/sign-up", upload.single("image_user"), authController.signUp);
router.post("/refresh-token", authController.refreshToken);
router.patch("/forget-pass", authController.forgetPassword);
router.patch("/new-pass", authController.getNewPassword);
module.exports = router;
