import bcrypt from "bcryptjs";
import { customError } from "../../helpers/CustomError.js";
import { User } from "../../models/User.js";

import { SERVER_NAME } from "../../config/index.js";
import { MFA } from "../../models/MFA.js";
import {
  generateOTPToken,
  generateQRCode,
  generateSecretUser,
  verifyOTPToken,
} from "../../helpers/MFAHelper.js";

const getQRCode = async (req, res, next) => {
  try {
    const { password } = req.body;
    const user = await User.findById(req.user._id).populate("mfa");
    const mfa = await MFA.findOne({ owner: user._id });
    const checkPass = await bcrypt.compare(password, user.password);
    if (checkPass) {
      if (mfa.status === "idle") {
        const secretMFA = generateSecretUser();
        await mfa.updateOne({ secretMFA });
        const otpAuth = generateOTPToken(user.username, SERVER_NAME, secretMFA);
        const qrCode = await generateQRCode(otpAuth);
        return res.status(200).send({
          data: { qrCode },
          status: 200,
          message: "QR code was generated",
        });
      }
      customError(500, "MFA is already active");
    }
    customError(500, "Your password incorrect");
  } catch (error) {
    next(error);
  }
};
const enableMFA = async (req, res, next) => {
  try {
    const { userId } = req.body;
    const { user } = req;
    const mfa = await MFA.findOne({ owner: user._id });
    if (userId === user._id) {
      await mfa.updateOne({ status: "enable" });
      return res.status(200).send({
        data: { isEnable: true },
        status: 200,
        message: "MFA was activated",
      });
    }
    customError(500, "You can not active MFA for anther account");
  } catch (error) {
    next(error);
  }
};

const disableMFA = async (req, res, next) => {
  try {
    const { password, otp } = req.body;
    const user = await User.findById(req.user._id).populate("mfa");
    const checkPass = await bcrypt.compare(password, user.password);
    if (checkPass) {
      const mfa = user.mfa;
      const isValid = verifyOTPToken(otp, mfa.secretMFA);
      if (isValid) {
        await MFA.findOneAndUpdate({ status: "disable", secretMFA: null });
        return res.status(200).send({
          data: { isDisable: true },
          status: 200,
          message: "MFA was disabled",
        });
      }
      customError(500, "Oops...Your OTP code is not correct!");
    }
    customError(500, "Password incorrect");
  } catch (error) {
    next(error);
  }
};

const verifyOTP = async (req, res, next) => {
  try {
    const { otp } = req.body;
    const { user } = req;
    const mfa = await MFA.findOne({ owner: user._id });
    const isValid = verifyOTPToken(otp, mfa.secretMFA);
    if (isValid) {
      return res.status(200).send({
        data: { isValid },
        status: 200,
        message: "Verify OTP code successfully",
      });
    }
    customError(500, "Oops...Your OTP code is not correct!");
  } catch (error) {
    next(error);
  }
};
export { enableMFA, disableMFA, getQRCode, verifyOTP };
