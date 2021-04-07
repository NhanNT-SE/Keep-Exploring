const handlerCustomError = require("../middleware/customError");
const Address = require("../Models/Address");
const Blog = require("../Models/Blog");
const Comment = require("../Models/Comment");
const Post = require("../Models/Post");
require("../Models/User");
require("../Models/Comment");
require("../Models/Blog_Detail");

const getAddress = async (req, res, next) => {
  try {
    let { district, province } = req.body;
    if (!province || province === "") {
      province = "tp.hcm";
    }
    const address = await Address.findOne({ province });
    if (address) {
      let wardList = [];
      const districtList = address.district.map((e) => e.name);
      const index = address.district.findIndex((e) => e.name === district);
      if (index >= 0 && district) {
        wardList = address.district[index].ward;
      }
      return res.status(200).send({
        data: {
          wardList,
          districtList,
        },
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }
    handlerCustomError(
      201,
      `Không tìm thấy địa điểm ${province} trong hệ thống`
    );
  } catch (error) {
    next(error);
  }
};
const getProvinceList = async (req, res, next) => {
  try {
    const address = await Address.find({});
    const provinceList = address.map((e) => e.province);
    return res.status(200).send({
      data: provinceList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
const getBlogByID = async (req, res, next) => {
  try {
    const { idBlog } = req.params;
    const blogFound = await Blog.findById(idBlog)
      .populate("blog_detail")
      .populate("owner", ["displayName", "imgUser", "email"]);
    if (blogFound) {
      return res.send({ data: blogFound, status: 200, message: "" });
    }

    return handlerCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const getPostById = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const post = await Post.findById(idPost).populate("owner", [
      "displayName",
      "imgUser",
      "email",
    ]);
    if (post) {
      return res
        .status(200)
        .send({ data: post, status: 200, message: "Lấy dữ liệu thành công" });
    }
    return handlerCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};
const getPostList = async (req, res, next) => {
  try {
    const { category } = req.query;
    let postList;
    if (category === "" || !category) {
      postList = await Post.find({ status: "done" }).populate("owner", [
        "displayName",
        "imgUser",
        "email",
      ]);
    } else {
      postList = await Post.find({ status: "done", category }).populate(
        "owner",
        ["displayName", "imgUser", "email"]
      );
    }

    return res.send({
      data: postList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
const getPostByAddress = async (req, res, next) => {
  try {
    const { address } = req.body;
    const postList = await Post.find({
      address: {
        $regex: new RegExp(address),
        $options: "i",
      },
    });
    return res.send({
      data: postList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
const getPostComment = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const postFound = await Post.findById(idPost);
    if (postFound) {
      const commentList = await Comment.find({
        idPost: idPost,
      }).populate("idUser", ["email", "displayName", "imgUser"]);

      return res.send({
        data: commentList,
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }
    return handleCustomError(202, "Bài viết không tồn tại hoặc đã bị xóa");
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
      }).populate("idUser", ["email", "displayName", "imgUser"]);
      return res.send({
        data: commentList,
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }
    return handlerCustomError(202, "Bài viết không tồn tại hoặc đã bị xóa");
  } catch (error) {
    next(error);
  }
};
const getBlogList = async (req, res, next) => {
  try {
    const blogList = await Blog.find({ status: "done" }).populate("owner", [
      "displayName",
      "imgUser",
      "email",
    ]);
    return res.status(200).send({
      data: blogList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};

const getLikeListPost = async (req, res, next) => {
  try {
    const { idPost } = req.body;
    const postFound = await Post.findById(idPost).populate("like_list", [
      "email",
      "displayName",
      "imgUser",
    ]);
    if (!postFound) {
      return handleCustomError(201, "Bài viết không tồn tại hoặc đã bị xóa");
    }

    const likeList = postFound.like_list;
    return res.send({
      data: likeList,
      status: 200,
      message: "Danh sách những người like bài viết",
    });
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
    ]);
    if (!blogFound) {
      return handleCustomError(201, "Bài viết không tồn tại hoặc đã bị xóa");
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
module.exports = {
  getAddress,
  getBlogComment,
  getBlogList,
  getBlogByID,
  getPostByAddress,
  getPostComment,
  getPostById,
  getPostList,
  getLikeListBlog,
  getLikeListPost,
  getProvinceList,
};
