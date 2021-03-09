const RefreshToken = require('../Models/RefreshToken');
const jwt = require('jsonwebtoken');
const { REFRESH_TOKEN_SECRET, JWT_SECRET } = require('../config/index');

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
				const newAccessToken = jwt.sign({ id: decode.id }, JWT_SECRET, { expiresIn: '1h' });
				return res.send({ data: { newAccessToken } });
			}

			return next({ status: 201, message: 'idUser va decode cua refreshToken khong giong nhau' });
		}
	} catch (error) {
		next(error);
	}
};

module.exports = {
	rfToken,
};
