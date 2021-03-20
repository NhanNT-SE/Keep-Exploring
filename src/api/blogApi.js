import axiosClient from "./axiosClient";

const blogApi = {
  getAll: () => {
    const url = "/blog";
    return axiosClient.get(url);
  },
  getBlog: (blogId) => {
    const url = `/blog/${blogId}`;
    return axiosClient.get(url);
  },
};

export default blogApi;
