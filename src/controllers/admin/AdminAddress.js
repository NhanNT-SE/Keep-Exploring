import {customError} from "../../helpers/CustomError.js";
import Address from "../../models/Address.js";

const createAddress = async (req, res, next) => {
  try {
    const { province, district } = req.body;
    const addressFound = await Address.findOne({ province: province });
    if (addressFound) {
      const districtFound = [...addressFound.district];
      const lowerCasesDistrictBody = district.map((e) => e.name.toLowerCase());
      const listDuplicate = [];
      districtFound.some((e) => {
        if (lowerCasesDistrictBody.includes(e.name)) {
          listDuplicate.push(e.name);
        }
      });

      if (listDuplicate.length > 0) {
        return res.send({
          data: listDuplicate,
          status: 200,
          message: `Các địa điểm [${listDuplicate}] đã tồn tại vui lòng cập nhật, hoặc loại bỏ các địa điểm đã tồn tại để tiếp tục`,
        });
      }
      const updateDistrict = addressFound.district.concat(district);
      addressFound.district = updateDistrict;
      await addressFound.save();
      return res.send({
        data: addressFound,
        status: 200,
        message: "Đã cập nhật lại địa chỉ",
      });
    }
    const newAddress = await new Address({ province, district }).save();
    return res.send({
      data: newAddress,
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

const updateDistrict = async (req, res, next) => {
  try {
    const { province, district, ward } = req.body;
    const addressFound = await Address.findOne({ province });
    if (addressFound) {
      const districtFound = [...addressFound.district];
      const index = districtFound.findIndex(
        (e) => e.name === district.toLowerCase()
      );

      if (index < 0) {
        customError(
          201,
          `Không tìm thấy địa điểm '${district}' trong ${province}`
        );
      }

      let wardFound = districtFound[index].ward;
      if (!wardFound) {
        wardFound = [...ward];
      }
      wardFound = [...new Set(ward.concat(wardFound))];
      addressFound.district[index].ward = wardFound;
      await Address.findByIdAndUpdate(addressFound._id, addressFound);
      return res.send({
        data: addressFound.district[index],
        status: 200,
        message: "Cập nhật địa điểm thành công",
      });
    }
    customError(
      201,
      `Địa điểm ${province} chưa tồn tại trong hệ thống, vui lòng tạo mới hoặc cập nhật lại`
    );
  } catch (error) {
    next(error);
  }
};

export { createAddress, deleteAddress, updateDistrict };
