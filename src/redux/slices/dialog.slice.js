import { createSlice } from "@reduxjs/toolkit";
import { DIALOG } from "utils/global_variable";
const dialogSlice = createSlice({
  name: "dialog",
  initialState: {
    dialogMessage: false,
    dialogEditPost: false,
    dialogNotify: false,
    dialogDeleteUser: false,
    dialogChangePassword: false,
    dialogEnableMFA: false,
    dialogDisableMFA: false,
    dialogVerifyOTP: true,
  },
  reducers: {
    actionShowDialog: (state, action) => {
      const typeDialog = action.payload;
      switch (typeDialog) {
        case DIALOG.DIALOG_MESSAGE:
          state.dialogMessage = true;
          break;
        case DIALOG.DIALOG_EDIT_POST:
          state.dialogEditPost = true;
          break;
        case DIALOG.DIALOG_NOTIFY:
          state.dialogNotify = true;
          break;
        case DIALOG.DIALOG_DELETE_USER:
          state.dialogDeleteUser = true;
          break;
        case DIALOG.DIALOG_CHANGE_PASSWORD:
          state.dialogChangePassword = true;
          break;
        case DIALOG.DIALOG_ENABLE_MFA:
          state.dialogEnableMFA = true;
          break;
        case DIALOG.DIALOG_DISABLE_MFA:
          state.dialogDisableMFA = true;
          break;
        case DIALOG.DIALOG_VERIFY_OTP:
          state.dialogVerifyOTP = true;
          break;
        default:
          break;
      }
    },
    actionHideDialog: (state, action) => {
      const typeDialog = action.payload;
      switch (typeDialog) {
        case DIALOG.DIALOG_MESSAGE:
          state.dialogMessage = false;
          break;
        case DIALOG.DIALOG_EDIT_POST:
          state.dialogEditPost = false;
          break;
        case DIALOG.DIALOG_NOTIFY:
          state.dialogNotify = false;
          break;
        case DIALOG.DIALOG_DELETE_USER:
          state.dialogDeleteUser = false;
          break;
        case DIALOG.DIALOG_CHANGE_PASSWORD:
          state.dialogChangePassword = false;
          break;
        case DIALOG.DIALOG_ENABLE_MFA:
          state.dialogEnableMFA = false;
          break;
        case DIALOG.DIALOG_DISABLE_MFA:
          state.dialogDisableMFA = false;
          break;
        case DIALOG.DIALOG_VERIFY_OTP:
          state.dialogVerifyOTP = false;
          break;
        default:
          break;
      }
    },
  },
});

export const { actionShowDialog, actionHideDialog } = dialogSlice.actions;
export default dialogSlice.reducer;
