const Blog = require("../../Models/Blog");
const customError = require("../../helpers/customError");
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

const deleteBlog = async (req, res, next) => {
  try {
    const { idBlog } = req.params;
    const blog = await Blog.findById(idBlog);
    if (blog) {
      await Blog.findByIdAndDelete(idBlog);
      return res.status(200).send({
        data: blog,
        status: 200,
      });
    }
    customError(500, "Bai viet khong ton tai");
  } catch (error) {
    next(error);
  }
};
const updateStatus = async (req, res, next) => {
  try {
    const { idBlog, status } = req.body;
    const blog = await Blog.findById(idBlog);
    const { io } = req;
    if (blog) {
      const blogUpdate = {
        status,
        view_mode: "public",
      };
      await Blog.findByIdAndUpdate(idBlog, blogUpdate);
      return res.send({
        data: blog,
        status: 200,
        message: "Cập nhật bài viết thành công",
      });
    }
    return customError(201, "Bài viết không tồn tại");
  } catch (error) {
    console.log(error);
    next(error);
  }
};
// const updateStatus = async (req, res, next) => {
//     try {
//       const { idUpdate, status, type } = req.body;
//       let updateFound = null;
//       let notify = null;
//       let msgNotify;
//       let notifyClient = {};
//       const { io } = req;
//       if (type === "post") {
//         updateFound = await Post.findById(idUpdate);
//       } else {
//         updateFound = await Blog.findById(idUpdate);
//       }
//       if (updateFound) {
//         updateFound.status = status;
//         if (type === "post") {
//           await Post.findByIdAndUpdate(idUpdate, updateFound);
//           notify = {
//             idUser: updateFound.owner.toString(),
//             idPost: idUpdate,
//             status: "new",
//             statusPost: status,
//           };
//           notifyClient.idPost = idUpdate;
//         } else {
//           await Blog.findByIdAndUpdate(idUpdate, updateFound);
//           notify = {
//             idUser: updateFound.owner.toString(),
//             idBlog: idUpdate,
//             status: "new",
//             statusBlog: status,
//           };
//           notifyClient.idBlog = idUpdate;
//         }

//         if (status === "done") {
//           notify.content = "moderated";
//           msgNotify = `Bài viết ${updateFound.title} của bạn đã được kiểm duyệt, và đang được hiển thị với mọi người`;
//         } else {
//           notify.content = "unmoderated";
//           if (status === "pending") {
//             msgNotify = `Bài viết ${updateFound.title} của bạn hiện đang trong quá trình kiểm duyệt`;
//           } else {
//             msgNotify = `Bài viết ${updateFound.title} của bạn cần được chỉnh sủa`;
//           }
//         }
//         const notification = await createNotification(notify);

//         notifyClient.message = msgNotify;
//         notifyClient.type = status;
//         sendNotifyRealtime(io, updateFound.owner, notifyClient);
//         return res.send({
//           data: notification,
//           status: 200,
//           message: "Cập nhật bài viết thành công",
//         });
//       }
//       return handlerCustomError(201, "Bài viết không tồn tại");
//     } catch (error) {
//       console.log(error);
//       next(error);
//     }
//   };
module.exports = {
  getAllBlog,
  updateStatus,
  deleteBlog
};
