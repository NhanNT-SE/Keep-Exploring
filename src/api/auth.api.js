import axiosClient from "./axiosClient";
const authApi = {
  login: (user) => {
    const url = "/auth/sign-in";
    return axiosClient.post(url, user);
  },
  logout: (payload) => {
    const url = "/auth/sign-out";
    return axiosClient.post(url, payload);
  },
  refreshToken: (data) => {
    const url = "/auth/refresh-token";
    return axiosClient.post(url, data);
  },
};
export default authApi;
