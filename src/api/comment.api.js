import axiosClient from "./axiosClient";
const commentApi = {
  deleteComment: (commentId) => {
    const url = `/comment/deletebyId/${commentId}`;
    return axiosClient.delete(url);
  },
  getCommentList: (id, type) => {
    const url = `/public/${type}/comments/${id}`;
    return axiosClient.get(url);
  },
  getLikeList: (body, type) => {
    const url = `/public/${type}/like`;
    return axiosClient.post(url, body);
  },
};
export default commentApi;
