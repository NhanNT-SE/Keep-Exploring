const jwt = require("jsonwebtoken");
const Token = require("../Models/Token");
const { ACCESS_TOKEN_SECRET } = require("../config/index");
const handlerCustomError = require("./customError");
const generateToken = async (user, secretSignature, tokenLife) => {
  try {
    const userData = {
      _id: user._id,
      role: user.role,
    };
    const token = await jwt.sign(userData, secretSignature, {
      algorithm: "HS256",
      expiresIn: tokenLife,
    });
    return token;
  } catch (error) {
    handlerCustomError(401, error.message);
  }
};

const verifyToken = async (token, secretKey) => {
  try {
    const verify = await jwt.verify(token, secretKey);
    return verify;
  } catch (error) {
    const message = error.message;
    if (message === "jwt expired") {
      const dateExpired = getNumberDateExpired(error.expiredAt);
      handlerCustomError(401, message,dateExpired);
    }
    handlerCustomError(401, message);
  }
};

const isAuth = async (req, res, next) => {
  const TOKEN_FROM_CLIENT = req.headers["authorization"];
  // const userId = req.headers["user-id"];
  try {
    if (TOKEN_FROM_CLIENT) {
      const decoded = await verifyToken(TOKEN_FROM_CLIENT, ACCESS_TOKEN_SECRET);
      const token = await Token.findById(decoded._id);
      if (token.accessToken === TOKEN_FROM_CLIENT) {
        req.jwtDecoded = decoded;
        req.user = decoded;
        return next();
      }
      handlerCustomError(401, "Vui lòng đăng nhập lại để tiếp tục");
    }
    handlerCustomError(401, "Không nhận được token.");
  } catch (error) {
    next(error);
  }
};
const isAdmin = async (req, res, next) => {
  const { user } = req;
  try {
    if (user.role === "admin") {
      return next();
    }
    handlerCustomError(
      401,
      "Tài khoản hiện tại của bạn không đủ quyền để thực hiện hành động này"
    );
  } catch (error) {
    next(error);
  }
};
const getNumberDateExpired = (dateExpired) => {
  const date = new Date();
  const diffTime = Math.abs(date - dateExpired);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays;
};
module.exports = {
  isAdmin,
  isAuth,
  generateToken,
  verifyToken,
};
