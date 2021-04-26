const fs = require("fs");
const handlerCustomError = require("../middleware/customError");
const { createNotification } = require("./NotificationController");
const Post = require("../Models/Post");
const Blog = require("../Models/Blog");
const User = require("../Models/User");
const Notification = require("../Models/Notification");
const Comment = require("../Models/Comment");
const { sendNotifyRealtime } = require("../middleware/Socket.io");
const monthList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
require("../Models/Address");
const deleteAllCommentPost = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    const commentList = await Comment.find(
      { idPost: idPost },
      { img: 1, _id: 0 }
    );
    if (commentList) {
      commentList.forEach((item) => {
        fs.unlinkSync("src/public/images/comment/post/" + item.img);
      });
    }

    await Comment.deleteMany({ idPost: idPost });
    await Post.findByIdAndUpdate(
      idPost,
      { $unset: { comment: "" } },
      { multi: true }
    );

    return res.send({
      data: null,
      status: 200,
      message: "Đã xóa tất cả comment của bài Post",
    });
  } catch (error) {
    next(error);
  }
};
const deleteAllCommentBlog = async (req, res, next) => {
  try {
    const { idBlog } = req.params;
    const commentList = await Comment.find(
      { idBlog: idBlog },
      { img: 1, _id: 0 }
    );
    if (commentList) {
      commentList.forEach((item) => {
        fs.unlinkSync("src/public/images/comment/blog/" + item.img);
      });
    }

    await Comment.deleteMany({ idBlog: idBlog });
    await Blog.findByIdAndUpdate(
      idBlog,
      { $unset: { comment: 1 } },
      { multi: true }
    );

    return res.send({
      data: null,
      status: 200,
      message: "Đã xóa tất cả comment của bài Blog",
    });
  } catch (error) {
    next(error);
  }
};
const getAllUser = async (req, res, next) => {
  try {
    const userList = await User.find(
      { role: "user" },
      { pass: 0, role: 0 }
    ).sort({ created_on: -1 });
    return res.send({
      data: userList,
      status: 201,
      message: "Danh sách tất cả người dùng",
    });
  } catch (error) {
    next(error);
  }
};
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
const getAllBlog = async (req, res, next) => {
  try {
    const blog_list = await Blog.find({})
      .populate("owner", ["displayName", "imgUser", "email"])
      .sort({ created_on: -1 });
    return res.send({
      data: blog_list,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
const deleteUser = async (req, res, next) => {
  try {
    const { idUser } = req.params;
    const userFound = await User.findById(idUser);
    if (!userFound) {
      return handlerCustomError(
        202,
        "Người dùng này không tồn tại hoặc đã bị xóa"
      );
    }

    if (userFound.role == "admin") {
      return handlerCustomError(203, "Bạn không thể xóa tài khoản admin");
    }
    if (userFound.imgUser && userFound.imgUser !== "avatar-default.png") {
      fs.unlinkSync("src/public/images/user/" + userFound.imgUser);
    }
    await User.findByIdAndDelete(idUser);
    return res.send({
      data: null,
      status: 200,
      message: "Xóa tài khoản thành công",
    });
  } catch (error) {
    next(error);
  }
};
const sendNotify = async (req, res, next) => {
  try {
    const { idUser, contentAdmin } = req.body;
    const user = {};
    const listUser = [];
    const { io } = req;
    const msgNotify = `Bạn có thông báo mới từ hệ thống`;

    idUser.forEach((e) => {
      user.idUser = e;
      user.contentAdmin = contentAdmin;
      listUser.push(user);
      sendNotifyRealtime(io, e, {
        message: msgNotify,
        type: "system",
        content: contentAdmin,
      });
    });
    await Notification.insertMany(listUser);
    return res.send({
      data: listUser,
      status: 200,
      message: "Tạo thông báo thành công",
    });
  } catch (error) {
    next(error);
  }
};
const updateStatus = async (req, res, next) => {
  try {
    const { idUpdate, status, type } = req.body;
    let updateFound = null;
    let notify = null;
    let msgNotify;
    let notifyClient = {};
    const { io } = req;
    if (type === "post") {
      updateFound = await Post.findById(idUpdate);
    } else {
      updateFound = await Blog.findById(idUpdate);
    }
    if (updateFound) {
      updateFound.status = status;
      if (type === "post") {
        await Post.findByIdAndUpdate(idUpdate, updateFound);
        notify = {
          idUser: updateFound.owner.toString(),
          idPost: idUpdate,
          status: "new",
          statusPost: status,
        };
        notifyClient.idPost = idUpdate;
      } else {
        await Blog.findByIdAndUpdate(idUpdate, updateFound);
        notify = {
          idUser: updateFound.owner.toString(),
          idBlog: idUpdate,
          status: "new",
          statusBlog: status,
        };
        notifyClient.idBlog = idUpdate;
      }

      if (status === "done") {
        notify.content = "moderated";
        msgNotify = `Bài viết ${updateFound.title} của bạn đã được kiểm duyệt, và đang được hiển thị với mọi người`;
      } else {
        notify.content = "unmoderated";
        if (status === "pending") {
          msgNotify = `Bài viết ${updateFound.title} của bạn hiện đang trong quá trình kiểm duyệt`;
        } else {
          msgNotify = `Bài viết ${updateFound.title} của bạn cần được chỉnh sủa`;
        }
      }
      const notification = await createNotification(notify);

      notifyClient.message = msgNotify;
      notifyClient.type = status;
      sendNotifyRealtime(io, updateFound.owner, notifyClient);
      return res.send({
        data: notification,
        status: 200,
        message: "Cập nhật bài viết thành công",
      });
    }
    return handlerCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    console.log(error);
    next(error);
  }
};
const statisticsNumber = async (req, res, next) => {
  try {
    const totalPost = await Post.countDocuments({});
    const totalBlog = await Blog.countDocuments({});
    const pendingPost = await Post.countDocuments({ status: "pending" });
    const pendingBlog = await Blog.countDocuments({ status: "pending" });
    const need_updatePost = await Post.countDocuments({
      status: "need_update",
    });
    const need_updateBlog = await Blog.countDocuments({
      status: "need_update",
    });
    const donePost = await Post.countDocuments({ status: "done" });
    const doneBlog = await Blog.countDocuments({ status: "done" });
    const admin = await User.countDocuments({ role: "admin" });
    const user = await User.countDocuments({ role: "user" });
    const data = {
      user: {
        title: `Users(${user + admin})`,
        data: [user, admin],
      },
      postBlog: {
        title: `Post-Blog(${totalPost + totalBlog})`,
        data: [totalPost, totalBlog],
      },
      post: {
        title: `Post(${totalPost})`,
        data: [donePost, pendingPost, need_updatePost],
      },
      blog: {
        title: `Blog(${totalBlog})`,
        data: [doneBlog, pendingBlog, need_updateBlog],
      },
    };
    return res
      .status(200)
      .send({ data, status: 200, message: "Lấy dữ liệu thành công" });
  } catch (error) {
    next(error);
  }
};

const statisticsTimeLine = async (req, res, next) => {
  try {
    const monthPost = await Post.aggregate([
      {
        $group: {
          _id: { $month: "$created_on" },
          count: { $sum: 1 },
        },
      },
      {
        $sort: { _id: 1 },
      },
      {
        $project: {
          count: 1,
          month: "$_id",
          _id: 0,
        },
      },
    ]);
    const monthBlog = await Blog.aggregate([
      {
        $group: {
          _id: { $month: "$created_on" },
          count: { $sum: 1 },
        },
      },
      {
        $sort: { _id: 1 },
      },
      {
        $project: {
          count: 1,
          month: "$_id",
          _id: 0,
        },
      },
    ]);
    const monthUser = await User.aggregate([
      {
        $match: { role: "user" },
      },
      {
        $group: {
          _id: { $month: "$created_on" },
          count: { $sum: 1 },
        },
      },

      {
        $sort: { _id: 1 },
      },
      {
        $project: {
          count: 1,
          month: "$_id",
          _id: 0,
        },
      },
    ]);

    const resultUser = convertStaticsList(monthUser);
    const resultPost = convertStaticsList(monthPost);
    const resultBlog = convertStaticsList(monthBlog);
    return res.status(200).send({
      data: { user: resultUser, post: resultPost, blog: resultBlog },
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    console.log(error);
    next(error);
  }
};
const convertStaticsList = (inputList) => {
  const monthInput = inputList.map((e) => e.month);
  const monthNull = monthList.filter((n) => !monthInput.includes(n));
  const resultNull = [];
  monthNull.map((e) => {
    const item = {};
    item.month = e;
    item.count = 0;
    resultNull.push(item);
  });
  const resultList = resultNull
    .concat(inputList)
    .sort((a, b) => a.month - b.month);
  return resultList.map((e) => e.count);
};
module.exports = {
  deleteAllCommentBlog,
  deleteAllCommentPost,
  deleteUser,
  getAllBlog,
  getAllPost,
  getAllUser,
  sendNotify,
  updateStatus,
  statisticsNumber,
  statisticsTimeLine,
};
