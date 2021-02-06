const express = require('express');
const multer = require('multer');

const userController = require('../Controllers/UserController');
const passport = require('passport');
require('../middleware/passport');

const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storage = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/images/post');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const upload = multer({ storage: storage });

//GET Medthod
router.get('/', (req, res) => {
	res.status(200).send('thanh cong');
});

//POST Method
router.post('/signUp', upload.single('image_user'), userController.signUp);
router.post('/signIn', userController.signIn);
router.post('/checkAdmin', passport.authenticate('jwt', { session: false }), userController.checkRoleAdmin);

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