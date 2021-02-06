const Post = require('../Models/Post');

const createPost = async (req, res) => {
	try {
		const userID = '5fedb705d22e540e38ac6d8f';
		const user = req.user;
		var img_list = new Array();
		var i;

		//Luu string hinh anh vao database
		const length = req.files.length;
		const files = req.files;
		for (i = 0; i < length; ++i) {
			img_list.push(files[i].filename);
		}

		const post = new Post({
			...req.body,
			owner: userID,
			imgs: img_list,
		});

		await post.save();
		return res.status(200).send(post);
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const deletePost = async (req, res) => {
	try {
		const { postID } = req.params;
		const postFound = await Post.findById(postID);
		if (postFound) {
			await Post.findByIdAndDelete(postID);
			return res.status(200).send('Deleted post');
		}
		return res.status(201).send('Bai Post khong ton tai');
	} catch (error) {
		return res.status(202).send(error.message);
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
		const postList = await Post.find({}).populate('owner');
		return res.status(200).json({ postList });
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

module.exports = {
	createPost,
	deletePost,
	getPost,
	getPostList,
};
