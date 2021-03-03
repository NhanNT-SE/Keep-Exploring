import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: {
    user: null,
    loading: false,
  },
  reducers: {
    actionLogin(state) {
      state.loading = true;
    },
    actionSetUser(state, action) {
      const user = action.payload;
      state.user = user;
      state.loading = false;
    },
    actionLogout(state) {
      state.loading = true;
    },
  },
});
export const { actionLogin, actionLogout, actionSetUser } = userSlice.actions;
export default userSlice.reducer;
