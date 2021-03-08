import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: {
    user: null,
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
