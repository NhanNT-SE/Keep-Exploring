import axiosClient from "./axiosClient";

const userApi = {
  login: (user) => {
    const url = "/login";
    return axiosClient.post(url, user);
  },
  logout: (payload) => {
    const url = "/logout";
    return axiosClient.post(url, payload);
  },
  refreshToken: (data) => {
    const url = "/refresh-token";
    return axiosClient.post(url, data);
  },
};
export default userApi;
