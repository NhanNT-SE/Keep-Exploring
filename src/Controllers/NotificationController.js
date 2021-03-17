const e = require('express');
const Notification = require('../Models/Notification');

const createNotification = async (notify) => {
	try {
		const notiFound_list = await Notification.find({ idPost: notify.idPost });

		if (notiFound_list) {
			let notiUpdate = null;
			notiFound_list.forEach(async (item) => {
				if (item.content == notify.content) {
					item.status = 'new';
					notiUpdate = item;
					return;
				}
			});

			if (notiUpdate) {
				await notiUpdate.save();
			} else {
				await notify.save();
			}
			return;
		} else {
			await notify.save();
			return;
		}
	} catch (error) {
		return error;
	}
};



const changeNewStatusNoti = async (req, res, next) => {
	try {
		const { idNoti } = req.body;
		const notiFound = await Notification.findById(idNoti);

		if (!notiFound) {
			handleCustomError(201, 'Thông báo không tồn tại');
		}

		notiFound.status = 'new';
		await notiFound.save();
		return res.send({ data: null, status: 200, message: 'Đã đánh dấu thành thông báo chưa đọc' });
	} catch (error) {
		next(error);
	}
};

const changeSeenStatusNoti = async (req, res, next) => {
	try {
		const idUser = req.user._id;
		const newNoti_list = await Notification.find({ status: 'new', idUser: idUser });

		if (!newNoti_list) {
			handleCustomError(201, 'Bạn đã xem hết thông báo mới');
		}

		newNoti_list.forEach(async (item) => {
			item.status = 'seen';
			await item.save();
		});

		return res.send(newNoti_list);
	} catch (error) {
		next(error);
	}
};

const deleteNoti = async (req, res, next) => {
	try {
		const { idNoti } = req.params;
		const notiFound = await Notification.findById(idNoti);
		if (!notiFound) {
			handleCustomError(201, 'Thông báo không tồn tại hoặc đã bị xóa');
		}

		await Notification.findByIdAndDelete(idNoti);
		return res.send({ data: null, status: 200, message: 'Đã xóa thông báo' });
	} catch (error) {
		next(error);
	}
};

const getAllbyUser = async (req, res, next) => {
	try {
		const user = req.user;
		const { status } = req.query;
		const notification_list = await Notification.find({ idUser: user._id });

		if (!notification_list) {
			handleCustomError(201, 'Bạn chưa có thông báo nào');
		}

		if (status) {
			const resultList = notification_list.filter((item) => {
				return item.status == status;
			});
			return res.send({ data: { resultList }, status: 200, message: 'List thông báo đã lọc' });
		}

		return res.send({ data: { notification_list }, status: 200, message: 'List tất cả thông báo' });
	} catch (error) {
		next(error);
	}
};

const handleCustomError = (status, message) => {
	const err = new Error();
	err.status = status || 500;
	err.message = message;
	throw err;
};

module.exports = {
	createNotification,
	changeNewStatusNoti,
	changeSeenStatusNoti,
	deleteNoti,
	getAllbyUser,
};
