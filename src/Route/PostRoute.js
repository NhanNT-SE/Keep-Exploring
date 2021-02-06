const express = require('express');
const multer = require('multer');
const postController = require('../Controllers/PostController');
const passport = require('passport');
require('../middleware/passport');

const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storage = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/public/images/post');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const upload = multer({ storage: storage });

//Get Method
router.get('/postList', postController.getPostList);
router.get('/:idPost', postController.getPost);

//Post Method
router.post('/add', passport.authenticate('jwt'), upload.array('image_post', 10), postController.createPost);
router.post('/delete/:postID', postController.deletePost);

module.exports = router;

/**
 * @swagger
 * tags:
 *   name: Post
 *   description: Post management
 */

// /**
//  * @swagger
//  * path:
//  *  /post/postList:
//  *    get:
//  *      summary: Get all post
//  *      description: Logged in users can fetch only their own user information. Only admins can fetch other users.
//  *      tags: [Post] 
//  *      responses:
//  *        "200":
//  *          description: OK
//  *          content:
//  *            application/json:
//  *              schema:
//  *                 $ref: '#/components/schemas/Post'
//  */