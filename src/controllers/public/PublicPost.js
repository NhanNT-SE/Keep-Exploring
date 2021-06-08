import { customError } from "../../helpers/CustomError.js";
import { Comment } from "../../models/Comment.js";
import { Post } from "../../models/Post.js";
import "../../models/User.js";
import "../../models/Comment.js";
import { customResponse } from "../../helpers/CustomResponse.js";

const getPostById = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const post = await Post.findOne({
      _id: idPost,
      status: "done",
      view_mode: "public",
    }).populate("owner", ["displayName", "imgUser", "email", "blog", "post"]);
    if (post) {
      return res.send(customResponse(post));
    }
    return customError("Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};
const getPostList = async (req, res, next) => {
  try {
    const { category } = req.query;
    let postList;
    if (category === "" || !category) {
      postList = await Post.find({ status: "done", view_mode: "public" })
        .populate("owner", ["displayName", "imgUser", "email", "post", "blog"])
        .sort({ created_on: -1 });
    } else {
      postList = await Post.find({ status: "done", category })
        .populate("owner", ["displayName", "imgUser", "email", "post", "blog"])
        .sort({ created_on: -1 });
    }
    return res.send(customResponse(postList));
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
    return res.send(customResponse(resultList));
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
      return res.send(customResponse(commentList.reverse()));
    }
    return customError("Bài viết không tồn tại hoặc đã bị xóa");
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
      return customError("Bài viết không tồn tại hoặc đã bị xóa");
    }

    const likeList = postFound.like_list;
    return res.send(customResponse(likeList));
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
