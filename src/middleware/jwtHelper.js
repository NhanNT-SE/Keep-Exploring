const jwt = require("jsonwebtoken");
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

const verifyToken = async (token, secretKey) => {
  try {
    const verify = await jwt.verify(token, secretKey);
    return verify;
  } catch (error) {
    console.log("Verify Token Error:", error);
  }
};

const isAuth = async (req, res, next) => {
  const tokenFromClient =
    req.body.token || req.query.token || req.headers["authorization"];
  if (tokenFromClient) {
    try {
      const decoded = await verifyToken(tokenFromClient, accessTokenSecret);
      req.jwtDecoded = decoded;
      next();
    } catch (error) {
      return res.send({
        status: 401,
        data: null,
        message: error.message,
        error: null,
      });
    }
  } else {
    return res.send({
      status: 401,
      data: null,
      message: "No token provided.",
      error: null,
    });
  }
};
module.exports = {
  isAuth,
  generateToken,
  verifyToken,
};
