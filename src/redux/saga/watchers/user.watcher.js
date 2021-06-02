import { takeLatest } from "@redux-saga/core/effects";
import {
  actionDeleteUser,
  actionGetSelectedUser,
  actionGetUserList,
  actionSendNotify,
} from "redux/slices/user.slice";
import {
  handlerDeleteUser,
  handlerGetSelectedUser,
  handlerGetUserList,
  handlerSendNotify,
} from "../handlers/user.handler";

export function* sagaDeleteUser() {
  yield takeLatest(actionDeleteUser.type, handlerDeleteUser);
}
export function* sagaGetSelectedUser() {
  yield takeLatest(actionGetSelectedUser.type, handlerGetSelectedUser);
}
export function* sagaGetUserList() {
  yield takeLatest(actionGetUserList.type, handlerGetUserList);
}
export function* sagaSendNotify() {
  yield takeLatest(actionSendNotify.type, handlerSendNotify);
}
