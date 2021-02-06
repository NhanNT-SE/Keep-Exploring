const Address = require('../Models/Address');

const createAdress = async (req, res) => {
	try {
		const address = req.body;
		address.idPost = '5ffd2debfc4c8b1dd8791cc2';
		await new Address(address).save();
		return res.status(200).send(address);
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const deleteAddress = async (req, res) => {
	try {
		const { idPost } ="5ffd2debfc4c8b1dd8791cc2"
		await Address.deleteOne({ idPost: idPost });
		return res.status(200).send('Deleted address');
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

const getAddressList = async (req, res) => {
	try {
		const addressList = await Address.find({}).populate("idPost");
		return res.status(200).send(addressList);
	} catch (error) {
		return res.status(202).send(error.message);
	}
};

module.exports = {
    createAdress,
    deleteAddress,
	getAddressList,
};
