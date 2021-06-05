import { createSlice } from "@reduxjs/toolkit";
const mfaSlice = createSlice({
  name: "mfa",
  initialState: {},
  reducers: {
    actionActiveMFA() {},
    actionVerifyOTP() {},
    actionEnableMFA() {},
    actionDisableMFA() {},
  },
});
export const {
  actionActiveMFA,
  actionEnableMFA,
  actionDisableMFA,
  actionVerifyOTP,
} = mfaSlice.actions;
export default mfaSlice.reducer;
