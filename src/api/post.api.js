import axiosClient from "./axiosClient";

const postApi = {
  deletePost: (postId) => {
    const url = `/admin/post/${postId}`;
    return axiosClient.delete(url);
  },
  getAll: () => {
    const url = "/admin/post";
    return axiosClient.get(url);
  },
  getPost: (postId) => {
    const url = `/public/post/${postId}`;
    return axiosClient.get(url);
  },
  updatePost: (body) => {
    const url = `/admin/post`;
    return axiosClient.patch(url, body);
  },
};

export default postApi;
