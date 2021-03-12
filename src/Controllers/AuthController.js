const jwtHelper = require("../middleware/jwtHelper");
const User = require("../Models/User");
const Token = require("../Models/Token");
const bcrypt = require("bcryptjs");

const accessTokenLife = "1d";
const accessTokenSecret = process.env.ACCESS_TOKEN_SECRET;
const refreshTokenLife = "365d";
const refreshTokenSecret = process.env.REFRESH_TOKEN_SECRET;
const sleep = require("util").promisify(setTimeout);
const login = async (req, res, next) => {
  try {
    const { email, pass } = req.body;
    const user = await User.findOne({ email });
    if (user) {
      const checkPass = await bcrypt.compare(pass, user.pass);
      let token = await Token.findById(user._id);
      if (checkPass) {
        const accessToken = await jwtHelper.generateToken(
          user,
          accessTokenSecret,
          accessTokenLife
        );

        const refreshToken = await jwtHelper.generateToken(
          user,
          refreshTokenSecret,
          refreshTokenLife
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
        const result = {
          accessToken,
          refreshToken,
          _id: user._id,
          name: user.name,
          email: user.email,
          role: user.role,
          displayName: user.displayName,
          imageUser: user.imgUser,
        };
        // return res.status(200).send({
        //   status: 200,
        //   data: result,
        // });
        await sleep(1000);
        return res.status(200).send({
          status: 200,
          data: result,
        });
      }
      customErr(201, "Your password incorrect");
    }
    customErr(201, "this user is not exists");
  } catch (e) {
    console.log(e);
    next(e);
  }
};
const refreshToken = async (req, res) => {
  try {
    const { refreshToken, userId } = req.body;
    const token = await Token.findById(userId);
    if (token && token.refreshToken === refreshToken) {
      const decoded = await jwtHelper.verifyToken(
        refreshToken,
        refreshTokenSecret
      );
      const userData = decoded.data;
      // console.log("user decode", decoded);
      const accessToken = await jwtHelper.generateToken(
        userData,
        accessTokenSecret,
        accessTokenLife
      );
      token.accessToken = accessToken;
      await token.save();
      return res.send({
        status: 200,
        data: token,
      });
    }
    return res.send({
      status: 403,
      data: null,
      msg: "No token provided or Invalid refresh token !",
    });
  } catch (error) {
    return res.status(500).send("loi server:" + error.message);
  }
};

const logout = async (req, res) => {
  try {
    const { userId } = req.body;
    const token = await Token.findById(userId);
    token.refreshToken = null;
    token.accessToken = null;
    await token.save();
    return res.send({
      status: 200,
      data: null,
      msg: "Logout successfully",
    });
  } catch (error) {}
};
const customErr = (status, message) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  throw err;
};

module.exports = {
  login,
  logout,
  refreshToken,
};
