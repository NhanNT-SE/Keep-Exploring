import { takeLatest } from "@redux-saga/core/effects";
import {
  handlerDisableMFA,
  handlerEnableMFA,
  handlerVerifyOTP,
} from "redux/saga/handlers/mfa.handler";
import {
  actionDisableMFA,
  actionEnableMFA,
  actionVerifyOTP,
} from "redux/slices/mfa.slice";

export function* sagaVerifyOTP() {
  yield takeLatest(actionVerifyOTP.type, handlerVerifyOTP);
}
export function* sagaEnableMFA() {
  yield takeLatest(actionEnableMFA.type, handlerEnableMFA);
}
export function* sagaDisableMFA() {
  yield takeLatest(actionDisableMFA.type, handlerDisableMFA);
}
// export function* sagaEnableMFA() {
//   yield takeLatest(actionEnableMFA.type, handlerRefreshToken);
// }
// export function* sagaDisableMFA() {
//   yield takeLatest(actionDisableMFA.type, handlerRefreshToken);
// }
