import bcrypt from "bcryptjs";
import fs from "fs";
import { generateToken, verifyToken } from "../../helpers/JWTHelper.js";
import { User } from "../../models/User.js";
import { Token } from "../../models/Token.js";
import { MFA } from "../../models/MFA.js";
import { Notification } from "../../models/Notification.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customError, errorAuth } from "../../helpers/CustomError.js";
import lodash from "lodash";
import {
  ACCESS_TOKEN_SECRET,
  REFRESH_TOKEN_SECRET,
  ACCESS_TOKEN_LIFE,
  REFRESH_TOKEN_LIFE,
} from "../../config/index.js";
import { customResponse } from "../../helpers/CustomResponse.js";
import { verifyOTPToken } from "../../helpers/MFAHelper.js";
import { bucket, pathImage, urlImage } from "../../helpers/Storage.js";
import { AdvancedInfo } from "../../models/AdvancedInfo.js";
import { BasicInfo } from "../../models/BasicInfo.js";

const signIn = async (req, res, next) => {
  const { session, opts } = req;
  session.startTransaction();
  try {
    const { username, password } = req.body;
    const user =
      (await User.findOne({ email: username })) ||
      (await User.findOne({ username }));
    if (user) {
      const checkPass = await bcrypt.compare(password, user.password);
      if (checkPass) {
        const accessToken = await generateToken(
          user,
          ACCESS_TOKEN_SECRET,
          ACCESS_TOKEN_LIFE
        );
        const refreshToken = await generateToken(
          user,
          REFRESH_TOKEN_SECRET,
          REFRESH_TOKEN_LIFE
        );
        const mfa = await MFA.findOne({ owner: user._id }).session(session);
        const advancedInfo = await AdvancedInfo.findOne({
          owner: user._id,
        }).session(session);
        let userStatus = {};
        let infoUpdate = {};
        if (
          advancedInfo.onlineStatus === "idle" ||
          advancedInfo.accountStatus === "inactive"
        ) {
          if (advancedInfo.onlineStatus === "idle") {
            userStatus.onlineStatus = "online";
          }
          if (advancedInfo.accountStatus === "inactive") {
            userStatus.accountStatus = "active";
            await new Notification({
              idUser: user._id,
              contentAdmin:
                "Tài khoản của bạn đã được kích hoạt, cảm ơn bạn đã sử dụng hệ thống của chúng tôi",
              status: "new",
            }).save({ session });
          }
          infoUpdate = await advancedInfo.updateOne({ ...userStatus }, opts);
        } else {
          infoUpdate = advancedInfo;
        }
        await Token.findOneAndUpdate(
          { owner: user._id },
          { accessToken, refreshToken },
          opts
        );
        const data = {
          accessToken,
          refreshToken,
          user: {
            mfa: mfa.status,
            ...lodash.pick(user, [
              "_id",
              "username",
              "email",
              "fullName",
              "avatar",
              "role",
            ]),
            ...lodash.pick(infoUpdate, ["accountStatus", "onlineStatus"]),
          },
        };
        await session.commitTransaction();
        session.endSession();
        return res.send(customResponse(data, "Đăng nhập thành công"));
      }
      customError("Mật khẩu của bạn không đúng");
    }
    customError("Người dùng không tồn tại");
  } catch (error) {
    await session.abortTransaction();
    session.endSession();
    next(error);
  }
};
const signOut = async (req, res, next) => {
  try {
    const { userId } = req.body;
    const user = await User.findById(userId);
    if (user) {
      await Token.findOneAndUpdate(
        { owner: userId },
        { $unset: { accessToken: 1, refreshToken: 1 } },
        { returnOriginal: false }
      );
      return res.send(customResponse(null, "Đăng xuất thành công"));
    }
    return customError("Không tồn tại người dùng này trong hệ thống");
  } catch (error) {
    next(error);
  }
};
const signUp = async (req, res, next) => {
  const { file, session, opts } = req;
  session.startTransaction();
  try {
    let avatar;

    const salt = await bcrypt.genSalt(10);
    const passHashed = await bcrypt.hash(req.body.password, salt);
    const user = await new User({
      ...req.body,
      password: passHashed,
      role: "user",
    }).save({ session });
    const owner = user._id;
    if (file) {
      const dirUpload = `avatar/${user._id}/`;
      const blob = bucket.file(`${dirUpload}` + file);
      const path = pathImage(`${dirUpload}`, file);
      blob.name = path;
      const blobStream = blob.createWriteStream();
      blobStream.on("error", (err) => {
        next(err);
      });
      blobStream.end(req.file.buffer);
      avatar = urlImage(path);
    }
    await new Token({ owner }).save({ session });
    const mfa = await new MFA({ owner }).save({ session });
    const basicInfo = await new BasicInfo({
      owner,
      avatar,
      fullName: req.body.fullName,
    }).save({ session });
    const advancedInfo = await new AdvancedInfo({ owner }).save({ session });
    await user.updateOne({ basicInfo, mfa, advancedInfo }, opts);
    await session.commitTransaction();
    session.endSession();

    const data = {
      username: user.username,
      email: user.email,
      fulName: basicInfo.fullName,
      avatar: basicInfo.avatar,
      role: user.role,
      created_on: user.created_on,
    };
    return res.send(customResponse(data, "Đăng ký thành công"));
  } catch (error) {
    await session.abortTransaction();
    session.endSession();
    next(error);
  }
};
const verifyOTPSignIn = async (req, res, next) => {
  try {
    const { otp, userId } = req.body;
    const mfa = await MFA.findOne({ owner: userId });
    if (mfa.secretMFA) {
      const isValid = verifyOTPToken(otp, mfa.secretMFA);
      if (isValid) {
        return res.send(
          customResponse({ isValid }, "Verify OTP code successfully")
        );
      }
      customError("Oops...Your OTP code is not correct!");
    }
    customError("Your MFA not enabled");
  } catch (error) {
    next(error);
  }
};
const refreshToken = async (req, res, next) => {
  try {
    const { refreshToken, userId } = req.body;

    const decoded = await verifyToken(refreshToken, REFRESH_TOKEN_SECRET);
    const token = await Token.findOne({ owner: userId });
    if (decoded._id === userId) {
      if (token && token.refreshToken === refreshToken) {
        const userData = decoded;
        const accessToken = await generateToken(
          userData,
          ACCESS_TOKEN_SECRET,
          ACCESS_TOKEN_LIFE
        );

        await token.updateOne({ accessToken });
        return res.send(
          customResponse(accessToken, "Refresh token thành công")
        );
      }
      errorAuth(401, "Refresh Token của bạn không hợp lệ");
    }
    errorAuth(
      401,
      "Refresh token này không phải của bạn, vui lòng sử dụng refresh token của mình để tiếp tục"
    );
  } catch (error) {
    next(error);
  }
};
const forgetPassword = async (req, res, next) => {
  try {
    const { bod, email, displayName } = req.body;
    const user = await User.findOne({ email });

    if (!user) {
      customError("Email của bạn không đúng");
    }
    if (user.displayName !== displayName) {
      customError("Tên hiển thị của bạn không đúng");
    }
    if (user.bod.toString() != new Date(bod)) {
      customError("Ngày sinh của bạn không đúng");
    }
    return res.send(
      customResponse("Xác nhận thành công", "Lấy mật khẩu thành công")
    );
  } catch (error) {
    next(error);
  }
};
const getNewPassword = async (req, res, next) => {
  try {
    const { email, newPass } = req.body;
    const user = await User.findOne({ email });
    const salt = await bcrypt.genSalt(10);
    const passHashed = await bcrypt.hash(newPass, salt);
    await user.updateOne({ password: passHashed });
    return res.send(
      customResponse("Đã đổi mật khẩu thành công", "Đã đổi mật khẩu thành công")
    );
  } catch (error) {
    next(error);
  }
};
export {
  signIn,
  signOut,
  signUp,
  refreshToken,
  forgetPassword,
  getNewPassword,
  verifyOTPSignIn,
};
