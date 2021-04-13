const User = require('../Models/User');
const bcrypt = require('bcryptjs');
const fs = require('fs');
const handlerCustomError = require('../middleware/customError');
const changePass = async (req, res, next) => {
	try {
		const { oldPass, newPass } = req.body;
		const user = req.user;

		const checkPass = await bcrypt.compare(oldPass, user.pass);

		if (checkPass) {
			const salt = await bcrypt.genSalt(10);
			const passHashed = await bcrypt.hash(newPass, salt);

			user.pass = passHashed;
			await user.save();
			return res.status(200).send({
				data: null,
				status: 200,
				message: 'Đổi mật khẩu thành công',
			});
		}

		handlerCustomError(201, 'Mật khẩu của bạn không đúng');
	} catch (error) {
		next(error);
	}
};


const getAnotherProfile = async (req, res, next) => {
	try {
		const { idUser } = req.params;
    const user = await User.findById(idUser);
		if (user) {
			return res.status(200).send({
				data: user,
				status: 200,
				message: 'Lấy dữ liệu thành công',
			});
		}
		return handlerCustomError(201, 'Người dùng không tồn tại hoặc đã bị xóa');
	} catch (error) {
		next(error);
	}
};

const updateProfile = async (req, res, next) => {
	try {
		const file = req.file;
		const user = req.user;
		const userFound = await User.findById(user._id);
		let avatar;

		if (!user) {
			handlerCustomError(201, 'Người dùng không tồn tại');
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
			data: {
				_id: user._id,
				email: user.email,
				...newUser,
			},
			message: 'Cap nhật thông tin cá nhân thành công',
			status: 200,
		});
	} catch (error) {
		next(error);
	}
};

module.exports = {
	changePass,
	getAnotherProfile,
	updateProfile,
};
