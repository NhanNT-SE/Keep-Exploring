import axiosClient from "./axiosClient";

const mfaAPI = {
  enableMFA: (password) => {
    const url = "/mfa/enable";
    return axiosClient.post(url, password);
  },
  disableMFA: (password) => {
    const url = "/mfa/disable";
    return axiosClient.post(url, password);
  },
  verifyOTP: (otp) => {
    const url = "/mfa/verify";
    return axiosClient.post(url, otp);
  },
};

export default mfaAPI;
