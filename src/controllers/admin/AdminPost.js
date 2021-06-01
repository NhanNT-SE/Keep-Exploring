import fs from "fs";
import {customError} from "../../helpers/CustomError.js";
import {Post} from "../../models/Post.js";

const getAllPost = async (req, res, next) => {
  try {
    const post_list = await Post.find({})
      .populate("owner", ["displayName", "imgUser", "email"])
      .sort({ created_on: -1 });
    return res.status(200).send({
      data: post_list,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
const deletePost = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const post = await Post.findById(idPost);
    if (post) {
      await Post.findByIdAndDelete(idPost);
      return res.status(200).send({
        data: post,
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
    const { idPost, status } = req.body;
    const post = await Post.findById(idPost);
    const { io } = req;
    if (post) {
      const postUpdate = {
        status,
        view_mode: "public",
      };
      await Post.findByIdAndUpdate(idPost, postUpdate);
      return res.send({
        data: post,
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

export { deletePost, getAllPost, updateStatus };
