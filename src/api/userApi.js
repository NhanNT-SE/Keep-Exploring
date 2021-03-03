import axiosClient from "./axiosClient";

const userApi = {
  getAll: () => {
    const url = "/";
    return axiosClient.get(url);
  },
  getById: (user) => {
    const url = "/";
    return axiosClient.post(url, user);
  },
};
export default userApi;
