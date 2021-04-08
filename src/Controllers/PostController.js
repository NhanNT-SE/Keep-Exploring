const Post = require("../Models/Post");
const Address = require("../Models/Address");
const fs = require("fs");
const Notification = require("../Models/Notification");
const { createNotification } = require("./NotificationController");
const User = require("../Models/User");
const handlerCustomError = require("../middleware/customError");

const createPost = async (req, res, next) => {
  try {
    const user = await User.findById(req.user._id);
    let img_list = new Array();
    //Luu string hinh anh vao database
    const files = req.files;
    const length = files.length;
    for (let i = 0; i < length; ++i) {
      img_list.push(files[i].filename);
    }

    const post = new Post({
      ...req.body,
      owner: user._id,
      imgs: img_list,
    });

    await post.save();
    user.post.push(post._id);
    await user.save();

    //Tao notify khi co nguoi tao bai viet
    const notify = new Notification({
      idUser: user._id,
      idPost: post._id,
      status: "new",
      content: "unmoderated",
    });
    const notification = await createNotification(notify);

    return res.status(200).send({
      data: { post, notification },
      err: "",
      status: 200,
      msg: "Tạo bài viết thành công, bài viết đang được kiểm duyệt",
    });
  } catch (error) {
    next(error);
  }
};

const deletePost = async (req, res, next) => {
  try {
    const { postID } = req.params;
    const user = req.user;

    const postFound = await Post.findById(postID);

    if (postFound) {
      if (user._id == postFound.owner.toString() || user.role == "admin") {
        const len = postFound.imgs.length;

        for (let i = 0; i < len; i++) {
          fs.unlink("src/public/images/post/" + postFound.imgs[i], (err) => {
            if (err) {
              console.log(err);
            }
          });
        }
        await Post.findByIdAndDelete(postID);

        //Xoa bai post khoi postlist cua user
        await User.findByIdAndUpdate(user._id, { $pull: { post: postID } });

        return res.status(200).send({
          data: postFound,
          err: "",
          status: 200,
          message: "Đã xóa bài viết",
        });
      }

      return handleCustomError(202, "Bạn không phải admin/owner bài viết này");
    }

    return handleCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const getPostListByUser = async (req, res, next) => {
  try {
    const { idUser } = req.params;
    const postList = await Post.find({ owner: idUser });
    if (postList) {
      return res.send({ data: postList, status: 200, message: "" });
    }
    return handleCustomError(201, "Người dùng này chưa có bài viết");
  } catch (error) {
    next(error);
  }
};

const likePost = async (req, res, next) => {
  try {
    //Lay id bai viet va id nguoi dung tu req
    const { idPost } = req.body;
    const user = req.user;

    //Kiem tra bai viet co ton tai hay khong
    const postFound = await Post.findById(idPost);
    if (postFound) {
      //Kiem tra nguoi dung da like bai viet hay chua
      var i = 0;
      var like_list = postFound.like_list;
      const len = like_list.length;

      for (i; i < len; i++) {
        //Neu nguoi dung da like bai viet thi doi thanh dislike- remove idUser khoi like_list
        if ((user._id = like_list[i])) {
          await postFound.like_list.splice(i, 1);
          await postFound.save();
          return res.send({
            data: null,
            status: 201,
            message: "Đã bỏ thích bài viết",
          });
        }
      }

      //Tao notify khi co nguoi like bai viet
      const notify = new Notification({
        idUser: postFound.owner.toString(),
        idPost: idPost,
        status: "new",
        content: "like",
      });
      const notification = await createNotification(notify);

      //Con neu nguoi dung chua like bai viet thi push idUser vao like_list
      await postFound.like_list.push(user._id);
      await postFound.save();

      return res.send({
        data: notification,
        status: 200,
        message: "Đã thích bài viết",
      });
    }

    return handlerCustomError(202, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const updatePost = async (req, res, next) => {
  try {
    //Lay data tu phia client gui len
    const { idPost } = req.params;
    const { imgs_deleted } = req.body;
    const user = req.user;
    const files = req.files;

    //Kiem tra bai viet co ton tai hay khong
    const postFound = await Post.findById(idPost);

    if (postFound) {
      let img_list = postFound.imgs;

      //Kiem tra user dang nhap co phai owner cua bai viet hay khong
      if ((user._id = postFound.owner)) {
        //Kiem tra co xoa img nao hay khong neu co thi xoa img phia server theo filename
        let length_deleted = 0;
        if (imgs_deleted) {
          const imageDeleteList = imgs_deleted.split(",");
          const tempList = [];
          img_list.some((e) => {
            if (imageDeleteList.indexOf(e) < 0) {
              tempList.push(e);
            } else {
              fs.unlink("src/public/images/post/" + e, (err) => {
                if (err) {
                  console.log(err);
                }
              });
            }
          });
          img_list = tempList;
        }

        //Kiem tra user co upload them hinh moi khong
        if (files) {
          const len_files = files.length;
          //Neu so luong hinh upload + so luong hinh o bai viet cu vuot qua gioi han thi tra ve status code 202
          if (postFound.imgs.lenth - length_deleted + len_files > 20) {
            handlerCustomError(
              202,
              "Vượt quá số lượng hình ảnh không được vượt quá 20"
            );
          }

          //Neu chua vuot gioi han thi thuc hien upload hinh anh len server
          for (i = 0; i < len_files; i++) {
            img_list.push(files[i].filename);
          }
        }
        //Tao object luu nhung update cua bai viet
        const newPost = {
          ...req.body,
          imgs: img_list,
          status: "pending",
        };
        await Post.findByIdAndUpdate(idPost, newPost);

        //Tao notify
        const notify = new Notification({
          idUser: postFound.owner.toString(),
          idPost: idPost,
          status: "new",
          content: "unmoderated",
        });
        const notification = await createNotification(notify);

        return res.send({
          data: { newPost, notification },
          status: 200,
          message:
            "Cập nhật bài viết thành công, bài viết của bạn đang được kiểm duyệt",
        });
      }

      //Neu khong phai owner bai viet thi tra ve status code 201
      return handlerCustomError(201, "Bạn không thể cập nhật bài viết này");
    }
  } catch (error) {
    console.log(error);
    next(error);
  }
};

module.exports = {
  createPost,
  deletePost,
  getPostListByUser,
  likePost,
  updatePost,
};
