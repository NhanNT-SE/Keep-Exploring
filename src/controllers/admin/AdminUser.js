import fs from "fs";
import {User} from "../../models/User.js";
import {Notification} from "../../models/Notification.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import {customError} from "../../helpers/CustomError.js";

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
const deleteUser = async (req, res, next) => {
  try {
    const { idUser } = req.params;
    const userFound = await User.findById(idUser);
    if (!userFound) {
      return customError(
        202,
        "Người dùng này không tồn tại hoặc đã bị xóa"
      );
    }
    if (userFound.role == "admin") {
      return customError(203, "Bạn không thể xóa tài khoản admin");
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

export { deleteUser, getAllUser, sendNotify };
