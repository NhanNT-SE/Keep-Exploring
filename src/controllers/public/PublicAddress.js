import { customError } from "../../helpers/CustomError.js";
import { customResponse } from "../../helpers/CustomResponse.js";
import { Address } from "../../models/Address.js";

const getAddress = async (req, res, next) => {
  try {
    let { district, province } = req.body;
    if (!province || province === "") {
      province = "tp.hcm";
    }
    const address = await Address.findOne({ province });
    if (address) {
      let wardList = [];
      const districtList = address.district.map((e) => e.name);
      const index = address.district.findIndex((e) => e.name === district);
      if (index >= 0 && district) {
        wardList = address.district[index].ward;
      }
      return res.send(
        customResponse({ wardList, districtList })
      );
    }
    customError(`Không tìm thấy địa điểm ${province} trong hệ thống`);
  } catch (error) {
    next(error);
  }
};
const getProvinceList = async (req, res, next) => {
  try {
    const address = await Address.find({});
    const provinceList = address.map((e) => e.province);
    return res.send(customResponse(provinceList));
  } catch (error) {
    next(error);
  }
};
export { getAddress, getProvinceList };
