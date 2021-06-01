import qrcode from "qrcode";
import otplib from "otplib";
const { authenticator } = otplib;
const generateOTPToken = (username, serviceName, secret) => {
  return authenticator.keyuri(username, serviceName, secret);
};
const generateQRCode = async (otpAuth) => {
  try {
    const qrCode = await qrcode.toDataURL(otpAuth);
    return qrCode;
  } catch (error) {
    console.log("Could not generate QR code", error);
    return;
  }
};

const generateSecretUser = () => {
  return authenticator.generateSecret();
};
const verifyOTPToken = (token, secret) => {
  return authenticator.verify({ token, secret });
  // return authenticator.check(token, secret)
};
export { generateOTPToken, generateQRCode, generateSecretUser, verifyOTPToken };
