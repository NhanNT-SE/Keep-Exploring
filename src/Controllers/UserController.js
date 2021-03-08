const User = require('../Models/User');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const fs = require('fs');

const { JWT_SECRET } = require('../config/index');

const getProfile = async (req, res) => {
	try {
		const user = req.user;
		if (user) {
			const profile = await User.findById(user._id);
			return res.status(200).send(profile);
		}

		return res.status(201).send(null);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const logOut = async (req, res) => {
	try {
		const user = req.user;
		if (user) {
			req.logout();
			return res.status(200).send(null);
		}
		return res.status(201).send(null);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const signIn = async (req, res) => {
	try {
		const { email, pass } = req.body;
		const user = await User.findOne({ email });

		//Kiem tra xem user co ton tai khong
		if (user) {
			const checkPass = await bcrypt.compare(pass, user.pass);

			//Kiem tra password dung thi tra ve status code 200
			if (checkPass) {
				// Synchronous Sign with default (HMAC SHA256)
				var token = jwt.sign({ id: user._id }, JWT_SECRET, { expiresIn: '20d' });

				//Set Header authorization
				res.setHeader('Authorization', 'Bearer ' + token);
				return res.status(200).send(user);
			}

			//Password sai thi tra ve status code 201
			return res.status(201).send();
		}

		//User khong ton tai thi tra ve status code 202
		return res.status(202).send();
	} catch (e) {
		return res.status(500).send('loi server:' + e.message);
	}
};

const signUp = async (req, res) => {
	try {
		const file = req.file;
		var imgUser;
		if (file) {
			imgUser = file.filename;
			console.log('imgUser: ', imgUser);
		} else {
			imgUser = 'avatar-default.png';
		}
		const user = req.body;

		const userFound = await User.findOne({ email: user.email });
		if (userFound) {
			return res.status(201).send(userFound);
		}

		const salt = await bcrypt.genSalt(10);
		const passHashed = await bcrypt.hash(user.pass, salt);

		var newUser = new User({
			...req.body,
			imgUser,
			pass: passHashed,
		});

		await newUser.save();
		return res.status(200).send(newUser);
	} catch (e) {
		return res.status(202).send(e.message);
	}
};

const updateProfile = async (req, res) => {
	try {
		const file = req.file;
		const user = req.user;
		const userFound = await User.findById(user._id);
		var avatar;

		if (!user) {
			return res.status(201).send(null);
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
		return res.status(200).send(newUser);
	} catch (error) {}
};

module.exports = {
	getProfile,
	logOut,
	signIn,
	signUp,
	updateProfile,
};
