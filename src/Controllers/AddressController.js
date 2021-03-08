const Address = require('../Models/Address');

const createAdress = async (req, res) => {
	try {
		const address = new Address({
			...req.body,
		});
		await new Address(address).save();
		return res.status(200).send(address);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const deleteAddress = async (req, res) => {
	try {
		const { idPost } = req.params;
		await Address.deleteOne({ idPost: idPost });
		return res.status(200).send('Deleted address');
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const getAddressList = async (req, res) => {
	try {
		const addressList = await Address.find({}).populate('idPost');
		return res.status(200).send(addressList);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

const getPostbyAddress = async (req, res) => {
	try {
		const { province } = req.body;
		var postList = [];

		// await Address.find({ province }).forEach((item) => {
		// 	postList.push(item);
		// });

		const addressList = await Address.find({ province }).populate('idPost');
		addressList.forEach((item) => {
			postList.push(item.idPost);
		});

		if ((postList.length = 0)) {
			return res.status(201).send('Dia diem chua duoc review');
		}

		return res.status(200).send(postList);
	} catch (error) {
		return res.status(500).send(error.message);
	}
};

module.exports = {
	createAdress,
	deleteAddress,
	getAddressList,
	getPostbyAddress,
};
