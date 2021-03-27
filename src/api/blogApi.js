import axiosClient from "./axiosClient";

const blogApi = {
  deleteBlog: (blogId) => {
    const url = `/blog/delete/${blogId}`;
    return axiosClient.delete(url);
  },
  getAll: () => {
    const url = "/admin/blogs";
    return axiosClient.get(url);
  },
  getBlog: (blogId) => {
    const url = `/public/blog/${blogId}`;
    return axiosClient.get(url);
  },
  updateBlog: (body) => {
    const url = "/admin/update-status";
    return axiosClient.patch(url, body);
  },
};

export default blogApi;
