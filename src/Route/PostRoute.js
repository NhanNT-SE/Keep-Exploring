const express = require('express');
const multer = require('multer');
const postController = require('../Controllers/PostController');
const passport = require('passport');
const Post = require('../Models/Post');
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

const limitImgs = async (req, res) => {
	try {
		const postFound = await Post.findById(req.params);
		const amount = postFound.imgs.length;
	} catch (error) {
		res.status(500).send(error.message);
	}
};

const upload = multer({ storage: storage });

//Get Method
router.get('/', passport.authenticate('jwt'), postController.getPostList);
router.get('/:idPost', postController.getPost);

//Post Method
router.post('/', passport.authenticate('jwt'), upload.array('image_post', 20), postController.createPost);

//Put Method
router.put('/like', passport.authenticate('jwt', { session: false }), postController.likePost);
router.put('/status', passport.authenticate('jwt', { session: false }), postController.updateStatus);
router.put(
	'/:idPost',
	passport.authenticate('jwt', { session: false }),
	upload.array('image_post', 20),
	postController.updatePost
);

//Delete Method
router.delete('/delete/:postID', postController.deletePost);

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
