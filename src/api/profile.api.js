import axiosClient from "./axiosClient";

const profileApi = {
  changePassword: (data) => {
    const url = "/user/changePass";
    return axiosClient.patch(url, data);
  },
  getProfile: () => {
    const url = "/user/profile";
    return axiosClient.get(url);
  },
  updateProfile: (data) => {
    const url = "/user/profile";
    const formData = new FormData();
    for (const [key, value] of Object.entries(data)) {
      formData.append(key, value);
    }
    return axiosClient.patch(url, formData, {
      headers: { "content-type": "multipart/form-data" },
    });
  },
};
export default profileApi;
