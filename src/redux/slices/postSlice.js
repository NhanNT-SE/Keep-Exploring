import { createSlice } from "@reduxjs/toolkit";
const postSlice = createSlice({
  name: "post",
  initialState: {
    postList: [],
    selectedPost: null,
    selectedPostList: null,
  },
  reducers: {
    actionGetAllPost: () => {},
    actionSetPostList: (state, action) => {
      state.postList = action.payload;
    },
    actionSetSelectedPost: (state, action) => {
      state.selectedPost = action.payload;
    },
    actionSetSelectedPostList: (state, action) => {
      state.selectedPostList = action.payload;
    },
  },
});
export const {
  actionGetAllPost,
  actionSetPostList,
  actionSetSelectedPost,
  actionSetSelectedPostList,
} = postSlice.actions;
export default postSlice.reducer;
