import fs from "fs";
import { customError } from "../../helpers/CustomError.js";
import { customResponse } from "../../helpers/CustomResponse.js";
import { Post } from "../../models/Post.js";
import lodash from "lodash";
import { AdvancedInfo } from "../../models/AdvancedInfo.js";
import { Album } from "../../models/Album.js";
import { Story } from "../../models/Story.js";
import { deleteDirectory } from "../../helpers/Storage.js";
const getAllPost = async (req, res, next) => {
  try {
    const post_list = await Post.find({})
      .populate("owner album story")
      .sort({ created_on: -1 });
    return res.send(customResponse(post_list));
  } catch (error) {
    next(error);
  }
};
const getPostById = async (req, res, next) => {
  try {
    const { postId } = req.params;
    const post = await Post.findById(postId).populate("owner album story");
    if (!post) {
      customError("This post doesn't exists !");
    }
    return res.send(customResponse(post));
  } catch (error) {
    next(error);
  }
};
const deletePost = async (req, res, next) => {
  const { session, opts } = req;
  session.startTransaction();
  try {
    const { postId } = req.params;
    const post = await Post.findByIdAndRemove(postId, opts);
    if (!post) {
      customError("This post was deleted");
    }
    await AdvancedInfo.findOneAndUpdate(
      { owner: post.owner },
      { $pull: { postList: post._id } },
      opts
    );
    if (post.album) {
      await Album.findOneAndRemove({ post }, opts);
    } else {
      await Story.findOneAndRemove({ post }, opts);
    }
    await deleteDirectory(`post/${post._id}`);
    await session.commitTransaction();
    session.endSession();
    return res.send(customResponse(post, "Xoa bai viet thanh cong"));
  } catch (error) {
    await session.abortTransaction();
    session.endSession();
    next(error);
  }
};
const updateStatus = async (req, res, next) => {
  try {
    const { postId } = req.params;
    const { status } = req.body;
    const post = await Post.findByIdAndUpdate(postId, { status });
    if (!post) {
      customError("This post doesn't exists !");
    }
    return res.send(customResponse(post, "Cập nhật bài viết thành công"));
  } catch (error) {
    console.log(error);
    next(error);
  }
};

export { deletePost, getAllPost, getPostById, updateStatus };
