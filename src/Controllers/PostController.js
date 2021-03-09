const Post = require('../Models/Post');
const Address = require('../Models/Address');
const fs = require('fs');

const createPost = async (req, res) => {
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

		return res.status(200).send(post);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const deletePost = async (req, res, next) => {
	try {
		let err = new Error();
		err.status = 201;
		err.message = 'Bai Post khong ton tai';
		const { postID } = req.params;
		console.log(postID);

		const postFound = await Post.findById(postID);

		if (postFound) {
			const len = postFound.imgs.length;

			for (let i = 0; i < len; i++) {
				fs.unlinkSync('src/public/images/post/' + postFound.imgs[i]);
			}
			await Post.findByIdAndDelete(postID);
			await Address.findOneAndDelete({ idPost: postID });
			return res.status(200).send('Deleted post');
		}

		// return res.status(201).send('Bai Post khong ton tai');
		return next(err);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const getPost = async (req, res) => {
	try {
		const { idPost } = req.params;
		const post = await Post.findById(idPost).populate('owner');
		return res.status(200).send(post);
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const getPostList = async (req, res) => {
	try {
		//Kiem tra role nguoi dung
		const user = req.user;
		const role = user.role;

		//Neu la admin thi co quyen xem tat ca bai viet
		if (role == 'admin') {
			// const postList = await Post.find({});
			// return res.status(200).send(postList);

			let { status } = req.query;
			var post_list = [];

			//Neu khong truyen status thi tra ve all post
			if (status == '') {
				post_list = await Post.find({});
				return res.status(200).send(post_list);
			}

			//con neu co truyen query thi loc post list theo query
			post_list = await Post.find({ status: status });
			return res.status(200).send(post_list);
		}

		//Khong phai admin thi chi xem nhung bai viet co status la done
		const postList_done = await Post.find({ status: 'done' });
		return res.status(200).send(postList_done);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const likePost = async (req, res) => {
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
					return res.status(200).send('Da bo like bai viet');
				}
			}
			//Con neu nguoi dung chua like bai viet thi push idUser vao like_list
			await postFound.like_list.push(user._id);
			await postFound.save();
			return res.status(201).send('Da like bai viet');
		}

		return res.status(202).send('Bai viet khong ton tai');
	} catch (error) {
		res.status(500).send(error.message);
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
						return res.status(202).send('So luong hinh anh da vuot qua gioi han la 20');
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
				return res.status(200).send(newPost);
			}

			//Neu khong phai owner bai viet thi tra ve status code 201
			return res.status(201).send('Ban khong phai owner bai viet nay');
		}
	} catch (error) {
		res.status(500).send(error.message);
	}
};

const updateStatus = async (req, res) => {
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
				return res.status(200).send('Cap nhat trang thai thanh cong');
			}

			//Neu khong ton tai bai post se tra ve client status code la 201
			return res.status(201).send('Bai viet khong ton tai');
		}

		//Khi role nguoi dung khong phai admin thi tra ve status code la 202
		return res.status(202).send('Ban khong co quyen cap nhat status blog');
	} catch (error) {
		return res.status(500).send(error.message);
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
