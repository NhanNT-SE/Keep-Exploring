const jwt = require("jsonwebtoken");
const Token = require("../Models/Token");
const accessTokenSecret = process.env.ACCESS_TOKEN_SECRET;
const generateToken = async (user, secretSignature, tokenLife) => {
  try {
    const userData = {
      _id: user._id,
      name: user.name,
      email: user.email,
      role: user.role,
      displayName: user.displayName,
      imageUser: user.imgUser,
    };
    const token = await jwt.sign({ data: userData }, secretSignature, {
      algorithm: "HS256",
      expiresIn: tokenLife,
    });
    return token;
  } catch (error) {
    console.log("Create Token Error:", error);
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
  const tokenFromClient = req.headers["authorization"];
  const userId = req.headers["userId"];
  if (tokenFromClient) {
    try {
      const decoded = await verifyToken(
        tokenFromClient,
        accessTokenSecret,
        (err) => {
          err.status = 401;
          return next(err);
        }
      );
      
      const token = await Token.findById(decoded.data._id);
      if (token.accessToken === tokenFromClient) {
        req.jwtDecoded = decoded;
        return next();
      }
      let err = new Error();
      err.status = 401;
      err.message = "Invalid Token";
      return next(err);
    } catch (error) {
      next(error);
    }
  } else {
    let err = new Error();
    err.message = "No token provided.";
    err.status = 401;
    return next(err);
  }
};
module.exports = {
  isAuth,
  generateToken,
  verifyToken,
};
