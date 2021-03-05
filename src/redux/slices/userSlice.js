import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: {
    user: null,
    loading: false,
    msg: "",
    err: "",
  },
  reducers: {
    actionLogin(state) {
      state.loading = true;
    },
    actionLogout(state) {
      state.loading = true;
      state.msg = "Logout successfully";
    },
    actionSetErr(state, action) {
      const { payload } = action;
      state.loading = false;
      state.err = payload;
    },
    actionSetUser(state, action) {
      const user = action.payload;
      state.user = user;
      state.msg = "Login successfully";
      state.loading = false;
      state.err = "";
    },
  },
});
export const {
  actionLogin,
  actionLogout,
  actionSetUser,
  actionSetErr,
} = userSlice.actions;
export default userSlice.reducer;
