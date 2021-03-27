import axiosClient from "./axiosClient";

const userApi = {
  changePassword: (data) => {
    const url = "/user/changePass";
    return axiosClient.patch(url, data);
  },
  deleteUser: (userId) => {
    const url = `/admin/user/${userId}`;
    return axiosClient.delete(url);
  },
  getAllUser: () => {
    const url = "/admin/users";
    return axiosClient.get(url);
  },
  getUser: (userId) => {
    const url = `/user/${userId}`;
    return axiosClient.get(url);
  },
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
