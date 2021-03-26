const User = require("../Models/User");
const bcrypt = require("bcryptjs");
const fs = require("fs");
const handlerCustomError = require("../middleware/customError");
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

const getMyProfile = async (req, res, next) => {
  try {
    const user = req.user;
    if (user) {
      const profile = await User.findById(user._id).populate("post");
      return res.status(200).send({
        data: profile,
        status: 200,
        message: "Lấy dữ liệu thành công",
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

const getAnotherProfile = async (req, res, next) => {
  try {
    const { idUser } = req.params;
    const profile = await User.findById(idUser)
      .populate("post")
      .populate("blog");

    if (profile) {
      return res.status(200).send({
        data: profile,
        status: 200,
        message: "Lấy dữ liệu thành công",
      });
    }

    return handlerCustomError(201, "Người dùng không tồn tại hoặc đã bị xóa");
  } catch (error) {
    next(error);
  }
};

const getAllUser = async (req, res, next) => {
  try {
    const user = req.user;
    if (user.role === "admin") {
      const userList = await User.find({ role: "user" }, { pass: 0, role: 0 });
      return res.send({
        data: userList,
        status: 201,
        message: "Danh sách tất cả người dùng",
      });
    }
    return handlerCustomError(201, "Bạn không phải admin");
  } catch (error) {
    next(error);
  }
};

const deleteUser = async (req, res, next) => {
  try {
    const { idUser } = req.params;
    const user = req.user;

    if (user.role != "admin") {
      return handlerCustomError(201, "Bạn không phải admin");
    }

    const userFound = await User.findById(idUser);
    if (!userFound) {
      return handlerCustomError(
        202,
        "Người dùng này không tồn tại hoặc đã bị xóa"
      );
    }

    if (userFound.role == "admin") {
      return handlerCustomError(203, "Bạn không thể xóa tài khoản admin");
    }

    await User.findByIdAndDelete(idUser);
    return res.send({
      data: null,
      status: 200,
      messsage: "Xóa tài khoản thành công",
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
      data: {
        _id: user._id,
        email: user.email,
        ...newUser,
      },
      message: "Cap nhật thông tin cá nhân thành công",
      status: 200,
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  changePass,
  getAllUser,
  getMyProfile,
  getAnotherProfile,
  deleteUser,
  updateProfile,
};
