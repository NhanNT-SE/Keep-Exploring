const Blog = require("../Models/Blog");
const Post = require("../Models/Post");
const User = require("../Models/User");

const statisticsPost = async (req, res, next) => {
  try {
    const totalPost = await Post.countDocuments({});
    const totalBlog = await Blog.countDocuments({});
    const pendingPost = await Post.countDocuments({ status: "pending" });
    const pendingBlog = await Blog.countDocuments({ status: "pending" });
    const need_updatePost = await Post.countDocuments({
      status: "need_update",
    });
    const need_updateBlog = await Blog.countDocuments({
      status: "need_update",
    });
    const donePost = await Post.countDocuments({ status: "done" });
    const doneBlog = await Blog.countDocuments({ status: "done" });
    const admin = await User.countDocuments({ role: "admin" });
    const user = await User.countDocuments({ role: "user" });
    const data = {
      user: {
        title: `Users(${user + admin})`,
        data: [user, admin],
      },
      postBlog: {
        title: `Post-Blog(${totalPost + totalBlog})`,
        data: [totalPost, totalBlog],
      },
      post: {
        title: `Post(${totalPost})`,
        data: [donePost, pendingPost, need_updatePost],
      },
      blog: {
        title: `Blog(${totalBlog})`,
        data: [doneBlog, pendingBlog, need_updateBlog],
      },
    };
    return res
      .status(200)
      .send({ data, status: 200, message: "Lấy dữ liệu thành công" });
  } catch (error) {
    next(error);
  }
};
const handlerCustomError = (status, message) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  throw err;
};
module.exports = {
  statisticsPost,
};
