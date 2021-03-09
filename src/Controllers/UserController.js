const User = require('../Models/User');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const fs = require('fs');

const { JWT_SECRET, REFRESH_TOKEN_SECRET } = require('../config/index');
const RefreshToken = require('../Models/RefreshToken');

const getProfile = async (req, res, next) => {
	try {
		const user = req.user;
		if (user) {
			const profile = await User.findById(user._id);
			return res.status(200).send({ data: { profile }, err: '', status: 200, msg: '' });
		}

		next({ status: 201, message: 'user khong ton tai' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const logOut = async (req, res, next) => {
	try {
		const user = req.user;
		const userId = user._id;

		if (user) {
			const token = await RefreshToken.findById(userId);
			token.refreshToken = null;
			token.accessToken = null;
			await token.save();

			req.logout();
			return res.send({
				status: 200,
				data: null,
				msg: 'Logout successfully',
			});
		}

		next({
			status: 201,
			message: 'user khong ton tai',
		});
	} catch (error) {
		next({
			status: error.status,
			message: error.message,
		});
	}
};

const signIn = async (req, res, next) => {
	try {
		const { email, pass } = req.body;
		const user = await User.findOne({ email });

		//Kiem tra xem user co ton tai khong
		if (user) {
			const checkPass = await bcrypt.compare(pass, user.pass);

			//Kiem tra password dung thi tra ve status code 200
			if (checkPass) {
				// Synchronous Sign with default (HMAC SHA256)
				var accessToken = jwt.sign({ id: user._id }, JWT_SECRET, { expiresIn: '30m' });
				var refreshToken = jwt.sign({ id: user._id }, REFRESH_TOKEN_SECRET, { expiresIn: '15d' });

				let token = await RefreshToken.findById(user._id);
				if (!token) {
					token = new RefreshToken({
						_id: user._id,
						accessToken,
						refreshToken,
					});
				} else {
					token.accessToken = accessToken;
					token.refreshToken = refreshToken;
				}
				await token.save();

				return res.status(200).send({
					data: { user, token },
					err: '',
					status: 200,
				});
			}

			//Password sai thi tra ve status code 201
			next({
				status: 201,
				message: 'password khong dung',
			});
		}

		//User khong ton tai thi tra ve status code 202
		next({
			status: 202,
			message: 'user khong ton tai',
		});
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const signUp = async (req, res, next) => {
	try {
		const file = req.file;
		var imgUser;
		if (file) {
			imgUser = file.filename;
		} else {
			imgUser = 'avatar-default.png';
		}
		const user = req.body;

		const userFound = await User.findOne({ email: user.email });
		if (userFound) {
			next({
				status: 201,
				message: 'user da ton tai',
			});
		}

		const salt = await bcrypt.genSalt(10);
		const passHashed = await bcrypt.hash(user.pass, salt);

		var newUser = new User({
			...req.body,
			imgUser,
			pass: passHashed,
		});

		await newUser.save();
		return res.status(200).send({
			data: { newUser },
			message: 'Dang ky thanh cong',
			status: 200,
		});
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const updateProfile = async (req, res, next) => {
	try {
		const file = req.file;
		const user = req.user;
		const userFound = await User.findById(user._id);
		var avatar;

		if (!user) {
			next({
				status: 201,
				message: 'user khong ton tai',
			});
		}

		//Kiem tra nguoi dung muon doi avata hay khong
		if (file) {
			//Kiem tra truoc do nguoi dung da co avtar chua hay su dung avatar mac dinh
			if (userFound.imgUser !== 'avatar-default.png') {
				fs.unlinkSync('src/public/images/user/' + user.imgUser);
			}
			avatar = file.filename;
		} else {
			avatar = userFound.imgUser;
		}

		const newUser = {
			...req.body,
			imgUser: avatar,
		};

		await User.findByIdAndUpdate(user._id, newUser);
		return res.status(200).send({
			data: { newUser },
			message: 'Cap nhat profile thanh cong',
			status: 200,
		});
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

module.exports = {
	getProfile,
	logOut,
	signIn,
	signUp,
	updateProfile,
};
