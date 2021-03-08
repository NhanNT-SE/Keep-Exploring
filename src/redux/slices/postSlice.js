import { createSlice } from "@reduxjs/toolkit";
const postSlice = createSlice({
  name: "post",
  initialState: {
    postList: [],
    activeId: "",
  },
  reducers: {
    actionGetAllPost: () => {},
    actionSetPostList: (state, action) => {
      state.postList = action.payload;
    },
  },
});
export const {
  actionGetAllPost,
  actionSetPostList,
} = postSlice.actions;
export default postSlice.reducer;
