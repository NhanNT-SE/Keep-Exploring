const RefreshToken = require("../Models/RefreshToken");
const jwt = require("jsonwebtoken");
const { REFRESH_TOKEN_SECRET, JWT_SECRET } = require("../config/index");

const rfToken = async (req, res, next) => {
  try {
    //Lay accessToken va refreshToken tu header cua user gui len
    const { refreshToken, userId } = req.body;

    //Kiem tra accessToken co ton tai hay khong
    const tokenFound = await RefreshToken.findById(userId);

    if (tokenFound && refreshToken === tokenFound.refreshToken) {
      //decode ra data cua user
      const decode = jwt.verify(refreshToken, REFRESH_TOKEN_SECRET);
      if (decode.id == userId) {
        const newAccessToken = jwt.sign({ id: decode.id }, JWT_SECRET, {
          expiresIn: "15s",
        });
        return res.send({
          data: newAccessToken,
          status: 200,
          message: "Refresh token thành công",
        });
      }

      handlerCustomError(201, "Refresh Token của bạn không hợp lệ");
    }

    handlerCustomError(202, "Refresh Token của bạn không hợp lệ");
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
  rfToken,
};
