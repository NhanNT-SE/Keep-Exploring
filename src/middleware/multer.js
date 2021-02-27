const multer = require('multer');

//The disk storage engine gives you full control on storing files to disk.
const storagePost = multer.diskStorage({
	destination: function (req, file, callback) {
		callback(null, 'src/images/post');
	},
	filename: function (req, file, callback) {
		callback(null, Date.now() + '-' + file.originalname);
	},
});

const uploadPost = multer({ storage: storagePost });

module.exports = {
	uploadPost,
};
