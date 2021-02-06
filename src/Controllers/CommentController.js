const Comment = require('../Models/Comment');

const createComment = async (req, res) => {
	try {
		const comment = req.body;
		comment.idPost = '5ffe94e5e8c21e0c441e32bc';
		comment.idUser = '5fef51e6e8d9c11adc648a8f';
		await new Comment(comment).save();
		return res.status(200).send(comment);
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const deleteCommentbyID = async (req, res) => {
	try {
		const { idComment } = req.params;
		await Comment.findByIdAndDelete(idComment);
		return res.status(200).send('Deleted comment successfully');
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const deleteCommentbyPost = async (req, res) => {
	try {
		const idPost = '5ffe94e5e8c21e0c441e32bc';
		await Comment.deleteMany({ idPost: idPost });
		return res.status(200).send('Delete comment by Post successfully');
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const getCommentbyPost = async (req, res) => {
	try {
		const idPost = '5ffd2debfc4c8b1dd8791cc2';
		const commentList = await Comment.find({ idPost: idPost }).populate('idPost').populate("idUser");
		return res.status(200).send(commentList);
	} catch (error) {}
};

module.exports = {
	createComment,
	deleteCommentbyID,
	deleteCommentbyPost,
	getCommentbyPost,
};
