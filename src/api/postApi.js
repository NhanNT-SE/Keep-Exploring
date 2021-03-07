import axiosClient from "./axiosClient";

const postApi = {
  getAll: () => {
    const url = "/testAuth";
    return axiosClient.get(url);
  },
};

export default postApi;
