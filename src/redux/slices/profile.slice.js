import { createSlice } from "@reduxjs/toolkit";
const profileSlice = createSlice({
  name: "profile",
  initialState: {},
  reducers: {
    actionChangePassword() {},
    actionGetMyProfile() {},
    actionUpdateProfile() {},
  },
});
export const { actionChangePassword, actionGetMyProfile, actionUpdateProfile } =
  profileSlice.actions;
export default profileSlice.reducer;
