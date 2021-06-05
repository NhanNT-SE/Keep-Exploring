import { createSlice } from "@reduxjs/toolkit";
const mfaSlice = createSlice({
  name: "mfa",
  initialState: {},
  reducers: {
    actionVerifyOTP() {},
    actionEnableMFA() {},
    actionDisableMFA() {},
  },
});
export const {
  actionEnableMFA,
  actionDisableMFA,
  actionVerifyOTP,
} = mfaSlice.actions;
export default mfaSlice.reducer;
