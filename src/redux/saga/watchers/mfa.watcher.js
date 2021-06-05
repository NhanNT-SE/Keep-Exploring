import { takeLatest } from "@redux-saga/core/effects";
import {
  handlerEnableMFA,
  handlerVerifyOTP,
} from "redux/saga/handlers/mfa.handler";
import { actionActiveMFA, actionVerifyOTP } from "redux/slices/mfa.slice";

export function* sagaVerifyOTP() {
  yield takeLatest(actionVerifyOTP.type, handlerVerifyOTP);
}
export function* sagaActiveMFA() {
  yield takeLatest(actionActiveMFA.type, handlerEnableMFA);
}
// export function* sagaEnableMFA() {
//   yield takeLatest(actionEnableMFA.type, handlerRefreshToken);
// }
// export function* sagaDisableMFA() {
//   yield takeLatest(actionDisableMFA.type, handlerRefreshToken);
// }
