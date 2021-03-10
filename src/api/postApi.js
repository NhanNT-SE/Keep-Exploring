import axiosClient from "./axiosClient";

const postApi = {
  getAll: () => {
    const url = "/post";
    return axiosClient.get(url);
  },
};

export default postApi;
