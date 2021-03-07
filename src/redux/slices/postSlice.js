import { createSlice } from "@reduxjs/toolkit";
const postSlice = createSlice({
  name: "post",
  initialState: {
    postList: [],
    activeId: "",
    isLoading: false,
    err: "",
  },
  reducers: {
    actionGetAllPost: (state, action) => {
      state.isLoading = true;
    },
    actionSetPosts: (state, action) => {
      state.postList = action.payload;
      state.isLoading = false;
      state.err = "";
    },
    actionGetPostSuccess: (state) => {
      state.isLoading = false;
      state.err = "";
    },
    actionGetPostFail: (state, action) => {
      state.isLoading = false;
      state.err = action.payload;
    },
  },
});
export const {
  actionGetAllPost,
  actionGetPostFail,
  actionGetPostSuccess,
  actionSetPosts,
} = postSlice.actions;
export default postSlice.reducer;
