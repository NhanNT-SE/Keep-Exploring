const Blog = require("../Models/Blog");
const Blog_Detail = require("../Models/Blog_Detail");

const fs = require("fs");
const Notification = require("../Models/Notification");
const { createNotification } = require("./NotificationController");

const createBlog = async (req, res, next) => {
  try {
    const { title, content_list } = req.body;

    const user = req.user;
    const files = req.files;
    const content_list_json = JSON.parse(content_list);
    const blog = new Blog({
      title,
      owner: user._id,
    });
    blog.blog_detail = blog._id;
    await blog.save();

    const _id = blog._id;
    const len = files.length;
    const detail_list = [];

    // Gán img+content vào mảng detail_list
    for (let i = 0; i < len; i++) {
      let detail = {};
      detail.img = files[i].filename;
      detail.content = content_list_json[i];
      detail_list.push(detail);
    }

    const blog_detail = new Blog_Detail({
      _id,
      detail_list,
    });
    await blog_detail.save();
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
    const len = detailFound.detail_list.length;

    if (user.role !== "admin" && user._id !== blogFound.owner) {
      return handlerCustomError(201, "Bạn không phải admin/owner bài viết này");
    }

    if (blogFound && detailFound) {
      for (let i = 0; i < len; i++) {
        fs.unlinkSync(
          "src/public/images/blog/" + detailFound.detail_list[i].img
        );
      }
      await Blog_Detail.findByIdAndDelete(idBlog);
      await Blog.findByIdAndDelete(idBlog);
      return res.status(200).send({
        status: 200,
        data: null,
        message: "Xóa bài viết thành công",
      });
    }
    // return res.status(201).send("Bai Blog khong ton tai");
    handlerCustomError(201, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const getAll = async (req, res, next) => {
  try {
    //Kiem tra role nguoi dung
    const user = req.user;
    const role = user.role;

    //Neu la admin thi co quyen xem tat ca bai viet
    if (role == "admin") {
      const blogList = await Blog.find({}).populate("owner", [
        "displayName",
        "imgUser",
      ]);
      return res.status(200).send({
        data: blogList,
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }

    //Khong phai admin thi chi xem nhung bai viet co status la done
    const blogList_done = await Blog.find({ status: "done" });
    return res.status(200).send(blogList_done);
  } catch (error) {
    next(error);
  }
};

const getBlogbyID = async (req, res, next) => {
  try {
    const { idBlog } = req.params;

    const blogFound = await Blog.findById(idBlog)
      .populate("blog_detail")
      .populate({path:"comment",populate:{path:"idUser"}})
      .populate("like_list");

    if (blogFound) {
      return res.send({ data: blogFound, status: 200, message: "" });
    }

    return handlerCustomError(201, "Bài viết không tồn tại");
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
    // return res.status(202).send("Bai viet khong ton tai");
    handlerCustomError(202, "Bài viết không tồn tại");
  } catch (error) {
    next(error);
  }
};

const updateStatus = async (req, res, next) => {
  try {
    const { idBlog, status } = req.body;
    //Kiem tra role cua nguoi dung, chi co admin moi duoc update status
    const { role } = req.user;

    if (role === "admin") {
      //Kiem tra co ton tai bai viet
      const blogFound = await Blog.findById(idBlog);

      //Neu ton tai thi admin cap nhat status,
      if (blogFound) {
        blogFound.status = status;
        await Blog.findByIdAndUpdate(idBlog, blogFound);

        //Tao notify
        const notify = new Notification({
          idUser: blogFound.owner.toString(),
          idPost: idBlog,
          status: "new",
        });
        if (status == "done") {
          notify.content = "moderated";
        } else {
          notify.content = "unmoderated";
        }
        await createNotification(notify);

        return res.status(200).send({
          status: 200,
          data: null,
          message: "Cập nhật bài viết thành công",
        });
      }

      //Neu khong ton tai blog se tra ve client status code la 201
      return handlerCustomError(201, "Bài viết không tồn tại");
    }

    //Khi role nguoi dung khong phai admin thi tra ve status code la 202
    return handlerCustomError(202, "Bạn không có quyền cập nhật status blog");
  } catch (error) {
    next(error);
  }
};

const handlerCustomError = (status, message) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  throw err;
};
module.exports = {
  createBlog,
  deleteBlog,
  getAll,
  getBlogbyID,
  likeBlog,
  updateStatus,
};
