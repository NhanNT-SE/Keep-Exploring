const getAllbyUser = async (req, res, next) => {
	try {
	} catch (error) {
		next({ status: error.status, message: error.message });
	}
};

module.exports = {
	getAllbyUser,
};
