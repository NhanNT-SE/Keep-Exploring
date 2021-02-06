const Blog = require('../Models/Blog');

const createBlog = async (req, res) => {
	try {
		const blog = new Blog({
			...req.body,
		});
		await blog.save();
		return res.status(200).send(blog);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};



module.exports = {
	createBlog,
};
s