import axiosClient from "./axiosClient";

const postApi = {
  deletePost: (postId) => {
    const url = `/post/delete/${postId}`;
    return axiosClient.delete(url);
  },
  getAll: () => {
    const url = "/admin/posts";
    return axiosClient.get(url);
  },
  getPost: (postId) => {
    const url = `/public/post/${postId}`;
    return axiosClient.get(url);
  },
  updatePost: (body) => {
    const url = "/admin/update-status";
    return axiosClient.patch(url, body);
  },
};

export default postApi;
