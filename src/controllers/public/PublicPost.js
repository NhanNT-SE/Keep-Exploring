import {customError} from "../../helpers/CustomError.js";
import Comment from "../../models/Comment.js";
import Post from "../../models/Post.js";
import "../../models/User.js";
import "../../models/Comment.js";

const getPostById = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const post = await Post.findById(idPost).populate("owner", [
      "displayName",
      "imgUser",
      "email",
      "blog",
      "post",
    ]);
    if (post) {
      return res
        .status(200)
        .send({ data: post, status: 200, message: "Lấy dữ liệu thành công" });
    }
    return customError(201, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};
const getPostList = async (req, res, next) => {
  try {
    const { category } = req.query;
    let postList;
    if (category === "" || !category) {
      postList = await Post.find({ status: "done" })
        .populate("owner", ["displayName", "imgUser", "email", "post", "blog"])
        .sort({ created_on: -1 });
    } else {
      postList = await Post.find({ status: "done", category })
        .populate("owner", ["displayName", "imgUser", "email", "post", "blog"])
        .sort({ created_on: -1 });
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
const getPostByQuery = async (req, res, next) => {
  try {
    const query = req.query.query || "";
    const postList = await Post.find({ status: "done" })
      .populate("owner", ["displayName", "imgUser", "email", "post", "blog"])
      .sort({ created_on: -1 });
    let resultList = postList.filter((e) =>
      e.owner.displayName.toLowerCase().includes(query.toLowerCase())
    );
    if (resultList.length === 0) {
      resultList = postList.filter((e) =>
        e.address.toLowerCase().includes(query.toLowerCase())
      );
    }
    if (resultList.length === 0) {
      resultList = postList.filter((e) =>
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
const getPostComment = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const postFound = await Post.findById(idPost);
    if (postFound) {
      const commentList = await Comment.find({
        idPost: idPost,
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

const getLikeListPost = async (req, res, next) => {
  try {
    const { idPost } = req.body;
    const postFound = await Post.findById(idPost).populate("like_list", [
      "email",
      "displayName",
      "imgUser",
      "post",
      "blog",
    ]);
    if (!postFound) {
      return customError(201, "Bài viết không tồn tại hoặc đã bị xóa");
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

export {
  getPostByQuery,
  getPostComment,
  getPostById,
  getPostList,
  getLikeListPost,
};
