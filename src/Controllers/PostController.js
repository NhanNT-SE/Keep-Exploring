const Post = require('../Models/Post');
const Address = require('../Models/Address');
const fs = require('fs');
const Notification = require('../Models/Notification');

const createPost = async (req, res, next) => {
	try {
		const user = req.user;
		var img_list = new Array();
		var i;

		//Luu string hinh anh vao database
		const files = req.files;
		const length = files.length;
		for (i = 0; i < length; ++i) {
			img_list.push(files[i].filename);
		}

		const post = new Post({
			...req.body,
			owner: user._id,
			imgs: img_list,
		});

		await post.save();

		//Tim address de push idPost vao
		const addressID = req.body.address;
		const addressPost = await Address.findById(addressID);
		addressPost.idPost = post._id;
		await addressPost.save();

		return res.status(200).send({ data: { post }, err: '', status: 200, msg: 'create Post successfully' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const deletePost = async (req, res, next) => {
	try {
		const { postID } = req.params;
		const user = req.user;

		const postFound = await Post.findById(postID);

		if (postFound) {
			if (user._id == postFound.owner.toString() || user.role == 'admin') {
				const len = postFound.imgs.length;

				for (let i = 0; i < len; i++) {
					fs.unlinkSync('src/public/images/post/' + postFound.imgs[i]);
				}
				await Post.findByIdAndDelete(postID);
				await Address.findOneAndDelete({ idPost: postID });

				return res.status(200).send({ data: null, err: '', status: 200, msg: 'Deleted post' });
			}

			console.log(user._id);
			console.log(postFound.owner);
			console.log(user.role);

			return next({ status: 202, message: 'Ban khong phai owner bai viet/ admin' });
		}

		next({ status: 201, message: 'Bai Post khong ton tai' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const getPost = async (req, res, next) => {
	try {
		const { idPost } = req.params;
		const post = await Post.findById(idPost).populate('owner');

		if (post) {
			return res.status(200).send({ data: { post }, err: '', status: 200, msg: 'get post by id' });
		}
		next({ status: 201, message: 'Bai viet khong ton tai' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const getPostList = async (req, res, next) => {
	try {
		//Kiem tra role nguoi dung
		const user = req.user;
		const role = user.role;

		//Neu la admin thi co quyen xem tat ca bai viet
		if (role == 'admin') {
			let { status } = req.query;
			var post_list = [];

			//Neu khong truyen status thi tra ve all post
			if (status == '' || status == 'all') {
				post_list = await Post.find({});
				return res.status(200).send({ data: { post_list }, err: '', status: 200, msg: 'list all post ' });
			}

			//con neu co truyen query thi loc post list theo query
			post_list = await Post.find({ status: status });
			return res.status(200).send({ data: { post_list }, err: '', status: 200, msg: 'list post by status' });
		}

		//Khong phai admin thi chi xem nhung bai viet co status la done
		const postList_done = await Post.find({ status: 'done' });
		return res.send({ data: { postList_done }, err: '', status: 200, msg: '' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const likePost = async (req, res, next) => {
	try {
		//Lay id bai viet va id nguoi dung tu req
		const { idPost } = req.body;
		const user = req.user;

		//Kiem tra bai viet co ton tai hay khong
		const postFound = await Post.findById(idPost);
		if (postFound) {
			//Kiem tra nguoi dung da like bai viet hay chua
			var i = 0;
			var like_list = postFound.like_list;
			const len = like_list.length;

			for (i; i < len; i++) {
				//Neu nguoi dung da like bai viet thi doi thanh dislike- remove idUser khoi like_list
				if ((user._id = like_list[i])) {
					await postFound.like_list.splice(i, 1);
					await postFound.save();
					return res.send({ data: null, err: '', status: 201, msg: 'Da bo like bai viet' });
				}
			}
			//Con neu nguoi dung chua like bai viet thi push idUser vao like_list
			await postFound.like_list.push(user._id);
			await postFound.save();

			//Tao notify khi co nguoi like bai viet
			//Kiem tra co loai thong bao nay cho nguoi dung chua

			// const notifyFoundList = await Notification.findOne({ idPost: postFound._id });
			// console.log(notifyFoundList);

			// const notify = new Notification({
			// 	idUser: postFound.owner,
			// 	idPost: postFound._id,
			// 	content: 'like',
			// });

			// await notify.save();

			return res.send({ data: null, err: '', status: 200, msg: 'Da like bai viet' });
		}

		next({ status: 202, message: 'Bai viet khong ton tai' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const updatePost = async (req, res, next) => {
	try {
		//Lay data tu phia client gui len
		const { idPost } = req.params;
		const { imgs_deleted } = req.body;
		const user = req.user;
		const files = req.files;

		//Kiem tra bai viet co ton tai hay khong
		const postFound = await Post.findById(idPost);

		if (postFound) {
			const img_list = postFound.imgs;

			//Kiem tra user dang nhap co phai owner cua bai viet hay khong
			if ((user._id = postFound.owner)) {
				//Kiem tra co xoa img nao hay khong neu co thi xoa img phia server theo filename
				var len_deleted = 0;

				if (imgs_deleted) {
					const imgs_deleted_json = JSON.parse(imgs_deleted);
					len_deleted = imgs_deleted_json.length;

					for (let i = 0; i < len_deleted; i++) {
						for (let j = 0; j < len_deleted; j++) {
							if ((postFound.imgs[j] = imgs_deleted_json[i])) {
								await postFound.imgs.splice(i, 1);
							}
						}
						fs.unlinkSync('src/public/images/post/' + imgs_deleted_json[i]);
					}
				}

				//Kiem tra user co upload them hinh moi khong
				if (files) {
					const len_files = files.length;

					//Neu so luong hinh upload + so luong hinh o bai viet cu vuot qua gioi han thi tra ve status code 202
					if (postFound.imgs.lenth - len_deleted + len_files > 20) {
						next({ status: 202, message: 'So luong hinh anh da vuot qua gioi han la 20' });
					}

					//Neu chua vuot gioi han thi thuc hien upload hinh anh len server
					for (i = 0; i < len_files; i++) {
						img_list.push(files[i].filename);
					}
				}

				//Tao object luu nhung update cua bai viet
				const newPost = {
					...req.body,
					imgs: img_list,
					status: 'pending',
				};
				await Post.findByIdAndUpdate(idPost, newPost);
				return res.send({ data: { newPost }, status: 200, message: 'Update  successfully' });
			}

			//Neu khong phai owner bai viet thi tra ve status code 201
			next({ status: 201, message: 'Ban khong phai owner bai viet nay' });
		}
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const updateStatus = async (req, res, next) => {
	try {
		const { idPost, status } = req.body;

		//Kiem tra role cua nguoi dung, chi co admin moi duoc update status
		const { role } = req.user;

		if (role === 'admin') {
			//Kiem tra co ton tai bai viet
			const postFound = await Post.findById(idPost);

			//Neu ton tai thi admin cap nhat status,
			if (postFound) {
				postFound.status = status;

				await Post.findByIdAndUpdate(idPost, postFound);
				return res.send({ data: null, status: 200, message: 'Update Status successfully' });
			}

			//Neu khong ton tai bai post se tra ve client status code la 201
			next({ status: 201, message: 'Bai viet khong ton tai' });
		}

		//Khi role nguoi dung khong phai admin thi tra ve status code la 202
		next({ status: 202, message: 'Ban khong co quyen cap nhat status Post' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};
module.exports = {
	createPost,

	deletePost,
	getPost,
	getPostList,
	likePost,
	updatePost,
	updateStatus,
};