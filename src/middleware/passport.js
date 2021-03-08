const passport = require('passport');
const User = require('../Models/User');

const { ExtractJwt } = require('passport-jwt');
const JwtStrategy = require('passport-jwt').Strategy;
const { JWT_SECRET } = require('../config/index');

const opts = {
	jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken('Authorization'),
	secretOrKey: JWT_SECRET,
<<<<<<< HEAD
=======
	ignoreExpiration: true,
>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331
};

passport.use(
	'jwt',
	new JwtStrategy(opts, (jwt_payload, done) => {
		// try {
		// 	console.log('payload', payload);
		// 	return done(null, payload, { message: 'Logged in Successfully' });
		// } catch (error) {
		// 	done(error, false);
		// }
<<<<<<< HEAD

		User.findOne({ id: jwt_payload.sub }, function (err, user) {
=======
		User.findById(jwt_payload.id, function (err, user) {
>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331
			if (err) {
				return done(err, false);
			}
			if (user) {
				return done(null, user);
			} else {
				return done(null, false);
				// or you could create a new account
			}
		});
	})
);

passport.serializeUser(function (payload, done) {
	done(null, payload);
});

passport.deserializeUser(function (payload, done) {
	done(null, payload);
});
