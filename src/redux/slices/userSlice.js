import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());
const userSlice = createSlice({
  name: "user",
  initialState: {
    user: userStorage ? userStorage : null,
    selectedUser: null,
    userList: [],
  },
  reducers: {
    actionChangePassword() {},
    actionDeleteUser() {},
    actionLogin() {},
    actionLogout() {},
    actionRefreshToken() {},
    actionGetListUser() {},
    actionGetUser() {},
    actionSendNotify() {},
    actionUpdateProfile() {},
    actionSetUser(state, action) {
      const user = action.payload;
      state.user = user;
    },
    actionSetSelectedUser(state, action) {
      state.selectedUser = action.payload;
    },
    actionSetUserList(state, action) {
      state.userList = action.payload;
    },
  },
});
export const {
  actionChangePassword,
  actionDeleteUser,
  actionGetListUser,
  actionGetUser,
  actionLogin,
  actionLogout,
  actionRefreshToken,
  actionSetSelectedUser,
  actionSendNotify,
  actionSetUser,
  actionSetUserList,
  actionUpdateProfile,
} = userSlice.actions;
export default userSlice.reducer;
