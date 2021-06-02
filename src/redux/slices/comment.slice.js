import { createSlice } from "@reduxjs/toolkit";

const commonSlice = createSlice({
  name: "comment",
  initialState: {
    commentList: null,
    likeList: null,
  },
  reducers: {
    actionDeleteComment: () => {},
    actionGetCommentList: () => {},
    actionGetLikeList: () => {},
    actionSetCommentList: (state, action) => {
      state.commentList = action.payload;
    },
    actionSetLikeList: (state, action) => {
      state.likeList = action.payload;
    },
  },
});
export const {
  actionDeleteComment,
  actionGetCommentList,
  actionGetLikeList,
  actionSetCommentList,
  actionSetLikeList,
} = commonSlice.actions;

export default commonSlice.reducer;
