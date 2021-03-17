const User = require("../Models/User");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const fs = require("fs");

const { JWT_SECRET, REFRESH_TOKEN_SECRET } = require("../config/index");
const RefreshToken = require("../Models/RefreshToken");

const changePass = async (req, res, next) => {
  try {
    const { oldPass, newPass } = req.body;
    const user = req.user;

    const checkPass = await bcrypt.compare(oldPass, user.pass);

    if (checkPass) {
      const salt = await bcrypt.genSalt(10);
      const passHashed = await bcrypt.hash(newPass, salt);

      user.pass = passHashed;
      await user.save();
      return res.status(200).send({
        data: null,
        status: 200,
        message: "Đổi mật khẩu thành công",
      });
    }

    handlerCustomError(201, "Mật khẩu của bạn không đúng");
  } catch (error) {
    next(error);
  }
};

const getProfile = async (req, res, next) => {
  try {
    const user = req.user;
    if (user) {
      const profile = await User.findById(user._id);
      return res.status(200).send({
        data: profile,
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }

    handlerCustomError(201, "Không tồn tại người dùng này trong hệ thống");
  } catch (error) {
    next(error);
  }
};

const logOut = async (req, res, next) => {
  try {
    const user = req.user;
    const userId = user._id;
    if (user) {
      const token = await RefreshToken.findById(userId);
      token.refreshToken = null;
      token.accessToken = null;
      await token.save();

      req.logout();
      return res.send({
        status: 200,
        data: null,
        message: "Đăng xuất thành công",
      });
    }

    handlerCustomError(201, "Không tồn tại người dùng này trong hệ thống");
  } catch (error) {
    next(error);
  }
};

const signIn = async (req, res, next) => {
  try {
    const { email, pass } = req.body;
    const user = await User.findOne({ email });

    //Kiem tra xem user co ton tai khong
    if (user) {
      const checkPass = await bcrypt.compare(pass, user.pass);

      //Kiem tra password dung thi tra ve status code 200
      if (checkPass) {
        // Synchronous Sign with default (HMAC SHA256)
        let accessToken = jwt.sign({ id: user._id }, JWT_SECRET, {
          expiresIn: "1d",
        });
        let refreshToken = jwt.sign({ id: user._id }, REFRESH_TOKEN_SECRET, {
          expiresIn: "15d",
        });

        let token = await RefreshToken.findById(user._id);
        if (!token) {
          token = new RefreshToken({
            _id: user._id,
            accessToken,
            refreshToken,
          });
        } else {
          token.accessToken = accessToken;
          token.refreshToken = refreshToken;
        }
        await token.save();
        return res.status(200).send({
          data: {
            _id: user._id,
            name: user.name,
            email: user.email,
            role: user.role,
            displayName: user.displayName,
            imageUser: user.imgUser,
            accessToken,
            refreshToken,
          },
          status: 200,
          message: "Đăng nhập thành công",
        });
      }

      //Password sai thi tra ve status code 201
      handlerCustomError(201, "Mật khẩu của bạn không đúng");
    }

    //User khong ton tai thi tra ve status code 202
    handlerCustomError(202, "Người dùng không tồn tại");
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

const updateProfile = async (req, res, next) => {
  try {
    const file = req.file;
    const user = req.user;
    const userFound = await User.findById(user._id);
    let avatar;

    if (!user) {
      handlerCustomError(201, "Người dùng không tồn tại");
    }

    //Kiem tra nguoi dung muon doi avata hay khong
    if (file) {
      //Kiem tra truoc do nguoi dung da co avtar chua hay su dung avatar mac dinh
      if (userFound.imgUser !== "avatar-default.png") {
        fs.unlinkSync("src/public/images/user/" + user.imgUser);
      }
      avatar = file.filename;
    } else {
      avatar = userFound.imgUser;
    }

    const newUser = {
      ...req.body,
      imgUser: avatar,
    };

    await User.findByIdAndUpdate(user._id, newUser);
    return res.status(200).send({
      data: newUser,
      message: "Cap nhật thông tin cá nhân thành công",
      status: 200,
    });
  } catch (error) {
    next(error);
  }
};
const handlerCustomError = (status, message) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  throw err;
};
module.exports = {
  changePass,
  getProfile,
  logOut,
  signIn,
  signUp,
  updateProfile,
};
