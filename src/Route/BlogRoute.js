const express = require('express');
const multer = require('multer');
const passport = require('passport');
require('../middleware/passport');
const BlogController = require('../Controllers/BlogController');

const router = express.Router();

//The disk storage engine gives you full control on storing files to disk.
const storage = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/public/images/blog/');
		// const { userId } = req.body;
		// const dir = 'src/public/images/blog/';
		// fs.exists(dir, (exist) => {
		// 	if (!exist) {
		// 		return fs.mkdir(dir, (error) => cb(error, dir));
		// 	}
		// 	return callback(null, dir);
		// });
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const upload = multer({ storage: storage });

//Get Method
router.get('/', BlogController.getAll);
router.get('/admin', passport.authenticate('jwt', { session: false }), BlogController.getAllByAdmin);
router.get('/:idBlog', BlogController.getBlogbyID);

//Post Method
router.post(
	'/add',
	passport.authenticate('jwt', { session: false }),
	upload.array('image_blog', 10),
	BlogController.createBlog
);

//Patch method
router.patch('/status', passport.authenticate('jwt', { session: false }), BlogController.updateStatus);
router.patch('/like', passport.authenticate('jwt', { session: false }), BlogController.likeBlog);

//Delete Method
router.delete('/delete/:idBlog', passport.authenticate('jwt'), BlogController.deleteBlog);

module.exports = router;
