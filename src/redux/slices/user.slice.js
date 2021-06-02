import { createSlice } from "@reduxjs/toolkit";
const userSlice = createSlice({
  name: "user",
  initialState: {
    selectedUser: null,
    userList: [],
  },
  reducers: {
    actionDeleteUser() {},
    actionGetUserList() {},
    actionGetSelectedUser() {},
    actionSendNotify() {},
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
  actionGetUserList,
  actionGetSelectedUser,
  actionSetSelectedUser,
  actionSetUserList,
  actionSendNotify,
} = userSlice.actions;
export default userSlice.reducer;
