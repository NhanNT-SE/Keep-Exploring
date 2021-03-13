import { createSlice } from "@reduxjs/toolkit";
const commonSlice = createSlice({
  name: "common",
  initialState: {
    isError: false,
    isLoading: false,
    isSuccess: false,
    message: "",
    isShowDialog: false,
    isOpenDrawer: false,
  },
  reducers: {
    actionFailed: (state, action) => {
      state.isError = true;
      state.isLoading = false;
      state.isSuccess = false;
      state.message = action.payload;
    },
    actionLoading: (state, action) => {
      state.isLoading = true;
      state.isError = false;
      state.isSuccess = false;
      state.message = action.payload;
    },
    actionSuccess: (state, action) => {
      state.isError = false;
      state.isLoading = false;
      state.isSuccess = true;
      state.message = action.payload;
    },
    actionShowDialog: (state) => {
      state.isShowDialog = true;
    },
    actionHideDialog: (state) => {
      state.isShowDialog = false;
    },
    actionOpenDrawer: (state) => {
      state.isOpenDrawer = true;
    },
    actionCloseDrawer: (state) => {
      state.isOpenDrawer = false;
    },
  },
});

export const {
  actionFailed,
  actionLoading,
  actionSuccess,
  actionShowDialog,
  actionHideDialog,
  actionOpenDrawer,
  actionCloseDrawer,
} = commonSlice.actions;
export default commonSlice.reducer;