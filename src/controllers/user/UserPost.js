import fs from "fs";
import { Post } from "../../models/Post.js";
import { Notification } from "../../models/Notification.js";
import { User } from "../../models/User.js";
import { customError } from "../../helpers/CustomError.js";
import { createNotification } from "../../helpers/NotifyHelper.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customResponse } from "../../helpers/CustomResponse.js";

const createAlbum = async (req, res, next) => {
  const { files, session, opts } = req;
  // session.startTransaction();
  try {
    if(files.length === 0){
      customError("Needs at least one image to create this post")
    }
    console.log(files.length)
    const user = await User.findById(req.user._id);

    return res.send(customResponse({ user }, "Tạo bài viết thành công"));
  } catch (error) {
    next(error);
  }
};
const createStory = async (req, res, next) => {
  const { files, session, opts } = req;
  session.startTransaction();
  try {
    const user = await User.findById(req.user._id);

    return res.send(customResponse({ user }, "Tạo bài viết thành công"));
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

        await User.findByIdAndUpdate(user._id, { $pull: { post: postID } });

        return res.send(customResponse(postFound, "Đã xóa bài viết"));
      }
      customError("Bạn không the xoa bài viết này");
    }

    customError("Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const getPostListByUser = async (req, res, next) => {
  try {
    const user = req.user;
    const { idUser } = req.params;
    let postList = [];
    if (idUser === user._id) {
      postList = await Post.find({ owner: idUser })
        .populate("owner", ["displayName", "imgUser", "email"])
        .sort({ created_on: -1 });
    } else {
      postList = await Post.find({ owner: idUser, status: "done" })
        .populate("owner", ["displayName", "imgUser", "email"])
        .sort({ created_on: -1 });
    }

    return res.send(customResponse(postList));
  } catch (error) {
    next(error);
  }
};

const likePost = async (req, res, next) => {
  try {
    const { idPost } = req.body;
    const user = req.user;
    const postFound = await Post.findById(idPost);
    const io = req.io;
    if (postFound) {
      let like_list = [...postFound.like_list];
      const index = like_list.findIndex((e) => e == user._id);
      let notification = null;
      if (index >= 0) {
        like_list.splice(index, 1);
      } else {
        like_list.push(user._id);
        const notify = new Notification({
          idUser: postFound.owner.toString(),
          idPost: idPost,
          status: "new",
          content: "like",
        });
        notification = createNotification(notify);
      }
      postFound.like_list = like_list;
      await Post.findByIdAndUpdate(idPost, { ...postFound });
      if (notification) {
        const msgNotify = `Vừa có người yêu thích về bài viết ${postFound.title} của bạn`;
        sendNotifyRealtime(io, postFound.owner, {
          message: msgNotify,
          type: "like",
          idPost,
        });
      }
      const resMsg = notification
        ? "Đã thích bài viết"
        : "Đã bỏ thích bài viết";
      return res.send(customResponse(postFound, resMsg));
    }

    customError("Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const updatePost = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const { imgs_deleted } = req.body;
    const user = req.user;
    const files = req.files;
    const { io } = req;

    const postFound = await Post.findById(idPost);

    if (postFound) {
      let img_list = postFound.imgs;

      if ((user._id = postFound.owner)) {
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

        if (files) {
          const len_files = files.length;
          if (postFound.imgs.lenth - length_deleted + len_files > 20) {
            customError("Số lượng hình ảnh không được vượt quá 20");
          }

          for (i = 0; i < len_files; i++) {
            img_list.push(files[i].filename);
          }
        }
        const newPost = {
          ...req.body,
          imgs: img_list,
          status: "pending",
        };
        await Post.findByIdAndUpdate(idPost, newPost);

        const notify = {
          idUser: postFound.owner.toString(),
          idPost: idPost,
          status: "new",
          content: "unmoderated",
          statusPost: "pending",
        };
        const notification = await createNotification(notify);
        const msgNotify = `Bài viết ${postFound.title} của bạn đã được cập nhật, bài viết hiện đang trong quá trình kiểm duyệt`;
        sendNotifyRealtime(io, postFound.owner, {
          message: msgNotify,
          type: "pending",
          idPost,
        });
        io.emit("notification:admin", {
          image: postFound.imgs[0],
          type: "post",
          id: postFound._id,
          message: `Bài viết ${postFound.title} vừa được chỉnh sửa, đang chờ kiểm duyệt`,
        });

        return res.send(
          customResponse(
            { newPost, notification },
            "Cập nhật bài viết thành công"
          )
        );
      }

      return customError("Bạn không thể cập nhật bài viết này");
    }
  } catch (error) {
    next(error);
  }
};

export { createAlbum, createStory, deletePost, getPostListByUser, likePost, updatePost };
