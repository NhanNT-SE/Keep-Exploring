import fs from "fs";
import { Blog } from "../../models/Blog.js";
import { Comment } from "../../models/Comment.js";
import { Notification } from "../../models/Notification.js";
import { Post } from "../../models/Post.js";
import { createNotification } from "../../helpers/NotifyHelper.js";
import { customError } from "../../helpers/CustomError.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customResponse } from "../../helpers/CustomResponse.js";

const createCommentPost = async (req, res, next) => {
  try {
    const { content, idPost } = req.body;
    const file = req.file;
    const user = req.user;
    const { io } = req;

    const postFound = await Post.findById(idPost);

    if (postFound) {
      let img = "";
      if (file) {
        img = file.filename;
      }

      const comment = new Comment({
        idPost,
        idUser: user._id,
        content,
        img,
      });

      await comment.save();

      postFound.comment.push(comment._id);
      await postFound.save();

      const notify = new Notification({
        idUser: postFound.owner.toString(),
        idPost: idPost,
        status: "new",
        content: "comment",
      });
      await createNotification(notify);
      const msgNotify = `Vừa có người bình luận về bài viết ${postFound.title} của bạn`;
      sendNotifyRealtime(io, postFound.owner, {
        message: msgNotify,
        type: "comment",
        idPost,
      });
      return res.send(customResponse(comment, "Tạo bình luận thành công"));
    }

    return customError("Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const createCommentBlog = async (req, res, next) => {
  try {
    const { content, idBlog } = req.body;
    const file = req.file;
    const user = req.user;
    const { io } = req;

    const blogFound = await Blog.findById(idBlog);

    if (blogFound) {
      let img;

      if (file) {
        img = file.filename;
      }

      const comment = new Comment({
        idBlog,
        idUser: user._id,
        content,
        img,
      });

      await comment.save();

      blogFound.comment.push(comment._id);
      await blogFound.save();

      const notify = new Notification({
        idUser: blogFound.owner.toString(),
        idBlog,
        status: "new",
        content: "comment",
      });
      await createNotification(notify);
      const msgNotify = `Vừa có người bình luận về bài viết ${blogFound.title} của bạn`;
      sendNotifyRealtime(io, blogFound.owner, {
        message: msgNotify,
        type: "comment",
        idBlog,
      });
      return res.send(customResponse(comment, "Tạo bình luận thành công"));
    }

    return customError("Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const deleteCommentByID = async (req, res, next) => {
  try {
    const user = req.user;
    const { idComment } = req.params;
    const commentFound = await Comment.findById(idComment);
    if (user.role == "admin" || user._id == commentFound.idUser) {
      if (commentFound.idPost) {
        if (commentFound.img) {
          fs.unlink(
            "src/public/images/comment/post/" + commentFound.img,
            (err) => {
              console.log(err);
            }
          );
        }

        await Post.findByIdAndUpdate(commentFound.idPost, {
          $pull: { comment: idComment },
        });
      }

      if (commentFound.idBlog) {
        if (commentFound.img) {
          fs.unlink(
            "src/public/images/comment/blog/" + commentFound.img,
            (err) => {
              console.log(err);
            }
          );
        }
        await Blog.findByIdAndUpdate(commentFound.idBlog, {
          $pull: { comment: idComment },
        });
      }

      await Comment.findByIdAndDelete(idComment);
      return res.send(
        customResponse(commentFound, "Đã xóa bình luận thành công ")
      );
    }

    return customError("Bạn không the comment này");
  } catch (error) {
    next(error);
  }
};
const editCommentBlog = async (req, res, next) => {
  try {
    const { idComment, content, deleteImg } = req.body;
    const user = req.user;
    const file = req.file;
    const commentFound = await Comment.findById(idComment);

    if (!commentFound) {
      return customError("Bình luận không tồn tại");
    }

    if (user._id != commentFound.idUser.toString()) {
      return customError("Bạn không có quyền chỉnh sửa comment này");
    }

    if (commentFound.img && deleteImg) {
      fs.unlinkSync("src/public/images/comment/blog/" + commentFound.img);
    }

    if (file) {
      commentFound.img = file.filename;
    }
    commentFound.content = content;

    await commentFound.save();
    return res.send(
      customResponse(commentFound, "Chỉnh sửa bình luận thành công")
    );
  } catch (error) {
    next(error);
  }
};

const editCommentPost = async (req, res, next) => {
  try {
    const { idComment, content, deleteImg } = req.body;
    const user = req.user;
    const file = req.file;
    const commentFound = await Comment.findById(idComment);

    if (!commentFound) {
      return customError("Bình luận không tồn tại");
    }

    if (user._id != commentFound.idUser.toString()) {
      return customError("Bạn không có quyền chỉnh sửa comment này");
    }

    if (commentFound.img && deleteImg) {
      fs.unlinkSync("src/public/images/comment/post/" + commentFound.img);
    }

    if (file) {
      commentFound.img = file.filename;
    }
    commentFound.content = content;

    await commentFound.save();
    return res.send(
      customResponse(commentFound, "Chỉnh sửa bình luận thành công")
    );
  } catch (error) {
    next(error);
  }
};
export {
  createCommentPost,
  createCommentBlog,
  deleteCommentByID,
  editCommentBlog,
  editCommentPost,
};
