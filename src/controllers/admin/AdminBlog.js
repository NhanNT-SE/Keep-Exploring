import { Blog } from "../../models/Blog.js";
import { customError } from "../../helpers/CustomError.js";
import { customResponse } from "../../helpers/CustomResponse.js";
const getAllBlog = async (req, res, next) => {
  try {
    const blog_list = await Blog.find({})
      .populate("owner", ["displayName", "imgUser", "email"])
      .sort({ created_on: -1 });
    return res.send(customResponse(blog_list));
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
      return res.send(customResponse(blog, "Xoa bai viet thanh cong"));
    }
    customError("Bai viet khong ton tai");
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
      await blog.updateOne({ ...blogUpdate }, { returnOriginal: false });
      return res.send(customResponse(blog, "Cập nhật bài viết thành công"));
    }
    return customError("Bài viết không tồn tại");
  } catch (error) {
    console.log(error);
    next(error);
  }
};
export { getAllBlog, updateStatus, deleteBlog };
