import axiosClient from "./axiosClient";

const blogApi = {
  deleteBlog: (blogId) => {
    const url = `/blog/delete/${blogId}`;
    return axiosClient.delete(url);
  },
  getAll: () => {
    const url = "/blog";
    return axiosClient.get(url);
  },
  getBlog: (blogId) => {
    const url = `/blog/${blogId}`;
    return axiosClient.get(url);
  },
  updateBlog: (body) => {
    const url = "/blog/status";
    return axiosClient.patch(url, body);
  },
};

export default blogApi;
