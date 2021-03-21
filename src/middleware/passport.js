const passport = require("passport");
const User = require("../Models/User");

const { ExtractJwt } = require("passport-jwt");
const JwtStrategy = require("passport-jwt").Strategy;
const { JWT_SECRET } = require("../config/index");
const RefreshToken = require("../Models/RefreshToken");

const opts = {
  jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken("Authorization"),
  secretOrKey: JWT_SECRET,
  ignoreExpiration: true,
};

passport.use(
  "jwt",
  new JwtStrategy(opts, async (jwt_payload, done) => {
    try {
      const user = await User.findById(jwt_payload.id);
      console.log(user);
      const token = await RefreshToken.findById(jwt_payload.id);
      if (user && token.accessToken) {
        return done(null, user);
      }

      if (user) {
        return done(
          { status: 201, message: "Người dùng chưa đăng nhập" },
          false
        );
      }

      return done({ status: 202, message: "Tài khoản không tồn tại" }, false);
    } catch (error) {
      return done(error.message, false);
    }
  })
);

passport.serializeUser(function (payload, done) {
  done(null, payload);
});

passport.deserializeUser(function (payload, done) {
  done(null, payload);
});
