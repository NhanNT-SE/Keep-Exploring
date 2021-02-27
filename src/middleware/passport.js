const passport = require('passport');
const User = require('../Models/User');

const { ExtractJwt } = require('passport-jwt');
const JwtStrategy = require('passport-jwt').Strategy;
const { JWT_SECRET } = require('../config/index');

const opts = {
	jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken('Authorization'),
	secretOrKey: JWT_SECRET,
	ignoreExpiration: true,
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
		User.findById(jwt_payload.id, function (err, user) {
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
