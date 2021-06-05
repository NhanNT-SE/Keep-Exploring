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
import { customResponse } from "../../helpers/CustomResponse.js";

const getQRCode = async (req, res, next) => {
  try {
    const { password } = req.body;
    const user = await User.findById(req.user._id).populate("mfa");
    const mfa = await MFA.findOne({ owner: user._id });
    const checkPass = await bcrypt.compare(password, user.password);
    if (checkPass) {
      if (mfa.status === "disable") {
        const secretMFA = generateSecretUser();
        await mfa.updateOne({ secretMFA });
        const otpAuth = generateOTPToken(user.username, SERVER_NAME, secretMFA);
        const qrCode = await generateQRCode(otpAuth);
        return res.send(customResponse(qrCode, "QR code was generated"));
      }
      customError("MFA is already active");
    }
    customError("Your password incorrect");
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
      return res.send(customResponse({ isEnable: true }, "MFA was activated"));
    }
    customError("You can not active MFA for anther account");
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
        await MFA.findOneAndUpdate(
          { owner: user._id },
          { status: "disable", $unset: { secretMFA: 1 } }
        );
        return res.send(
          customResponse({ isDisable: true }, "MFA was disabled")
        );
      }
      customError("Your OTP code is not correct!");
    }
    customError("Password incorrect");
  } catch (error) {
    next(error);
  }
};

const verifyOTP = async (req, res, next) => {
  try {
    const { otp } = req.body;
    const { user } = req;
    const mfa = await MFA.findOne({ owner: user._id });
    if (mfa.secretMFA) {
      const isValid = verifyOTPToken(otp, mfa.secretMFA);
      if (isValid) {
        return res.send(
          customResponse({ isValid }, "Verify OTP code successfully")
        );
      }
      customError("Oops...Your OTP code is not correct!");
    }
    customError("Your MFA not enabled");
  } catch (error) {
    next(error);
  }
};
export { enableMFA, disableMFA, getQRCode, verifyOTP };
