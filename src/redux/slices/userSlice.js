import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());
const userSlice = createSlice({
  name: "user",
  initialState: {
    user: userStorage ? userStorage : null,
    isRefreshingToken: false,
    selectedUser: null,
    userList: [],
  },
  reducers: {
    actionDeleteUser() {},
    actionLogin() {},
    actionLogout() {},
    actionRefreshToken() {},
    actionGetListUser() {},
    actionGetUser() {},
    actionSendNotify() {},
    actionSendMultiNotify() {},
    actionRefreshTokenEnded(state) {
      state.isRefreshingToken = false;
    },
    actionRefreshTokenStarted: (state) => {
      state.isRefreshingToken = true;
    },
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
  actionDeleteUser,
  actionGetListUser,
  actionGetUser,
  actionLogin,
  actionLogout,
  actionRefreshToken,
  actionRefreshTokenEnded,
  actionRefreshTokenStarted,
  actionSetSelectedUser,
  actionSendNotify,
  actionSendMultiNotify,
  actionSetUser,
  actionSetUserList,
} = userSlice.actions;
export default userSlice.reducer;
