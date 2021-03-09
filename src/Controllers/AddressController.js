const Address = require('../Models/Address');

const createAdress = async (req, res, next) => {
	try {
		const address = new Address({
			...req.body,
		});
		await new Address(address).save();
		return res.send({ data: { address }, status: 200, message: 'Created Address Successfully' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const deleteAddress = async (req, res, next) => {
	try {
		const { idPost } = req.params;
		await Address.deleteOne({ idPost: idPost });

		return res.send({ data: null, status: 200, message: 'Deleted Address' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const getAddressList = async (req, res, next) => {
	try {
		const addressList = await Address.find({}).populate('idPost');
		return res.status(200).send(addressList);
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const getPostbyAddress = async (req, res, next) => {
	try {
		const { province } = req.body;
		var postList = [];

		const addressList = await Address.find({ province }).populate('idPost');
		addressList.forEach((item) => {
			postList.push(item.idPost);
		});

		if ((postList.length = 0)) {
			next({ status: 201, message: 'Dia diem chua duoc review' });
		}

		return res.send({ data: { postList }, status: 200, message: '' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

const updateAddress = async (req, res, next) => {
	try {
		const { idAddress } = req.params;
		const addressFound = await Address.findById(idAddress);

		if (addressFound) {
			const newAddress = {
				...req.body,
			};

			await Address.findByIdAndUpdate(idAddress, newAddress);
			return res.send({ data: { newAddress }, status: 200, message: 'Update Address successfully' });
		}

		next({ status: 201, message: 'Dia chi khong ton tai' });
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

module.exports = {
	createAdress,
	deleteAddress,
	getAddressList,
	getPostbyAddress,
	updateAddress,
};
