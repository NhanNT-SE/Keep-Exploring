const Blog = require("../Models/Blog");
const Blog_Detail = require("../Models/Blog_Detail");
const fs = require("fs");
const Notification = require("../Models/Notification");
const { createNotification } = require("./NotificationController");
const User = require("../Models/User");
const handlerCustomError = require("../middleware/customError");

const createBlog = async (req, res, next) => {
  try {
    // const files = req.files;
    // const content_list_json = JSON.parse(content_list);
    // const blog = new Blog({
    //   title,
    //   owner: user._id,
    // });
    // blog.blog_detail = blog._id;
    // await blog.save();

    // const _id = blog._id;
    // const len = files.length;
    // const detail_list = [];

    // Gán img+content vào mảng detail_list
    // for (let i = 0; i < len; i++) {
    //   let detail = {};
    //   detail.img = files[i].filename;
    //   detail.content = content_list_json[i];
    //   detail_list.push(detail);
    // }

    // const blog_detail = new Blog_Detail({
    //   _id,
    //   detail_list,
    // });
    // // await blog_detail.save();

    // //Add post vào list post của user
    // await user.blog.push(_id);
    // // await user.save();

    // const blogSaved = await Blog.findById(_id)
    //   .populate("owner")
    //   .populate("blog_detail");

    const {title, detail_list } = req.body;
    const file = req.file;
    const user = await User.findById(req.user._id);
    const blog = new Blog({ title });
    const blog_detail = new Blog_Detail({});

    if (file) {
      blog.img = file.filename;
    }

    blog_detail.detail_list = [...detail_list];
    blog_detail._id = blog._id;

    blog.owner = user._id;
    blog.blog_detail = blog._id;

    user.blog.push(blog._id);

    return res.status(200).send({
      status: 200,
      data: { blog, blog_detail, user },
      message: "Tạo bài viết thành công",
    });
  } catch (error) {
    next(error);
  }
};
const deleteBlog = async (req, res, next) => {
  try {
    console.log("abc");
    const user = req.user;
    const { idBlog } = req.params;

    //Kiem tra xem bai viet co ton tai khong
    const blogFound = await Blog.findById(idBlog);
    const detailFound = await Blog_Detail.findById(idBlog);
    const len = detailFound.detail_list.length;

    if (user.role === "admin" || user._id == blogFound.owner.toString()) {
      if (blogFound && detailFound) {
        for (let i = 0; i < len; i++) {
          fs.unlinkSync(
            "src/public/images/blog/" + detailFound.detail_list[i].img
          );
        }
        await Blog_Detail.findByIdAndDelete(idBlog);
        await Blog.findByIdAndDelete(idBlog);

        //Xoa bai blog khoi bloglist cua user
        await User.findByIdAndUpdate(user._id, { $pull: { blog: idBlog } });

        return res.status(200).send({
          status: 200,
          data: null,
          message: "Xóa bài viết thành công",
        });
      }
      handlerCustomError(201, "Bài viết không tồn tại");
    }

    handlerCustomError(201, "Bạn không phải admin/owner bài viết này");
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

module.exports = {
  createBlog,
  deleteBlog,
  likeBlog,
};
