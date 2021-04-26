import { createSlice } from "@reduxjs/toolkit";
const postSlice = createSlice({
  name: "post",
  initialState: {
    postList: [],
    selectedPost: null,
    selectedPostList: null,
    statisticsData: null,
    timeLineData: null,
  },
  reducers: {
    actionDeletePost: () => {},
    actionGetAllPost: () => {},
    actionGetPost: () => {},
    actionGetStatistics() {},
    actionGetTimeLineStatistics() {},

    actionUpdatePost: () => {},
    actionSetPostList: (state, action) => {
      state.postList = action.payload;
    },
    actionSetSelectedPost: (state, action) => {
      state.selectedPost = action.payload;
    },

    actionSetSelectedPostList: (state, action) => {
      state.selectedPostList = action.payload;
    },
    actionSetStatistics: (state, action) => {
      state.statisticsData = action.payload;
    },
    actionSetTimeLineStatistics: (state, action) => {
      state.timeLineData = action.payload;
    },
  },
});
export const {
  actionDeletePost,
  actionGetAllPost,
  actionGetCommentList,
  actionGetPost,
  actionSetPostList,
  actionSetSelectedPost,
  actionSetSelectedPostList,
  actionUpdatePost,
  actionGetStatistics,
  actionGetTimeLineStatistics,
  actionSetStatistics,
  actionSetTimeLineStatistics,
} = postSlice.actions;
export default postSlice.reducer;
