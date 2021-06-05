import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());
const commonSlice = createSlice({
  name: "common",
  initialState: {
    isError: false,
    isLoading: false,
    isSuccess: false,
    message: "",
    isOpenDrawer: false,
    isRemember: userStorage ? userStorage.remember : false,
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
    actionOpenDrawer: (state) => {
      state.isOpenDrawer = true;
    },
    actionCloseDrawer: (state) => {
      state.isOpenDrawer = false;
    },
    actionSetIsRemember: (state, action) => {
      state.isRemember = action.payload;
    },
  },
});

export const {
  actionFailed,
  actionLoading,
  actionSuccess,
  actionOpenDrawer,
  actionCloseDrawer,
  actionSetIsRemember,
} = commonSlice.actions;
export default commonSlice.reducer;
