const { createSlice } = require("@reduxjs/toolkit");

const blogSlice = createSlice({
  name: "blog",
  initialState: {
    blogList: [],
    selectedBlog: null,
  },
  reducers: {
    actionGetAllBlog: () => {},
    actionGetBlog: () => {},
    actionDeleteBlog: () => {},
    actionUpdateBlog: () => {},
    actionDeleteComment: () => {},
    actionSetSelectedBlog: (state, action) => {
      state.selectedBlog = action.payload;
    },
    actionSetBlogList: (state, action) => {
      state.blogList = action.payload;
    },
  },
});

export const {
  actionDeleteBlog,
  actionDeleteComment,
  actionGetAllBlog,
  actionSetBlogList,
  actionGetBlog,
  actionSetSelectedBlog,
  actionUpdateBlog,
} = blogSlice.actions;

export default blogSlice.reducer;
