import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
import GLOBAL_VARIABLE from "utils/global_variable";

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
