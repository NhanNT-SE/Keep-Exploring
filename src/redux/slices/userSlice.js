import { createSlice } from "@reduxjs/toolkit";
import localStorageService from "api/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());
const userSlice = createSlice({
  name: "user",
  initialState: {
    user: userStorage ? userStorage : null,
    isRefreshingToken: false,
  },
  reducers: {
    actionLogin() {},
    actionLogout() {},
    actionRefreshToken() {},
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
  },
});
export const {
  actionLogin,
  actionLogout,
  actionRefreshToken,
  actionRefreshTokenEnded,
  actionRefreshTokenStarted,
  actionSetUser,
} = userSlice.actions;
export default userSlice.reducer;
