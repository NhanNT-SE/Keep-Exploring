const Address = require("../Models/Address");

const createAddress = async (req, res, next) => {
  try {
    const address = new Address({
      ...req.body,
    });
    await new Address(address).save();
    return res.send({
      data: { address },
      status: 200,
      message: "Tạo địa chỉ thành công",
    });
  } catch (error) {
    next(error);
  }
};

const deleteAddress = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    await Address.deleteOne({ idPost: idPost });

    return res.send({ data: null, status: 200, message: "Đã xóa địa điểm" });
  } catch (error) {
    next(error);
  }
};

const getPostByAddress = async (req, res, next) => {
  try {
    const { province } = req.body;
    var postList = [];

    const addressList = await Address.find({ province }).populate("idPost");
    // addressList.forEach((item) => {
    //   postList.push(item.idPost);
    // });
    postList = [...addressList];
    if ((postList.length = 0)) {
      handlerCustomError(201, "Địa điểm chưa được review");
    }

    return res.send({ data: postList, status: 200, message: "" });
  } catch (error) {
    next(error);
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
      return res.send({
        data: { newAddress },
        status: 200,
        message: "cập nhật địa chỉ thành công",
      });
    }

    handlerCustomError(201, "Địa chỉ không tồn tại");
  } catch (error) {
    next(error);
  }
};
const handlerCustomError = (status, message) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  throw err;
};
module.exports = {
  createAddress,
  deleteAddress,
  getPostByAddress,
  updateAddress,
};
