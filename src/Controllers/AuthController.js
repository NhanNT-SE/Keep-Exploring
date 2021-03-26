const jwtHelper = require("../middleware/jwtHelper");
const User = require("../Models/User");
const Token = require("../Models/Token");
const bcrypt = require("bcryptjs");
const {
  ACCESS_TOKEN_SECRET,
  REFRESH_TOKEN_SECRET,
  ACCESS_TOKEN_LIFE,
  REFRESH_TOKEN_LIFE,
} = require("../config/index");
const handlerCustomError = require("../middleware/customError");

const signIn = async (req, res, next) => {
  try {
    const { email, pass } = req.body;
    const user = await User.findOne({ email });
    if (user) {
      const checkPass = await bcrypt.compare(pass, user.pass);
      if (checkPass) {
        let token = await Token.findById(user._id);

        const accessToken = await jwtHelper.generateToken(
          user,
          ACCESS_TOKEN_SECRET,
          ACCESS_TOKEN_LIFE
        );
        const refreshToken = await jwtHelper.generateToken(
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
          imageUser: user.imgUser,
        };

        return res.status(200).send({
          data,
          status: 200,
          message: "Đăng nhập thành công",
        });
      }
      handlerCustomError(201, "Mật khẩu của bạn không đúng");
    }
    handlerCustomError(201, "Người dùng không tồn tại");
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
        data: null,
        msg: "Đăng xuất thành công",
      });
    }
    return handlerCustomError(
      201,
      "Không tồn tại người dùng này trong hệ thống"
    );
  } catch (error) {
    next(error);
  }
};

const signUp = async (req, res, next) => {
  try {
    const file = req.file;
    var imgUser;
    if (file) {
      imgUser = file.filename;
    } else {
      imgUser = "avatar-default.png";
    }
    const user = req.body;

    const userFound = await User.findOne({ email: user.email });
    if (userFound) {
      handlerCustomError(
        201,
        "Email này đã được sử dụng, vui lòng sử dụng email khác"
      );
    }

    const salt = await bcrypt.genSalt(10);
    const passHashed = await bcrypt.hash(user.pass, salt);

    const newUser = new User({
      ...req.body,
      imgUser,
      role: "user",
      pass: passHashed,
    });

    await newUser.save();
    return res.status(200).send({
      data: newUser,
      message: "Đăng ký thành công",
      status: 200,
    });
  } catch (error) {
    next(error);
  }
};
const refreshToken = async (req, res, next) => {
  try {
    const { refreshToken, userId } = req.body;
    const decoded = await jwtHelper.verifyToken(
      refreshToken,
      REFRESH_TOKEN_SECRET
    );
    const token = await Token.findById(userId);
    if (decoded._id === userId) {
      if (token && token.refreshToken === refreshToken) {
        const userData = decoded;
        const accessToken = await jwtHelper.generateToken(
          userData,
          ACCESS_TOKEN_SECRET,
          ACCESS_TOKEN_LIFE
        );
        token.accessToken = accessToken;
        await token.save();
        return res.send({
          data: token,
          status: 200,
          message: "Refresh token thành công",
        });
      }
      handlerCustomError(202, "Refresh Token của bạn không hợp lệ");
    }
    handlerCustomError(
      401,
      "Refresh token này không phải của bạn, vui lòng sử dụng refresh token của mình để tiếp tục"
    );
  } catch (error) {
    next(error);
  }
};
module.exports = {
  signIn,
  signOut,
  signUp,
  refreshToken,
};
