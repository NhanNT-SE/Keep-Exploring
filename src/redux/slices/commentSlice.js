import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
import GLOBAL_VARIABLE from "utils/global_variable";

const commonSlice = createSlice({
  name: "comment",
  initialState: {
    commentList: null,
  },
  reducers: {
    actionDeleteComment: () => {},
    actionGetCommentList: () => {},
    actionSetCommentList: (state, action) => {
      state.commentList = action.payload;
    },
  },
});
export const {
  actionDeleteComment,
  actionGetCommentList,
  actionSetCommentList
} = commonSlice.actions;

export default commonSlice.reducer;
