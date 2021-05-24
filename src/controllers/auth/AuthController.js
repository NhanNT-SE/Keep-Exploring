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
import {
  ACCESS_TOKEN_SECRET,
  REFRESH_TOKEN_SECRET,
  ACCESS_TOKEN_LIFE,
  REFRESH_TOKEN_LIFE,
} from "../../config/index.js";

const signIn = async (req, res, next) => {
  try {
    const { email, pass } = req.body;
    const user = await User.findOne({ email });
    if (user) {
      const checkPass = await bcrypt.compare(pass, user.pass);
      if (checkPass) {
        let token = await Token.findById(user._id);

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

        if (!token) {
          token = new Token({
            _id: user._id,
            accessToken,
            refreshToken,
          });
        } else {
          token.accessToken = accessToken;
          token.refreshToken = refreshToken;
        }
        await token.save();
        const data = {
          accessToken,
          refreshToken,
          _id: user._id,
          email: user.email,
          role: user.role,
          displayName: user.displayName,
          imgUser: user.imgUser,
        };

        return res.status(200).send({
          data,
          status: 200,
          message: "Đăng nhập thành công",
        });
      }
      customError(201, "Mật khẩu của bạn không đúng");
    }
    customError(201, "Người dùng không tồn tại");
  } catch (error) {
    next(error);
  }
};
const signOut = async (req, res, next) => {
  try {
    const { userId } = req.body;
    const user = await User.findById(userId);
    const token = await Token.findById(userId);
    if (user) {
      token.refreshToken = null;
      token.accessToken = null;
      await token.save();
      return res.send({
        status: 200,
        data: {},
        msg: "Đăng xuất thành công",
      });
    }
    return customError(201, "Không tồn tại người dùng này trong hệ thống");
  } catch (error) {
    next(error);
  }
};
const signUp = async (req, res, next) => {
  const { file, session, opts } = req;
  session.startTransaction();
  try {
    let imgUser;
    if (file) {
      imgUser = file.filename;
    } else {
      imgUser = "avatar-default.png";
    }
    const salt = await bcrypt.genSalt(10);
    const passHashed = await bcrypt.hash(req.body.password, salt);
    const user = await User.create(
      [
        {
          ...req.body,
          imgUser,
          password: passHashed,
          role: "user",
        },
      ],
      opts
    );
    const owner = user[0]._id;
    await Token.create([{ owner }], opts);
    await MFA.create([{ owner }], opts);
    const userInfo = await UserInfo.create([{ owner }], opts);

    await User.findByIdAndUpdate(owner, { userInfo: userInfo[0]._id }, opts);
    // await Notification.create(
    //   [
    //     {
    //       idUser: user[0]._id,
    //       contentAdmin:
    //         "Tài khoản của bạn đã được kích hoạt, cảm ơn bạn đã sử dụng hệ thống của chúng tôi",
    //       status: "new",
    //     },
    //   ],
    //   opts
    // );
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
        return res.send({
          data: accessToken,
          status: 200,
          message: "Refresh token thành công",
        });
      }
      customError(202, "Refresh Token của bạn không hợp lệ");
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