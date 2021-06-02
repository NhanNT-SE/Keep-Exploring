import { takeLatest } from "@redux-saga/core/effects";
import {
  actionLogin,
  actionLogout,
  actionRefreshToken,
} from "redux/slices/auth.slice";

import {
  handleLogout,
  handlerLogin,
  handlerRefreshToken,
} from "../handlers/auth.handler";

export function* sagaLogin() {
  yield takeLatest(actionLogin.type, handlerLogin);
}
export function* sagaLogout() {
  yield takeLatest(actionLogout.type, handleLogout);
}
export function* sagaRefreshToken() {
  yield takeLatest(actionRefreshToken.type, handlerRefreshToken);
}
