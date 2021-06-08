import bcrypt from "bcryptjs";
import fs from "fs";
import { User } from "../../models/User.js";
import { MFA } from "../../models/MFA.js";
import { Notification } from "../../models/Notification.js";
import { BasicInfo } from "../../models/BasicInfo.js";
import { AdvancedInfo } from "../../models/AdvancedInfo.js";
import { customError } from "../../helpers/CustomError.js";
import { createNotification } from "../../helpers/NotifyHelper.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customResponse } from "../../helpers/CustomResponse.js";
import { bucket, pathImage, urlImage } from "../../helpers/Storage.js";
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
    const profile = await User.findById(user._id, "username email role -_id")
      .populate("basicInfo", "-_id -__v -owner")
      .populate("advancedInfo", "-owner -__v -_id");
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
    let avatar;
    if (file) {
      const dirUpload = `avatar/${user._id}/`;
      const blob = bucket.file(`${dirUpload}` + file);
      const path = pathImage(`${dirUpload}`, file);
      blob.name = path;
      const blobStream = blob.createWriteStream();
      blobStream.on("error", (err) => {
        next(err);
      });
      blobStream.end(file.buffer);
      avatar = urlImage(path);
    }
    const profileUpdate = await BasicInfo.findOneAndUpdate(
      { owner: user._id },
      { ...req.body, avatar },
      { returnOriginal: false, fields: { _id: 0 ,__v:0, owner:0} }
    );
    const data = customResponse(
      profileUpdate,
      "Cập nhật thông tin cá nhân thành công"
    );
    return res.send(data);
  } catch (error) {
    next(error);
  }
};

export { changePass, getAnotherProfile, getMyProfile, updateProfile };
