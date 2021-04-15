require("../Models/Blog");
require("../Models/Post");

const handlerCustomError = require("../middleware/customError");
const Notification = require("../Models/Notification");
const User = require("../Models/User");

const createNotification = async (notify) => {
  try {
    const notiFound_list = await Notification.find({ idPost: notify.idPost });

    if (notiFound_list) {
      let notiUpdate = null;
      notiFound_list.forEach((item) => {
        if (item.content == notify.content) {
          item.status = "new";
          notiUpdate = item;
          return;
        }
      });

      if (notiUpdate) {
        await notiUpdate.save();
        return notiUpdate;
      } else {
        await notify.save();
        return notify;
      }
    } else {
      await notify.save();
      return notify;
    }
  } catch (error) {
    return error;
  }
};
const changeNewStatusNotify = async (req, res, next) => {
  try {
    const { idNotify } = req.params;
    const notiFound = await Notification.findById(idNotify);
    if (!notiFound) {
      handlerCustomError(201, "Thông báo không tồn tại");
    }
    await await Notification.findByIdAndUpdate(idNotify, { status: "new" });
    return res.send({
      data: notiFound,
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
    const notiFound = await Notification.findById(idNotify);
    if (!notiFound) {
      return handlerCustomError(201, "Thông báo không tồn tại hoặc đã bị xóa");
    }
    if (user.role === "admin" || user._id == notiFound.idUser.toString()) {
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

const deleteAllNotify = async (req, res, next) => {
  try {
    const idUser = req.user._id;
    await Notification.deleteMany({ idUser });
    return res.send({
      data: {},
      status: 200,
      message: "Đã xóa tất cả thông báo",
    });
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
      .populate("idPost",["status","imgs","title"])
      .populate("idBlog",["status","img","title"])
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

module.exports = {
  createNotification,
  changeSeenStatusNotify,
  deleteAllNotify,
  deleteNotifyById,
  getAllByUser,
  changeNewStatusNotify,
};
