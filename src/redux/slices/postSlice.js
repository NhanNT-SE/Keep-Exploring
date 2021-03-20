import { createSlice } from "@reduxjs/toolkit";
const postSlice = createSlice({
  name: "post",
  initialState: {
    postList: [],
    commentList: [],
    selectedPost: null,
    selectedPostList: null,
  },
  reducers: {
    actionDeletePost: () => {},
    actionGetAllPost: () => {},
    actionGetCommentList: () => {},
    actionGetPost: () => {},
    actionUpdatePost: () => {},
    actionSetPostList: (state, action) => {
      state.postList = action.payload;
    },
    actionSetSelectedPost: (state, action) => {
      state.selectedPost = action.payload;
    },
    actionSetCommentList: (state, action) => {
      state.commentList = action.payload;
    },
    actionSetSelectedPostList: (state, action) => {
      state.selectedPostList = action.payload;
    },
  },
});
export const {
  actionDeletePost,
  actionGetAllPost,
  actionGetCommentList,
  actionGetPost,
  actionSetPostList,
  actionSetCommentList,
  actionSetSelectedPost,
  actionSetSelectedPostList,
  actionUpdatePost,
} = postSlice.actions;
export default postSlice.reducer;
