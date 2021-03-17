const Blog = require('../Models/Blog');
const Comment = require('../Models/Comment');
const Notification = require('../Models/Notification');
const Post = require('../Models/Post');
const { createNotification } = require('./NotificationController');
const fs = require('fs');

const createCommentPost = async (req, res, next) => {
	try {
		//Lay thong tin tu phia client
		const { content, idPost } = req.body;
		const file = req.file;
		const user = req.user;

		//Kiem tra bai post con ton tai hay khong
		const postFound = await Post.findById(idPost);

		if (postFound) {
			//Kiem tra comment co hinh anh hay khong
			let img = '';
			//Neu co thi luu vao thuoc tinh imgs
			if (file) {
				img = file.filename;
			}

			//Tao object comment voi cac thuoc tinh tu client
			const comment = new Comment({
				idPost,
				idUser: user._id,
				content,
				img,
			});

			//Luu comment vao database de lay id
			await comment.save();

			//Push idComment vao bai Post
			postFound.comment.push(comment._id);
			await postFound.save();

			//Tao notify khi co nguoi comment bai viet
			const notify = new Notification({
				idUser: postFound.owner.toString(),
				idPost: idPost,
				status: 'new',
				content: 'comment',
			});
			await createNotification(notify);

			//Thanh cong tra ve status code 200

			return res.send({ data: { comment }, status: 200, message: 'Tạo bình luận thành công' });
		}

		//Neu bai viet khong ton tai thi tra ve status code 201
		return handleCustomError(201, 'Bài viết không tồn tại');
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const createCommentBlog = async (req, res, next) => {
	try {
		//Lay thong tin tu phia client
		const { content, idBlog } = req.body;
		const file = req.file;
		const user = req.user;

		//Kiem tra bai post con ton tai hay khong
		const blogFound = await Blog.findById(idBlog);

		if (blogFound) {
			//Kiem tra comment co hinh anh hay khong
			let img;

			//Neu co thi luu vao thuoc tinh imgs
			if (file) {
				img = file.filename;
			}

			//Tao object comment voi cac thuoc tinh tu client
			const comment = new Comment({
				idBlog,
				idUser: user._id,
				content,
				img,
			});

			//Luu comment vao database de lay id
			await comment.save();

			//Push idComment vao bai Post
			blogFound.comment.push(comment._id);
			await blogFound.save();

			//Tao notify khi co nguoi comment bai viet
			const notify = new Notification({
				idUser: blogFound.owner.toString(),
				idBlog,
				status: 'new',
				content: 'comment',
			});
			await createNotification(notify);

			//Thanh cong tra ve status code 200
			return res.send({ data: { comment }, status: 200, message: 'Tạo bình luận thành công' });
		}

		//Neu bai viet khong ton tai thi tra ve status code 201
		return handleCustomError(201, 'Bài viết không tồn tại');
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const deleteCommentbyID = async (req, res, next) => {
	try {
		const user = req.user;
		const { idComment } = req.params;
		const commentFound = await Comment.findById(idComment);

		if (user.role == 'admin' || user._id == commentFound.idUser) {
			if (commentFound.idPost) {
				if (commentFound.img) {
					fs.unlinkSync('src/public/images/comment/post/' + commentFound.img);
				}

				await Post.findByIdAndUpdate(commentFound.idPost, { $pull: { comment: idComment } });
			}

			if (commentFound.idBlog) {
				if (commentFound.img) {
					fs.unlinkSync('src/public/images/comment/blog/' + commentFound.img);
				}
				await Blog.findByIdAndUpdate(commentFound.idBlog, { $pull: { comment: idComment } });
			}

			await Comment.findByIdAndDelete(idComment);
			return res.send({ data: null, status: 200, message: 'Đã xóa bình luận thành công ' });
		}

		return handleCustomError(201, 'Bạn không phải admin/chủ comment này');
	} catch (error) {
		next(error);
	}
};

const deleteCommentbyPost = async (req, res, next) => {
	try {
		const user = req.user;
		const { idPost } = req.params;

		if (user.role !== 'admin') {
			handleCustomError(201, 'Bạn không có quyền xóa list comment này');
		}

		const commentList = await Comment.find({ idPost: idPost }, { img: 1, _id: 0 });
		if (commentList) {
			commentList.forEach((item) => {
				fs.unlinkSync('src/public/images/comment/post/' + item.img);
			});
		}

		await Comment.deleteMany({ idPost: idPost });
		await Post.findByIdAndUpdate(idPost, { $unset: { comment: '' } }, { multi: true });

		return res.send({ data: null, status: 200, message: 'Đã xóa tất cả comment của bài Post' });
	} catch (error) {
		next(error);
	}
};

const deleteCommentbyBlog = async (req, res, next) => {
	try {
		const user = req.user;
		const { idBlog } = req.params;

		if (user.role !== 'admin') {
			handleCustomError(201, 'Bạn không có quyền xóa list comment này');
		}

		const commentList = await Comment.find({ idBlog: idBlog }, { img: 1, _id: 0 });
		if (commentList) {
			commentList.forEach((item) => {
				fs.unlinkSync('src/public/images/comment/blog/' + item.img);
			});
		}

		await Comment.deleteMany({ idBlog: idBlog });
		await Blog.findByIdAndUpdate(idBlog, { $unset: { comment: 1 } }, { multi: true });

		return res.send({ data: null, status: 200, message: 'Đã xóa tất cả comment của bài Blog' });
	} catch (error) {
		next(error);
	}
};

const getCommentbyPost = async (req, res, next) => {
	try {
		const { idPost } = req.params;

		const commentList = await Comment.find({ idPost: idPost }).populate('idPost').populate('idUser');
		if (!commentList) {
			const postFound = await Post.findById(idPost);
			if (postFound) {
				return handleCustomError(201, 'Bài viết chưa có comment');
			}
			return handleCustomError(202, 'Bài viết không tồn tại hoặc đã bị xóa');
		}

		return res.status(200).send(commentList);
	} catch (error) {
		next(error);
	}
};

const getCommentbyBlog = async (req, res, next) => {
	try {
		const { idBlog } = req.params;
		const commentList = await Comment.find({ idBlog: idBlog }).populate('idBlog').populate('idUser');
		if (!commentList) {
			const blogFound = await Blog.findById(idBlog);
			if (blogFound) {
				return handleCustomError(201, 'Bài viết chưa có comment');
			}
			return handleCustomError(202, 'Bài viết không tồn tại hoặc đã bị xóa');
		}
		return res.status(200).send(commentList);
	} catch (error) {
		next(error);
	}
};

const handleCustomError = (status, message) => {
	const err = new Error();
	err.status = status || 500;
	err.message = message;
	throw err;
};

module.exports = {
	createCommentPost,
	createCommentBlog,
	deleteCommentbyID,
	deleteCommentbyPost,
	deleteCommentbyBlog,
	getCommentbyPost,
	getCommentbyBlog,
};