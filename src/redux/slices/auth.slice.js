import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "utils/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());
const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: userStorage ? userStorage : null,
  },
  reducers: {
    actionLogin() {},
    actionLogout() {},
    actionRefreshToken() {},
    actionSetUser(state, action) {
      const user = action.payload;
      state.user = user;
    },
  },
});
export const { actionLogin, actionLogout, actionRefreshToken, actionSetUser } =
  authSlice.actions;
export default authSlice.reducer;
