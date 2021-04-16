const Blog = require("../Models/Blog");
const Comment = require("../Models/Comment");
const Notification = require("../Models/Notification");
const Post = require("../Models/Post");
const { createNotification } = require("./NotificationController");
const fs = require("fs");
const handlerCustomError = require("../middleware/customError");

const createCommentPost = async (req, res, next) => {
  try {
    //Lay thong tin tu phia client
    const { content, idPost } = req.body;
    const file = req.file;
    const user = req.user;

    //Kiem tra bai post con ton tai hay khong
    const postFound = await Post.findById(idPost);

    if (postFound) {
      //Kiem tra comment co hinh anh hay khong
      let img = "";
      //Neu co thi luu vao thuoc tinh imgs
      if (file) {
        img = file.filename;
      }

      //Tao object comment voi cac thuoc tinh tu client
      const comment = new Comment({
        idPost,
        idUser: user._id,
        content,
        img,
      });

      await comment.save();

      //Push idComment vao bai Post
      postFound.comment.push(comment._id);
      await postFound.save();

      //Tao notify khi co nguoi comment bai viet
      const notify = new Notification({
        idUser: postFound.owner.toString(),
        idPost: idPost,
        status: "new",
        content: "comment",
      });
      const notification = await createNotification(notify);

      //Thanh cong tra ve status code 200

      return res.send({
        data: { comment, notification },
        status: 200,
        message: "Tạo bình luận thành công",
      });
    }

    //Neu bai viet khong ton tai thi tra ve status code 201
    return handlerCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    return res.status(202).send(error.message);
  }
};

const createCommentBlog = async (req, res, next) => {
  try {
    //Lay thong tin tu phia client
    const { content, idBlog } = req.body;
    const file = req.file;
    const user = req.user;

    //Kiem tra bai post con ton tai hay khong
    const blogFound = await Blog.findById(idBlog);

    if (blogFound) {
      //Kiem tra comment co hinh anh hay khong
      let img;

      //Neu co thi luu vao thuoc tinh imgs
      if (file) {
        img = file.filename;
      }

      //Tao object comment voi cac thuoc tinh tu client
      const comment = new Comment({
        idBlog,
        idUser: user._id,
        content,
        img,
      });

      //Luu comment vao database de lay id
      await comment.save();

      //Push idComment vao bai Post
      blogFound.comment.push(comment._id);
      await blogFound.save();

      //Tao notify khi co nguoi comment bai viet
      const notify = new Notification({
        idUser: blogFound.owner.toString(),
        idBlog,
        status: "new",
        content: "comment",
      });
      const notification = await createNotification(notify);

      //Thanh cong tra ve status code 200
      return res.send({
        data: { comment, notification },
        status: 200,
        message: "Tạo bình luận thành công",
      });
    }

    //Neu bai viet khong ton tai thi tra ve status code 201
    return handlerCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    return res.status(202).send(error.message);
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
      return res.send({
        data: null,
        status: 200,
        message: "Đã xóa bình luận thành công ",
      });
    }

    return handlerCustomError(201, "Bạn không phải admin/chủ comment này");
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
      return handlerCustomError(201, "Bình luận không tồn tại");
    }

    if (user._id != commentFound.idUser.toString()) {
      return handlerCustomError(202, "Bạn không có quyền chỉnh sửa comment này");
    }

    if (commentFound.img && deleteImg) {
      fs.unlinkSync("src/public/images/comment/blog/" + commentFound.img);
    }

    if (file) {
      commentFound.img = file.filename;
    }
    commentFound.content = content;

    await commentFound.save();
    return res.send({
      data: commentFound,
      status: 200,
      message: "Chỉnh sửa bình luận thành công",
    });
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
      return handlerCustomError(201, "Bình luận không tồn tại");
    }

    if (user._id != commentFound.idUser.toString()) {
      return handlerCustomError(
        202,
        "Bạn không có quyền chỉnh sửa comment này"
      );
    }

    if (commentFound.img && deleteImg) {
      fs.unlinkSync("src/public/images/comment/post/" + commentFound.img);
    }

    if (file) {
      commentFound.img = file.filename;
    }
    commentFound.content = content;

    await commentFound.save();
    return res.send({
      data: commentFound,
      status: 200,
      message: "Chỉnh sửa bình luận thành công",
    });
  } catch (error) {
    next(error);
  }
};
module.exports = {
  createCommentPost,
  createCommentBlog,
  deleteCommentByID,
  editCommentBlog,
  editCommentPost,
};
