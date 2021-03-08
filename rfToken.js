const express = require('express');
const bodyParser = require('body-parser');
const jwt = require('jsonwebtoken');
const router = express.Router();
const config = require('./config');
const utils = require('./utils');
const tokenList = {};
const app = express();

router.get('/', (req, res) => {
	res.send('Ok');
});

/**
 * Đăng nhập
 * POST /login
 */
router.post('/login', (req, res) => {
	const postData = req.body;
	const user = {
		email: postData.email,
		name: postData.name,
	};

	// Thực hiện việc kết nối cơ sở dữ liệu (hay tương tự) để kiểm tra thông tin username and password
	// Đăng nhập thành công, tạo mã token cho user
	const token = jwt.sign(user, config.secret, {
		expiresIn: config.tokenLife,
	});

	// Tạo một mã token khác - Refresh token
	const refreshToken = jwt.sign(user, config.refreshTokenSecret, {
		expiresIn: config.refreshTokenLife,
	});

	// Lưu lại mã Refresh token, kèm thông tin của user để sau này sử dụng lại
	tokenList[refreshToken] = user;

	// Trả lại cho user thông tin mã token kèm theo mã Refresh token
	const response = {
		token,
		refreshToken,
	};

	res.json(response);
});

/**
 * Lấy mã token mới sử dụng Refresh token
 * POST /refresh_token
 */
router.post('/refresh_token', async (req, res) => {
	// User gửi mã Refresh token kèm theo trong body
	const { refreshToken } = req.body;

	// Kiểm tra Refresh token có được gửi kèm và mã này có tồn tại trên hệ thống hay không
	if (refreshToken && refreshToken in tokenList) {
		try {
			// Kiểm tra mã Refresh token
			await utils.verifyJwtToken(refreshToken, config.refreshTokenSecret);

			// Lấy lại thông tin user
			const user = tokenList[refreshToken];

			// Tạo mới mã token và trả lại cho user
			const token = jwt.sign(user, config.secret, {
				expiresIn: config.tokenLife,
			});
			const response = {
				token,
			};
			res.status(200).json(response);
		} catch (err) {
			console.error(err);
			res.status(403).json({
				message: 'Invalid refresh token',
			});
		}
	} else {
		res.status(400).json({
			message: 'Invalid request',
		});
	}
});

/**
 * Middleware xác thực người dùng dựa vào mã token
 * @param {*} req
 * @param {*} res
 * @param {*} next
 */
const TokenCheckMiddleware = async (req, res, next) => {
	// Lấy thông tin mã token được đính kèm trong request
	const token = req.body.token || req.query.token || req.headers['x-access-token'];

	// decode token
	if (token) {
		// Xác thực mã token và kiểm tra thời gian hết hạn của mã
		try {
			const decoded = await utils.verifyJwtToken(token, config.secret);

			// Lưu thông tin giã mã được vào đối tượng req, dùng cho các xử lý ở sau
			req.decoded = decoded;
			next();
		} catch (err) {
			// Giải mã gặp lỗi: Không đúng, hết hạn...
			console.error(err);
			return res.status(401).json({
				message: 'Unauthorized access.',
			});
		}
	} else {
		// Không tìm thấy token trong request
		return res.status(403).send({
			message: 'No token provided.',
		});
	}
};

router.use(TokenCheckMiddleware);

router.get('/profile', (req, res) => {
	// all secured routes goes here
	res.json(req.decoded);
});

app.use(bodyParser.json());

app.use('/api', router);

app.listen(config.port || process.env.PORT || 3000);
