import { createSlice } from "@reduxjs/toolkit";
const profileSlice = createSlice({
  name: "profile",
  initialState: {profile:{}},
  reducers: {
    actionChangePassword() {},
    actionGetMyProfile() {},
    actionUpdateProfile() {},
    actionSetProfile(state, action) {
      state.profile = action.payload;
    },
  },
});
export const {
  actionChangePassword,
  actionGetMyProfile,
  actionUpdateProfile,
  actionSetProfile,
} = profileSlice.actions;
export default profileSlice.reducer;
