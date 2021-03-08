const multer = require('multer');

//The disk storage engine gives you full control on storing files to disk.
<<<<<<< HEAD
const storage = multer.diskStorage({
=======
const storagePost = multer.diskStorage({
>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331
	destination: function (req, file, callback) {
		callback(null, 'src/images/post');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

<<<<<<< HEAD

=======
const uploadPost = multer({ storage: storagePost });

module.exports = {
	uploadPost,
};
>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331
