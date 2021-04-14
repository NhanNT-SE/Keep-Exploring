const express = require("express");
const multer = require("multer");
require("../Models/Blog");
require("../Models/Post");
const userController = require("../Controllers/UserController");

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

router.get("/:idUser", userController.getAnotherProfile);

//PUT Method
router.patch("/", upload.single("image_user"), userController.updateProfile);
router.patch("/changePass", userController.changePass);

module.exports = router;

/**
 * @swagger
 * tags:
 *   name: User
 *   description: User management and retrieval
 */

/**
 * @swagger
 * path:
 *  /user:
 *    get:
 *      summary: Get all users
 *      description: Only admins can retrieve all users.
 *      tags: [Users]
 *      responses:
 *        "200":
 *          description: OK
 */
