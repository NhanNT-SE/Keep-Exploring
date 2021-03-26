const jwt = require("jsonwebtoken");
const Token = require("../Models/Token");
const { ACCESS_TOKEN_SECRET } = require("../config/index");
const generateToken = async (user, secretSignature, tokenLife) => {
  try {
    const userData = {
      _id: user._id,
      displayName: user.displayName,
      email: user.email,
      role: user.role,
      imgUser: user.imgUser,
    };
    const token = await jwt.sign( userData , secretSignature, {
      algorithm: "HS256",
      expiresIn: tokenLife,
    });
    return token;
  } catch (error) {
    return error;
  }
};

const verifyToken = async (token, secretKey, cb) => {
  try {
    const verify = await jwt.verify(token, secretKey);
    return verify;
  } catch (error) {
    cb(error);
  }
};

const isAuth = async (req, res, next) => {
  const TOKEN_FROM_CLIENT = req.headers["authorization"];
  // const userId = req.headers["user-id"];
  if (TOKEN_FROM_CLIENT) {
    try {
      const decoded = await verifyToken(
        TOKEN_FROM_CLIENT,
        ACCESS_TOKEN_SECRET,
        (err) => {
          return next(err);
        }
      );
      const token = await Token.findById(decoded._id);
      if (token.accessToken === TOKEN_FROM_CLIENT) {
        req.jwtDecoded = decoded;
        req.user = decoded;
        return next();
      }
      const err = new Error();
      err.status = 401;
      err.message = "Vui lòng đăng nhập để tiếp tục";
      return next(err);
    } catch (error) {
      next(error);
    }
  } else {
    const err = new Error();
    err.message = "Không nhận được token.";
    err.status = 401;
    return next(err);
  }
};

module.exports = {
  isAuth,
  generateToken,
  verifyToken,
};
