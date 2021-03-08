const RefreshToken = require('../Models/RefreshToken');
const jwt = require('jsonwebtoken');
const { REFRESH_TOKEN_SECRET } = require('../config/index');

const rfToken = async (req, res, next) => {
	try {
		//Lay accessToken va refreshToken tu header cua user gui len
		const rfToken = req.headers.refreshtoken;
		const accessTokenfull = req.headers.authorization;
		const accessToken = accessTokenfull.slice(7);

		//Kiem tra accessToken co ton tai hay khong
		const tokenFound = await RefreshToken.findOne({ accessToken });
		if (tokenFound) {
			//Neu ton tai thi kiem tra nguoi dung co gui refreshToken len khong
			//va refreshToken cua database vs cua user gui len co giong nhau khong
			if (rfToken && rfToken === tokenFound.refreshToken) {
				//decode ra data cua user
				const decode = jwt.verify(refreshToken, REFRESH_TOKEN_SECRET);

				//Sau do tao accessToken moi
				const newAccessToken = jwt.sign(decode, JWT_SECRET, { expiresIn: '30m' });
				res.setHeader('Authorization', newAccessToken);
			}
		}

		// if (refreshToken && rf_token_database.refreshToken) {
		// 	return res.send(decode);
		// }
	} catch (error) {
		next(error);
	}
};

module.exports = {
	rfToken,
};
