import axiosClient from "./axiosClient";

const mfaAPI = {
  disableMFA: (password, otp) => {
    const url = "/mfa/disable";
    return axiosClient.patch(url, { password, otp });
  },
  enableMFA: (userId) => {
    const url = "/mfa/enable";
    return axiosClient.patch(url, { userId });
  },
  getQRCode: (password) => {
    const url = "/mfa/qr-code";
    return axiosClient.post(url, { password });
  },
  verifyOTP: (otp) => {
    const url = "/mfa/verify";
    return axiosClient.post(url, { otp });
  },
};

export default mfaAPI;
