import axiosClient from "./axiosClient";

const userApi = {
  login: (user) => {
    const url = "/user/signIn";
    return axiosClient.post(url, user);
  },
  logout: () => {
    const url = "/user/logout";
    return axiosClient.get(url);
  },
  refreshToken: (data) => {
    const url = "/refreshToken";
    return axiosClient.post(url, data);
  },
};
export default userApi;
