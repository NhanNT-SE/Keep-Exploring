import axiosClient from "./axiosClient";

const userApi = {
  getAllUser: () => {
    const url = "/user/list";
    return axiosClient.get(url);
  },
  getUser: (userId) => {
    const url = `/user/${userId}`;
    return axiosClient.get(url);
  },
  login: (user) => {
    const url = "/user/signIn";
    return axiosClient.post(url, user);
  },
  logout: (userId) => {
    const url = "/user/logout";
    return axiosClient.get(url);
  },
  refreshToken: (data) => {
    const url = "/refreshToken";
    return axiosClient.post(url, data);
  },
};
export default userApi;
