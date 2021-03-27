const handlerCustomError = require("../middleware/customError");
const Notification = require("../Models/Notification");

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
    const { idNoti } = req.body;
    const notiFound = await Notification.findById(idNoti);

    if (!notiFound) {
      handleCustomError(201, "Thông báo không tồn tại");
    }

    notiFound.status = "new";
    await notiFound.save();
    return res.send({
      data: null,
      status: 200,
      message: "Đã đánh dấu thành thông báo chưa đọc",
    });
  } catch (error) {
    next(error);
  }
};

const changeSeenStatusNotify = async (req, res, next) => {
  try {
    const idUser = req.user._id;
    const newNoti_list = await Notification.find({
      status: "new",
      idUser: idUser,
    });

    if (!newNoti_list) {
      handleCustomError(201, "Bạn đã xem hết thông báo mới");
    }

    newNoti_list.forEach(async (item) => {
      item.status = "seen";
      await item.save();
    });

    return res.send(newNoti_list);
  } catch (error) {
    next(error);
  }
};

const deleteNotify = async (req, res, next) => {
  try {
    const { idNoti } = req.params;
    const user = req.user;
    const notiFound = await Notification.findById(idNoti);
    if (!notiFound) {
      return handleCustomError(201, "Thông báo không tồn tại hoặc đã bị xóa");
    }
    if (user.role === "admin" || user._id == notiFound.idUser.toString()) {
      await Notification.findByIdAndDelete(idNoti);
      return res.send({ data: null, status: 200, message: "Đã xóa thông báo" });
    }
    return handleCustomError(202, "Bạn không có quyền xóa thông báo này");
  } catch (error) {
    next(error);
  }
};

const getAllByUser = async (req, res, next) => {
  try {
    const user = req.user;
    const { status } = req.query;
    const notification_list = await Notification.find({
      idUser: user._id,
    }).sort({ created_on: -1 });
    if (!notification_list) {
      handlerCustomError(201, "Bạn chưa có thông báo nào");
    }

    if (status) {
      const resultList = notification_list.filter((item) => {
        return item.status == status;
      });
      return res.send({
        data: { resultList },
        status: 200,
        message: "List thông báo đã lọc",
      });
    }

    return res.send({
      data: { notification_list },
      status: 200,
      message: "List tất cả thông báo",
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  createNotification,
  changeNewStatusNotify,
  changeSeenStatusNotify,
  deleteNotify,
  getAllByUser,
};
