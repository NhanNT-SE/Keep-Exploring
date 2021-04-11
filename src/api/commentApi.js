import axiosClient from "./axiosClient";
const commentApi = {
  deleteComment: (commentId) => {
    const url = `/comment/deletebyId/${commentId}`;
    return axiosClient.delete(url);
  },
  getCommentList:(id,type)=>{
    const url = `/public/${type}/comments/${id}`;
    return axiosClient.get(url);
  }
};
export default commentApi;
