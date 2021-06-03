import bcrypt from "bcryptjs";
import { customError } from "../../helpers/CustomError.js";
import { User } from "../../models/User.js";
import {
  generateOTPToken,
  generateQRCode,
  generateSecretUser,
} from "../../helpers/MFAHelper.js";
import { SERVER_NAME } from "../../config/index.js";
import { MFA } from "../../models/MFA.js";

const enableMFA = async (req, res, next) => {
  try {
    const { password } = req.body;
    const user = await User.findById(req.user._id).populate("mfa");
    const checkPass = await bcrypt.compare(password, user.password);
    if (checkPass) {
      const mfa = user.mfa;
      if (mfa.status === "idle") {
        const secretMFA = generateSecretUser();
        await MFA.findOneAndUpdate({ secretMFA });
        const qrCode =  await getQRCode(secretMFA, user.username);
        return res.status(200).send({
          data: { qrCode },
          status: 200,
          message: "QR code was generated",
        });
      }

      if (mfa.status === "enable") {
        return res.status(200).send({
          data: { isEnable: false },
          status: 200,
          message: "Your already enable MFA",
        });
      }
      return res.status(200).send({
        data: { isEnable: true },
        status: 200,
        message: "MFA was enabled",
      });
    }
    customError(500, "Password incorrect");
  } catch (error) {
    next(error);
  }
};
const disableMFA = async (req, res, next) => {
  try {
    const { password } = req.body;
    const user = await User.findById(req.user._id).populate("mfa");
    const checkPass = await bcrypt.compare(password, user.password);
    if (checkPass) {
      if (user.mfa.status === "enable") {
        await MFA.findOneAndUpdate({ status: "disable" });
        return res.status(200).send({
          data: { isDisable: true },
          status: 200,
          message: "MFA was disabled",
        });
      }
      return res.status(200).send({
        data: { isDisable: false },
        status: 200,
        message: "Your already disable MFA",
      });
    }
    customError(500, "Password incorrect");
  } catch (error) {
    next(error);
  }
};
const getQRCode = async (secretMFA, username) => {
  const otpAuth = generateOTPToken(username, SERVER_NAME, secretMFA);
  const QRCode = await generateQRCode(otpAuth);
  return QRCode;
};
const verifyOTP = async (req, res, next) => {
  try {
    const { otp } = req.body;
    const { user } = req;
    const mfa = await MFA.findOne({ owner: user._id });
    const isValid = mfaHelper.verifyOTPToken(otp, mfa.secretMFA);
    if (isValid) {
      if (mfa.status === "idle") {
        await MFA.findOneAndUpdate({ owner: user._id }, { status: "enable" });
        return res.status(200).send({
          data: { isValid, status: "enable" },
          status: 200,
          message: "MFA was enabled",
        });
      }
      return res.status(200).send({
        data: { isValid },
        status: 200,
        message: "Verify OTP code successfully",
      });
    }
    return res.status(200).send({
      data: { isValid },
      status: 200,
      message: "Your OTP code is incorrect!",
    });
  } catch (error) {
    next(error);
  }
};
export { enableMFA, disableMFA, verifyOTP };
