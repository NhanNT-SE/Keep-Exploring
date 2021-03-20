import axiosClient from "./axiosClient";

const postApi = {
  deletePost: (postId) => {
    const url = `/post/delete/${postId}`;
    return axiosClient.delete(url);
  },
  getAll: () => {
    const url = "/post";
    return axiosClient.get(url);
  },
  getCommentList: (postId) => {
    const url = `/comment/getByPost/${postId}`;
    return axiosClient.get(url);
  },

  getPost: (postId) => {
    const url = `/post/${postId}`;
    return axiosClient.get(url);
  },
  updatePost: (body) => {
    const url = "/post/status";
    return axiosClient.patch(url, body);
  },
};

export default postApi;
