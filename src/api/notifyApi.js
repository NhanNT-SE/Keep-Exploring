import axiosClient from "./axiosClient";
const notifyApi = {
  sendNotify: (body) => {
    const url = "/admin/send-notify";
    return axiosClient.post(url, body);
  },
  sendMultiNotify: (body) => {
    const url = "/notification/admin";
    return axiosClient.post(url, body);
  },
};
export default notifyApi;
