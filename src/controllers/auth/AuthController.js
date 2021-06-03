import bcrypt from "bcryptjs";
import fs from "fs";
import { generateToken, verifyToken } from "../../helpers/JWTHelper.js";
import { User } from "../../models/User.js";
import { UserInfo } from "../../models/UserInfo.js";
import { Token } from "../../models/Token.js";
import { MFA } from "../../models/MFA.js";
import { Notification } from "../../models/Notification.js";
import { sendNotifyRealtime } from "../../helpers/SocketHelper.js";
import { customError } from "../../helpers/CustomError.js";
import lodash from "lodash";
import * as mfaHelper from "../../helpers/MFAHelper.js";
import {
  ACCESS_TOKEN_SECRET,
  REFRESH_TOKEN_SECRET,
  ACCESS_TOKEN_LIFE,
  REFRESH_TOKEN_LIFE,
} from "../../config/index.js";

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
        const userInfo = await UserInfo.findOne({ owner: user._id }).session(
          session
        );
        let userStatus = {};
        let infoUpdate = {};
        if (
          userInfo.onlineStatus === "idle" ||
          userInfo.accountStatus === "inactive"
        ) {
          if (userInfo.onlineStatus === "idle") {
            userStatus.onlineStatus = "online";
          }
          if (userInfo.accountStatus === "inactive") {
            userStatus.accountStatus = "active";
            await new Notification({
              idUser: user._id,
              contentAdmin:
                "Tài khoản của bạn đã được kích hoạt, cảm ơn bạn đã sử dụng hệ thống của chúng tôi",
              status: "new",
            }).save({ session });
          }
          infoUpdate = await UserInfo.findByIdAndUpdate(
            userInfo._id,
            {
              ...userStatus,
            },
            opts
          );
        } else {
          infoUpdate = userInfo;
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
              "displayName",
              "avatar",
              "role",
            ]),
            ...lodash.pick(infoUpdate, ["accountStatus", "onlineStatus"]),
          },
        };
        await session.commitTransaction();
        session.endSession();
        return res.status(200).send({
          data,
          status: 200,
          message: "Đăng nhập thành công",
        });
      }
      customError(500, "Mật khẩu của bạn không đúng");
    }
    customError(500, "Người dùng không tồn tại");
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
      const data = await Token.findOneAndUpdate(
        { owner: userId },
        { accessToken: null, refreshToken: null },
        { returnOriginal: false }
      );
      return res.status(200).send({
        data,
        status: 200,
        message: "Đăng xuất thành công",
      });
    }
    return customError(500, "Không tồn tại người dùng này trong hệ thống");
  } catch (error) {
    next(error);
  }
};
const signUp = async (req, res, next) => {
  const { file, session, opts } = req;
  session.startTransaction();
  try {
    let avatar;
    if (file) {
      avatar = file.filename;
    } else {
      avatar = "avatar-default.png";
    }
    const salt = await bcrypt.genSalt(10);
    const passHashed = await bcrypt.hash(req.body.password, salt);
    const user = await new User({
      ...req.body,
      avatar,
      password: passHashed,
      role: "user",
    }).save({ session });
    const owner = user._id;
    await new Token({ owner }).save({ session });
    const mfa = await new MFA({ owner }).save({ session });
    const userInfo = await new UserInfo({ owner }).save({ session });
    await User.findByIdAndUpdate(
      owner,
      { userInfo: userInfo._id, mfa: mfa._id },
      opts
    );
    await session.commitTransaction();
    session.endSession();
    return res.status(200).send({
      data: userInfo,
      message: "Đăng ký thành công",
      status: 200,
    });
  } catch (error) {
    await session.abortTransaction();
    session.endSession();
    next(error);
  }
};

const refreshToken = async (req, res, next) => {
  try {
    const { refreshToken, userId } = req.body;

    const decoded = await verifyToken(refreshToken, REFRESH_TOKEN_SECRET);
    const token = await Token.findById(userId);
    if (decoded._id === userId) {
      if (token && token.refreshToken === refreshToken) {
        const userData = decoded;
        const accessToken = await generateToken(
          userData,
          ACCESS_TOKEN_SECRET,
          ACCESS_TOKEN_LIFE
        );
        token.accessToken = accessToken;
        await token.save();
        return res.status(200).send({
          data: accessToken,
          status: 200,
          message: "Refresh token thành công",
        });
      }
      customError(401, "Refresh Token của bạn không hợp lệ");
    }
    customError(
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
      customError(201, "Email của bạn không đúng");
    }
    if (user.displayName !== displayName) {
      customError(201, "Tên hiển thị của bạn không đúng");
    }
    if (user.bod.toString() != new Date(bod)) {
      customError(201, "Ngày sinh của bạn không đúng");
    }

    return res.status(200).send({
      data: "Xác nhận thành công",
      status: 200,
      message: "Lấy mật khẩu thành công",
    });
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
    await User.findByIdAndUpdate(user._id, { pass: passHashed });
    return res.status(200).send({
      data: "Đã đổi mật khẩu thành công",
      status: 200,
      message: "Lấy mật khẩu thành công",
    });
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
};
