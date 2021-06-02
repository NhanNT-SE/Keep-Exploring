import axiosClient from "./axiosClient";

const blogApi = {
  deleteBlog: (blogId) => {
    const url = `/admin/blog/${blogId}`;
    return axiosClient.delete(url);
  },
  getAll: () => {
    const url = "/admin/blog";
    return axiosClient.get(url);
  },
  getBlog: (blogId) => {
    const url = `/public/blog/${blogId}`;
    return axiosClient.get(url);
  },
  updateBlog: (body) => {
    const url = `/admin/blog`;
    return axiosClient.patch(url, body);
  },
};

export default blogApi;
