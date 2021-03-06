const jwtHelper = require("./jwtHelper");
const { JWT_SECRET } = require("../config/index");
const accessTokenSecret = JWT_SECRET;

const isAuth = async (req, res, next) => {
  try {
    const tokenFromClient =
      req.body.token || req.query.token || req.headers.Authorization;
    if (tokenFromClient) {
      const decode = await jwtHelper.verifyToken(
        tokenFromClient,
        accessTokenSecret
      );
      return next();
    }
    return res.send({
      status: 401,
      data: null,
      message: "Unauthorized.",
      error: null,
    });
  } catch (error) {
    return error;
  }
};


