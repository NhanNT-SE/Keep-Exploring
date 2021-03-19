import axiosClient from "./axiosClient";

const postApi = {
  getAll: () => {
    const url = "/post";
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