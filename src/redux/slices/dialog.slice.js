import { createSlice } from "@reduxjs/toolkit";
import GLOBAL_VARIABLE from "utils/global_variable";
const dialogSlice = createSlice({
  name: "dialog",
  initialState: {
    isShowDialogMessage: false,
    isShowDialogEditPost: false,
    isShowDialogNotify: false,
    isShowDialogDeleteUser: false,
    isShowDialogChangePassword: false,
    isShowDialogQRCode: true,
  },
  reducers: {
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
        case GLOBAL_VARIABLE.DIALOG_DELETE_USER:
          state.isShowDialogDeleteUser = true;
          break;
        case GLOBAL_VARIABLE.DIALOG_CHANGE_PASSWORD:
          state.isShowDialogChangePassword = true;
          break;
        case GLOBAL_VARIABLE.DIALOG_QR_CODE:
          state.isShowDialogQRCode = true;
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
        case GLOBAL_VARIABLE.DIALOG_DELETE_USER:
          state.isShowDialogDeleteUser = false;
          break;
        case GLOBAL_VARIABLE.DIALOG_CHANGE_PASSWORD:
          state.isShowDialogChangePassword = false;
          break;
        case GLOBAL_VARIABLE.DIALOG_QR_CODE:
          state.isShowDialogQRCode = false;
          break;
        default:
          break;
      }
    },
  },
});

export const { actionShowDialog, actionHideDialog } = dialogSlice.actions;
export default dialogSlice.reducer;
