import fs from "fs";
import { Blog } from "../../models/Blog.js";
import { ContentBlog } from "../../models/ContentBlog.js";
import { Notification } from "../../models/Notification.js";
import { User } from "../../models/User.js";
import { customError } from "../../helpers/CustomError.js";
import { createNotification } from "../../helpers/NotifyHelper.js";

import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customResponse } from "../../helpers/CustomResponse.js";

const createBlog = async (req, res, next) => {
  try {
    let { title, detail_list, folder_storage } = req.body;
    const file = req.file;
    const { io } = req;
    const user = await User.findById(req.user._id);
    const blog = new Blog({ title, folder_storage });
    const contentBlog = new ContentBlog({});
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
    contentBlog.detail_list = [...tempList];
    contentBlog._id = blog._id;

    blog.owner = user._id;
    blog.contentBlog = blog._id;
    user.blog.push(blog._id);

    await blog.save();
    await contentBlog.save();
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
    return res.send(customResponse(contentBlog, "Tạo bài viết thành công"));
  } catch (error) {
    next(error);
  }
};
const deleteBlog = async (req, res, next) => {
  try {
    const user = req.user;
    const { idBlog } = req.params;

    const blogFound = await Blog.findById(idBlog);
    const detailFound = await ContentBlog.findById(idBlog);
    if (user.role === "admin" || user._id == blogFound.owner.toString()) {
      if (blogFound && detailFound) {
        fs.unlink("src/public/images/blog/" + blogFound.img, (err) => {
          if (err) {
            console.log(err);
          }
        });
        await ContentBlog.findByIdAndDelete(idBlog);
        await Blog.findByIdAndDelete(idBlog);
        await User.findByIdAndUpdate(user._id, { $pull: { blog: idBlog } });
        return res.send(customResponse(blogFound, "Xóa bài viết thành công"));
      }
      customError("Bài viết không tồn tại");
    }
    customError("Bạn thể xóa bài viết của người khác");
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
      const resMsg = notification
        ? "Đã thích bài viết"
        : "Đã bỏ thích bài viết";
      return res.send(customResponse(blogFound, resMsg));
    }

    customError("Bài viết không tồn tại");
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
    return res.send(customResponse(blogList));
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
    const contentBlog = await ContentBlog.findById(idBlog);
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
    await ContentBlog.findByIdAndUpdate(idBlog, { detail_list: tempList });
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
    return res.send(customResponse(contentBlog, "Đã cập nhật bài viết"));
  } catch (error) {
    next(error);
  }
};

export { createBlog, deleteBlog, likeBlog, getBlogListByUser, updateBlog };
