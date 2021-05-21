import fs from "fs";
import Blog from "../../models/Blog.js";
import Blog_Detail from "../../models/Blog_Detail.js";
import Notification from "../../models/Notification.js";
import User from "../../models/User.js";
import {customError} from "../../helpers/CustomError.js";
import  {createNotification}  from "../../helpers/NotifyHelper.js";

import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";

const createBlog = async (req, res, next) => {
  try {
    let { title, detail_list, folder_storage } = req.body;
    const file = req.file;
    const { io } = req;
    const user = await User.findById(req.user._id);
    const blog = new Blog({ title, folder_storage });
    const blog_detail = new Blog_Detail({});
    if (file) {
      blog.img = file.filename;
    }
    let tempList = [];
    const isArray = Array.isArray(detail_list);
    if (!isArray) {
      tempList.push(JSON.parse(detail_list));
    } else {
      tempList = detail_list.map((e) => JSON.parse(e));
    }
    tempList.forEach((element) => {
      delete element.uriImage;
    });
    blog_detail.detail_list = [...tempList];
    blog_detail._id = blog._id;

    blog.owner = user._id;
    blog.blog_detail = blog._id;
    user.blog.push(blog._id);

    await blog.save();
    await blog_detail.save();
    await user.save();
    const notify = {
      idUser: user._id,
      idBlog: blog._id,
      status: "new",
      content: "unmoderated",
      statusBlog: "pending",
    };

    await createNotification(notify);
    const msgNotify = `Bài viết ${blog.title} của bạn hiện đang trong quá trình kiểm duyệt`;
    sendNotifyRealtime(io, blog.owner, {
      message: msgNotify,
      type: "pending",
      idBlog: blog._id,
    });
    io.emit("notification:admin", {
      image: blog.img,
      type: "blog",
      id: blog._id,
      message: `Bài viết ${blog.title} vừa được tạo, đang chờ kiểm duyệt`,
    });
    return res.status(200).send({
      status: 200,
      data: blog_detail,
      message: "Tạo bài viết thành công",
    });
  } catch (error) {
    next(error);
  }
};
const deleteBlog = async (req, res, next) => {
  try {
    const user = req.user;
    const { idBlog } = req.params;

    const blogFound = await Blog.findById(idBlog);
    const detailFound = await Blog_Detail.findById(idBlog);
    if (user.role === "admin" || user._id == blogFound.owner.toString()) {
      if (blogFound && detailFound) {
        fs.unlink("src/public/images/blog/" + blogFound.img, (err) => {
          if (err) {
            console.log(err);
          }
        });
        await Blog_Detail.findByIdAndDelete(idBlog);
        await Blog.findByIdAndDelete(idBlog);
        await User.findByIdAndUpdate(user._id, { $pull: { blog: idBlog } });
        return res.status(200).send({
          status: 200,
          data: blogFound,
          message: "Xóa bài viết thành công",
        });
      }
      customError(201, "Bài viết không tồn tại");
    }
    customError(201, "Bạn thể xóa bài viết của người khác");
  } catch (error) {
    next(error);
  }
};
const likeBlog = async (req, res, next) => {
  try {
    const { idBlog } = req.body;
    const user = req.user;
    const { io } = req;
    const blogFound = await Blog.findById(idBlog);
    if (blogFound) {
      let like_list = [...blogFound.like_list];
      const index = like_list.findIndex((e) => e == user._id);
      let notification = null;
      if (index >= 0) {
        like_list.splice(index, 1);
      } else {
        like_list.push(user._id);
        const notify = new Notification({
          idUser: blogFound.owner.toString(),
          idBlog,
          status: "new",
          content: "like",
        });
        notification = createNotification(notify);
      }

      blogFound.like_list = like_list;
      await Blog.findByIdAndUpdate(idBlog, { ...blogFound });
      if (notification) {
        const msgNotify = `Vừa có người yêu thích về bài viết ${blogFound.title} của bạn`;
        sendNotifyRealtime(io, blogFound.owner, {
          message: msgNotify,
          type: "like",
          idBlog,
        });
      }
      return res.send({
        data: blogFound,
        status: 200,
        message: notification ? "Đã thích bài viết" : "Đã bỏ thích bài viết",
      });
    }

    customError(202, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const getBlogListByUser = async (req, res, next) => {
  try {
    const user = req.user;
    const { idUser } = req.params;
    let blogList = [];
    if (idUser === user._id) {
      blogList = await Blog.find({ owner: idUser })
        .populate("owner", ["displayName", "imgUser", "email"])
        .sort({ created_on: 1 });
    } else {
      blogList = await Blog.find({
        owner: idUser,
        status: "done",
      })
        .populate("owner", ["displayName", "imgUser", "email"])
        .sort({ created_on: -1 });
    }
    return res.send({
      data: blogList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
const updateBlog = async (req, res, next) => {
  try {
    const { title, created_on, detail_list } = req.body;
    const { idBlog } = req.params;
    const { io } = req;
    const blog = await Blog.findById(idBlog);
    const blog_detail = await Blog_Detail.findById(idBlog);
    const file = req.file;
    if (file) {
      fs.unlink(`src/public/images/blog/${blog.img}`, (err) => {
        if (err) {
          console.log(err);
        }
      });
      blog.img = file.filename;
    }
    blog.status = "pending";
    blog.title = title;
    blog.created_on = created_on;
    let tempList = [];
    const isArray = Array.isArray(detail_list);
    if (!isArray) {
      tempList.push(JSON.parse(detail_list));
    } else {
      tempList = detail_list.map((e) => JSON.parse(e));
    }
    tempList.forEach((element) => {
      delete element.uriImage;
    });

    await Blog.findByIdAndUpdate(idBlog, { ...blog });
    await Blog_Detail.findByIdAndUpdate(idBlog, { detail_list: tempList });
    const notify = {
      idUser: blog.owner.toString(),
      idBlog: blog.id,
      status: "new",
      content: "unmoderated",
      statusBlog: "pending",
    };
    await createNotification(notify);
    const msgNotify = `Bài viết ${blog.title} của bạn đã được cập nhật, bài viết hiện đang trong quá trình kiểm duyệt`;
    sendNotifyRealtime(io, blog.owner, {
      message: msgNotify,
      type: "pending",
      idBlog: blog.id,
    });
    io.emit("notification:admin", {
      image: blog.img,
      type: "blog",
      id: blog._id,
      message: `Bài ${blog.title} viết vừa chỉnh sửa, đang chờ kiểm duyệt`,
    });
    return res.status(200).send({
      status: 200,
      data: blog_detail,
      message: "Đã cập nhật bài viết",
    });
  } catch (error) {
    next(error);
  }
};

export { createBlog, deleteBlog, likeBlog, getBlogListByUser, updateBlog };
