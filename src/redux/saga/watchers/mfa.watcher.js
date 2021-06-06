import { takeLatest } from "@redux-saga/core/effects";
import {
  handlerDisableMFA,
  handlerEnableMFA,
} from "redux/saga/handlers/mfa.handler";
import { actionDisableMFA, actionEnableMFA } from "redux/slices/mfa.slice";

export function* sagaEnableMFA() {
  yield takeLatest(actionEnableMFA.type, handlerEnableMFA);
}
export function* sagaDisableMFA() {
  yield takeLatest(actionDisableMFA.type, handlerDisableMFA);
}
