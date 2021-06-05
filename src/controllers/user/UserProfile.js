import bcrypt from "bcryptjs";
import fs from "fs";
import { User } from "../../models/User.js";
import { MFA } from "../../models/MFA.js";
import { Notification } from "../../models/Notification.js";
import { customError } from "../../helpers/CustomError.js";
import { createNotification } from "../../helpers/NotifyHelper.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customResponse } from "../../helpers/CustomResponse.js";
const changePass = async (req, res, next) => {
  try {
    const { oldPass, newPass } = req.body;
    const { io } = req;
    const user = await User.findById(req.user._id);
    const checkPass = await bcrypt.compare(oldPass, user.pass);
    if (checkPass) {
      const salt = await bcrypt.genSalt(10);
      const passHashed = await bcrypt.hash(newPass, salt);
      await User.findByIdAndUpdate(user._id, { pass: passHashed });
      const msgNotify = `Mật khẩu của bạn đã được cập nhật`;

      const notify = new Notification({
        idUser: user._id,
        contentAdmin: msgNotify,
        status: "new",
      });

      await notify.save();
      sendNotifyRealtime(io, user._id, {
        message: msgNotify,
        type: "system",
        content: "Mật khẩu của bạn đã được cập nhật",
      });
      return res.send(customResponse({}, "Đổi mật khẩu thành công"));
    }
    customError("Mật khẩu của bạn không đúng");
  } catch (error) {
    next(error);
  }
};

const getAnotherProfile = async (req, res, next) => {
  try {
    const { idUser } = req.params;
    const user = await User.one({ _id: idUser, role: "user" });
    if (user) {
      return res.send(customResponse(user));
    }
    return customError("Người dùng không tồn tại hoặc đã bị xóa");
  } catch (error) {
    next(error);
  }
};
const getMyProfile = async (req, res, next) => {
  try {
    const { user } = req;
    const profile = await User.findById(user._id, { password: 0 }).populate(
      "userInfo"
    );
    const mfa = await MFA.findOne({ owner: user._id });
    const data = JSON.parse(JSON.stringify(profile));
    return res.send(customResponse({ ...data, mfa: mfa.status }));
  } catch (error) {
    next(error);
  }
};

const updateProfile = async (req, res, next) => {
  try {
    const file = req.file;
    const { user } = req;
    const { io } = req;
    const userFound = await User.findById(user._id);
    let avatar;

    if (!userFound) {
      customError("Người dùng không tồn tại");
    }

    if (file) {
      if (userFound.imgUser !== "avatar-default.png") {
        fs.unlink("src/public/images/user/" + userFound.avatar, (err) => {
          if (err) {
            console.log(err);
          }
        });
      }
      avatar = file.filename;
    } else {
      avatar = userFound.avatar;
    }

    const newUser = {
      ...req.body,
      avatar,
    };

    await User.findByIdAndUpdate(user._id, newUser);
    const msgNotify = `Thông tin cá nhân của bạn đã được cập nhật`;

    const notify = new Notification({
      idUser: user._id,
      contentAdmin: msgNotify,
      status: "new",
    });

    await notify.save();
    sendNotifyRealtime(io, user._id, {
      message: msgNotify,
      type: "system",
    });
    const data = customResponse(
      {
        _id: user._id,
        email: user.email,
        ...newUser,
      },
      "Cập nhật thông tin cá nhân thành công"
    );
    return res.send(data);
  } catch (error) {
    next(error);
  }
};

export { changePass, getAnotherProfile, getMyProfile, updateProfile };
