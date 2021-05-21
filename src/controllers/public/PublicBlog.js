import {customError} from "../../helpers/CustomError.js";
import Blog from "../../models/Blog.js";
import Comment from "../../models/Comment.js";
import "../../models/User.js";
import "../../models/Comment.js";
import "../../models/Blog_Detail.js";

const getBlogByID = async (req, res, next) => {
  try {
    const { idBlog } = req.params;
    const blogFound = await Blog.findById(idBlog)
      .populate("blog_detail")
      .populate("owner", ["displayName", "imgUser", "email", "post", "blog"]);
    if (blogFound) {
      return res.send({ data: blogFound, status: 200, message: "" });
    }

    return customError(201, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};
const getBlogList = async (req, res, next) => {
  try {
    const blogList = await Blog.find({ status: "done" })
      .populate("blog_detail")
      .populate("owner", ["displayName", "imgUser", "email", "blog", "post"])
      .sort({ created_on: -1 });
    return res.status(200).send({
      data: blogList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};

const getBlogByQuery = async (req, res, next) => {
  try {
    const query = req.query.query || "";
    const blogList = await Blog.find({ status: "done" })
      .populate("blog_detail")
      .populate("owner", ["displayName", "imgUser", "email", "post", "blog"])
      .sort({ created_on: -1 });

    let resultList = blogList.filter((e) =>
      e.owner.displayName.toLowerCase().includes(query.toLowerCase())
    );
    if (resultList.length === 0) {
      resultList = blogList.filter((e) =>
        e.title.toLowerCase().includes(query.toLowerCase())
      );
    }
    return res.send({
      data: resultList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};

const getBlogComment = async (req, res, next) => {
  try {
    const { idBlog } = req.params;
    const blogFound = await Blog.findById(idBlog);
    if (blogFound) {
      const commentList = await Comment.find({
        idBlog: idBlog,
      }).populate("idUser", [
        "email",
        "displayName",
        "imgUser",
        "post",
        "blog",
      ]);
      return res.send({
        data: commentList.reverse(),
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }
    return customError(202, "Bài viết không tồn tại hoặc đã bị xóa");
  } catch (error) {
    next(error);
  }
};

const getLikeListBlog = async (req, res, next) => {
  try {
    const { idBlog } = req.body;
    const blogFound = await Blog.findById(idBlog).populate("like_list", [
      "email",
      "displayName",
      "imgUser",
      "post",
      "blog",
    ]);
    if (!blogFound) {
      return customError(201, "Bài viết không tồn tại hoặc đã bị xóa");
    }

    const likeList = blogFound.like_list;
    return res.send({
      data: likeList,
      status: 200,
      message: "Danh sách những người like bài viết",
    });
  } catch (error) {
    next(error);
  }
};

export {
  getBlogComment,
  getBlogList,
  getBlogByID,
  getBlogByQuery,
  getLikeListBlog,
};
