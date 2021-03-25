import axiosClient from "./axiosClient";
const commentApi = {
  deleteComment: (commentId) => {
    const url = `/comment//deletebyId/${commentId}`;
    return axiosClient.delete(url);
  },
};
export default commentApi;
