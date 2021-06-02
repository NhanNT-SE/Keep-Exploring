import axiosClient from "./axiosClient";

const userApi = {
  deleteUser: (userId) => {
    const url = `/admin/user/${userId}`;
    return axiosClient.delete(url);
  },
  getAllUser: () => {
    const url = "/admin/user/users";
    return axiosClient.get(url);
  },
  getUser: (userId) => {
    const url = `/admin/user/${userId}`;
    return axiosClient.get(url);
  },
};
export default userApi;
