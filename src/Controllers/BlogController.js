const Blog = require("../Models/Blog");
const Blog_Detail = require("../Models/Blog_Detail");
const fs = require("fs");
const Notification = require("../Models/Notification");
const { createNotification } = require("./NotificationController");
const User = require("../Models/User");
const handlerCustomError = require("../middleware/customError");

const createBlog = async (req, res, next) => {
  try {
    let { title, detail_list, folder_storage } = req.body;
    const file = req.file;
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

    //Kiem tra xem bai viet co ton tai khong
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
        //Xoa bai blog khoi bloglist cua user
        await User.findByIdAndUpdate(user._id, { $pull: { blog: idBlog } });
        return res.status(200).send({
          status: 200,
          data: blogFound,
          message: "Xóa bài viết thành công",
        });
      }
      handlerCustomError(201, "Bài viết không tồn tại");
    }
    handlerCustomError(201, "Bạn thể xóa bài viết của người khác");
  } catch (error) {
    next(error);
  }
};
const likeBlog = async (req, res, next) => {
  try {
    //Lay id bai viet va id nguoi dung tu req
    const { idBlog } = req.body;
    const user = req.user;

    //Kiem tra bai viet co ton tai hay khong
    const blogFound = await Blog.findById(idBlog);
    if (blogFound) {
      //Kiem tra nguoi dung da like bai viet hay chua
      var i = 0;
      var like_list = blogFound.like_list;
      const len = like_list.length;

      for (i; i < len; i++) {
        //Neu nguoi dung da like bai viet thi doi thanh dislike- remove idUser khoi like_list va return status code 200
        if ((user._id = like_list[i])) {
          await blogFound.like_list.splice(i, 1);
          await blogFound.save();
          return res.status(200).send({
            status: 200,
            message: "Đã bỏ like bài viết",
            data: null,
          });
        }
      }
      //Con neu nguoi dung chua like bai viet thi push idUser vao like_list va return status code 201
      await blogFound.like_list.push(user._id);
      await blogFound.save();

      //Tao notify khi co nguoi like bai viet
      const notify = new Notification({
        idUser: blogFound.owner.toString(),
        idPost: idBlog,
        status: "new",
        content: "like",
      });
      const notification = await createNotification(notify);

      return res.send({
        status: 201,
        data: notification,
        message: "Đã like bài viết",
      });
    }

    //Neu bai viet khong ton tai thi tra ve res code 202
    handlerCustomError(202, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};
const updateBlog = async (req, res, next) => {
  try {
    const { title, created_on, detail_list } = req.body;
    const { idBlog } = req.params;
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
    return res.status(200).send({
      status: 200,
      data: blog_detail,
      message: "Đã cập nhật bài viết",
    });
  } catch (error) {
    console.log(error);
    next(error);
  }
};

module.exports = {
  createBlog,
  deleteBlog,
  likeBlog,
  updateBlog,
};
