import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
import GLOBAL_VARIABLE from "utils/global_variable";
const userStorage = JSON.parse(localStorageService.getUser());
const commonSlice = createSlice({
  name: "common",
  initialState: {
    isError: false,
    isLoading: false,
    isSuccess: false,
    message: "",
    isShowDialogMessage: false,
    isShowDialogEditPost: false,
    isShowDialogNotify: false,

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
    actionShowDialog: (state, action) => {
      const typeDialog = action.payload;
      switch (typeDialog) {
        case GLOBAL_VARIABLE.DIALOG_MESSAGE:
          state.isShowDialogMessage = true;
          break;
        case GLOBAL_VARIABLE.DIALOG_EDIT_POST:
          state.isShowDialogEditPost = true;
          break;
        case GLOBAL_VARIABLE.DIALOG_NOTIFY:
          state.isShowDialogNotify = true;
          break;
        default:
          break;
      }
    },
    actionHideDialog: (state, action) => {
      const typeDialog = action.payload;
      switch (typeDialog) {
        case GLOBAL_VARIABLE.DIALOG_MESSAGE:
          state.isShowDialogMessage = false;
          break;
        case GLOBAL_VARIABLE.DIALOG_EDIT_POST:
          state.isShowDialogEditPost = false;
          break;
        case GLOBAL_VARIABLE.DIALOG_NOTIFY:
          state.isShowDialogNotify = false;
          break;
        default:
          break;
      }
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
  actionShowDialog,
  actionHideDialog,
  actionOpenDrawer,
  actionCloseDrawer,
  actionSetIsRemember,
} = commonSlice.actions;
export default commonSlice.reducer;
