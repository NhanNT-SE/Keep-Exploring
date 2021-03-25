import axiosClient from "./axiosClient";

const userApi = {
  changePassword: (data) => {
    const url = "/user/changePass";
    return axiosClient.patch(url, data);
  },
  deleteUser: (userId) => {
    const url = `/user/delete/${userId}`;
    return axiosClient.delete(url);
  },
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
  updateProfile: (data) => {
    const url = "/user";

    const formData = new FormData();

    for (const [key, value] of Object.entries(data)) {
      formData.append(key, value);
    }

    return axiosClient.patch(url, formData, {
      headers: { "content-type": "multipart/form-data" },
    });
  },
};
export default userApi;
