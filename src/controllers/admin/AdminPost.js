import fs from "fs";
import { customError } from "../../helpers/CustomError.js";
import { customResponse } from "../../helpers/CustomResponse.js";
import { Post } from "../../models/Post.js";

const getAllPost = async (req, res, next) => {
  try {
    const post_list = await Post.find({})
      .populate("owner", ["displayName", "imgUser", "email"])
      .sort({ created_on: -1 });
    return res.send(customResponse(post_list));
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
      return res.send(customResponse(post, "Xoa bai viet thanh cong"));
    }
    customError("Bai viet khong ton tai");
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
      await post.updateOne({ ...postUpdate }, { returnOriginal: false });
      return res.send(customResponse(post, "Cập nhật bài viết thành công"));
    }
    return customError("Bài viết không tồn tại");
  } catch (error) {
    console.log(error);
    next(error);
  }
};

export { deletePost, getAllPost, updateStatus };
