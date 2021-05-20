import "../../models/Blog.js";
import "../../models/Post.js";
import handlerCustomError from "../../helpers/CustomError.js";
import Notification from "../../models/Notification.js";
import User from "../../models/User.js";

const changeNewStatusNotify = async (req, res, next) => {
  try {
    const { idNotify } = req.params;
    const notifyFound = await Notification.findById(idNotify);
    if (!notifyFound) {
      handlerCustomError(201, "Thông báo không tồn tại");
    }
    await await Notification.findByIdAndUpdate(idNotify, { status: "new" });
    return res.send({
      data: notifyFound,
      status: 200,
      message: "Đã đổi trạng thái thông báo",
    });
  } catch (error) {
    next(error);
  }
};
const changeSeenStatusNotify = async (req, res, next) => {
  try {
    const idUser = req.user._id;
    await Notification.updateMany(
      { idUser, status: "new" },
      { status: "seen" }
    );
    return res.status(200).send({
      data: {},
      status: 200,
      message: "Cập nhật trạng thái thành công",
    });
  } catch (error) {
    next(error);
  }
};
const deleteNotifyById = async (req, res, next) => {
  try {
    const { idNotify } = req.params;
    const user = await User.findById(req.user._id);
    const notifyFound = await Notification.findById(idNotify);
    if (!notifyFound) {
      return handlerCustomError(201, "Thông báo không tồn tại hoặc đã bị xóa");
    }
    if (user.role === "admin" || user._id == notifyFound.idUser.toString()) {
      await Notification.findByIdAndDelete(idNotify);
      return res.send({
        data: { idNotify },
        status: 200,
        message: "Đã xóa thông báo",
      });
    }
    return handlerCustomError(202, "Bạn không thể xóa thông báo này");
  } catch (error) {
    next(error);
  }
};
const getAllByUser = async (req, res, next) => {
  try {
    const user = req.user;
    const notifyList = (resultList = await Notification.find({
      idUser: user._id,
    })
      .populate("idPost", ["status", "imgs", "title"])
      .populate("idBlog", ["status", "img", "title"])
      .sort({ created_on: -1 }));

    return res.send({
      data: notifyList,
      status: 200,
      message: "Lấy dữ liệu thành công",
    });
  } catch (error) {
    next(error);
  }
};
export {
  deleteNotifyById,
  changeNewStatusNotify,
  changeSeenStatusNotify,
  getAllByUser,
};
