import Blog from "../../models/Blog.js";
import {customError} from "../../helpers/CustomError.js";
const getAllBlog = async (req, res, next) => {
  try {
    const blog_list = await Blog.find({})
      .populate("owner", ["displayName", "imgUser", "email"])
      .sort({ created_on: -1 });
    return res.send({
      data: blog_list,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};

const deleteBlog = async (req, res, next) => {
  try {
    const { idBlog } = req.params;
    const blog = await Blog.findById(idBlog);
    if (blog) {
      await Blog.findByIdAndDelete(idBlog);
      return res.status(200).send({
        data: blog,
        status: 200,
      });
    }
    customError(500, "Bai viet khong ton tai");
  } catch (error) {
    next(error);
  }
};
const updateStatus = async (req, res, next) => {
  try {
    const { idBlog, status } = req.body;
    const blog = await Blog.findById(idBlog);
    const { io } = req;
    if (blog) {
      const blogUpdate = {
        status,
        view_mode: "public",
      };
      await Blog.findByIdAndUpdate(idBlog, blogUpdate);
      return res.send({
        data: blog,
        status: 200,
        message: "Cập nhật bài viết thành công",
      });
    }
    return customError(201, "Bài viết không tồn tại");
  } catch (error) {
    console.log(error);
    next(error);
  }
};
export { getAllBlog, updateStatus, deleteBlog };
