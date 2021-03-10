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
      message: "Tao dia chi thanh cong",
    });
  } catch (error) {
    next(error);
  }
};

const deleteAddress = async (req, res, next) => {
  try {
    const { idPost } = req.params;
    await Address.deleteOne({ idPost: idPost });

    return res.send({ data: null, status: 200, message: "Da xoa dia diem" });
  } catch (error) {
    next(error);
  }
};

//Khong dung toi
const getAddressList = async (req, res, next) => {
  try {
    const addressList = await Address.find({}).populate("idPost");
    return res.status(200).send(addressList);
  } catch (error) {
    next(error);
  }
};

const getPostByAddress = async (req, res, next) => {
  try {
    const { province } = req.body;
    var postList = [];

    const addressList = await Address.find({ province }).populate("idPost");
    addressList.forEach((item) => {
      postList.push(item.idPost);
    });

    if ((postList.length = 0)) {
      //   next({ status: 201, message: "Dia diem chua duoc review" });
      handlerCustomError(201, "Dia diem chua duoc review");
    }

    return res.send({ data: { postList }, status: 200, message: "" });
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

    // next({ status: 201, message: "Dia chi khong ton tai" });
    handlerCustomError(201, "Dia chi khong ton tai");
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
  getAddressList,
  getPostByAddress,
  updateAddress,
};
