import express from "express";
import multer from "multer";

import * as controller from "../../controllers/user/UserProfile.js";

const router = express.Router();

const storage = multer.diskStorage({
  destination: function (req, file, callback) {
    callback(null, "src/public/images/user");
  },
  filename: function (req, file, callback) {
    callback(null, Date.now() + "-" + file.originalname);
  },
});

const upload = multer({ storage: storage });

router.get("/:idUser", controller.getAnotherProfile);

router.patch("/", upload.single("image_user"), controller.updateProfile);
router.patch("/changePass", controller.changePass);

export default router;
