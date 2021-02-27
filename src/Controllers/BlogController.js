const Blog = require('../Models/Blog');
const Blog_Detail = require('../Models/Blog_Detail');

const fs = require('fs');

const createBlog = async (req, res) => {
	try {
		const { title, content_list } = req.body;

		const user = req.user;
		const files = req.files;
		const content_list_json = JSON.parse(content_list);
		const blog = new Blog({
			title,
			owner: user._id,
		});
		await blog.save();

		const _id = blog._id;
		const len = files.length;
		const detail_list = [];

		// Gán img+content vào mảng detail_list
		for (let i = 0; i < len; i++) {
			let detail = {};
			detail.img = files[i].filename;
			detail.content = content_list_json[i];
			detail_list.push(detail);
		}

		const blog_detail = new Blog_Detail({
			_id,
			detail_list,
		});
		await blog_detail.save();
		return res.status(200).send(blog_detail);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const deleteBlog = async (req, res) => {
	try {
		//Lay id bai viet
		const { idBlog } = req.params;

		//Kiem tra xem bai viet co ton tai khong
		const blogFound = await Blog.findById(idBlog);
		const detailFound = await Blog_Detail.findById(idBlog);
		const len = detailFound.detail_list.length;

		if (blogFound && detailFound) {
			for (let i = 0; i < len; i++) {
				fs.unlinkSync('src/public/images/blog/' + detailFound.detail_list[i].img);
			}
			await Blog_Detail.findByIdAndDelete(idBlog);
			await Blog.findByIdAndDelete(idBlog);
			return res.status(200).send('Blog deleted');
		}
		return res.status(201).send('Bai Blog khong ton tai');
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const updateBlog = async (req, res) => {
	try {
		//Lay du lieu gui len tu phia client
		const { idBlog, title, content_list } = req.body;
		const files = req.files;

		//Kiem tra bai viet co ton tai
		const blogFound = Blog.findById(idBlog);
		const detailFound = Blog_Detail.findById(idBlog);
		if (blogFound && detailFound) {
			//Kiem tra bai viet co thay doi hinh anh khong
		}
		return res.status(201).send('Bai viet khong ton tai');
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const likeBlog = async (req, res) => {
	try {
		//Lay id bai viet va id nguoi dung tu req
		const { idBlog } = req.body;
		const user = req.user;

		//Kiem tra bai viet co ton tai hay khong
		const blogFound = await Blog.findById(idBlog);
		if (blogFound) {
			//Kiem tra nguoi dung da like bai viet hay chua
			var i = 0;
			var like_list = blogFound.like_list;
			const len = like_list.length;

			for (i; i < len; i++) {
				//Neu nguoi dung da like bai viet thi doi thanh dislike- remove idUser khoi like_list va return status code 200
				if ((user._id = like_list[i])) {
					await blogFound.like_list.splice(i, 1);
					await blogFound.save();
					return res.status(200).send('Da bo like bai viet');
				}
			}
			//Con neu nguoi dung chua like bai viet thi push idUser vao like_list va return status code 201
			await blogFound.like_list.push(user._id);
			await blogFound.save();
			return res.status(201).send('Da like bai viet');
		}

		//Neu bai viet khong ton tai thi tra ve res code 202
		return res.status(202).send('Bai viet khong ton tai');
	} catch (error) {
		res.status(500).send(error.message);
	}
};

const updateStatus = async (req, res) => {
	try {
		const { idBlog, status } = req.body;
		//Kiem tra role cua nguoi dung, chi co admin moi duoc update status
		const { role } = req.user;

		if (role === 'admin') {
			//Kiem tra co ton tai bai viet
			const blogFound = await Blog.findById(idBlog);

			//Neu ton tai thi admin cap nhat status,
			if (blogFound) {
				blogFound.status = status;
				await Blog.findByIdAndUpdate(idBlog, blogFound);
				return res.status(200).send('Cap nhat trang thai thanh cong');
			}

			//Neu khong ton tai blog se tra ve client status code la 201
			return res.status(201).send('Bai viet khong ton tai');
		}

		//Khi role nguoi dung khong phai admin thi tra ve status code la 202
		return res.status(202).send('Ban khong co quyen cap nhat status blog');
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const getAll = async (req, res) => {
	try {
		//Kiem tra role nguoi dung
		const user = req.user;
		const role = user.role;

		//Neu la admin thi co quyen xem tat ca bai viet
		if (role == 'admin') {
			const blogList = await Blog.find({});
			return res.status(200).send(blogList);
		}

		//Khong phai admin thi chi xem nhung bai viet co status la done
		const blogList_done = await Blog.find({ status: 'done' });
		return res.status(200).send(blogList_done);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

module.exports = {
	createBlog,
	deleteBlog,
	likeBlog,
	updateStatus,
	getAll,
};
