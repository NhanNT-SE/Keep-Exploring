import { delay, put } from "@redux-saga/core/effects";
import {
  actionFailed,
  actionHideDialog,
  actionShowDialog,
  actionSuccess,
} from "redux/slices/commonSlice";

export function* handlerSuccessSaga(message) {
  yield put(actionSuccess(message));
  yield put(actionShowDialog());
  yield delay(2000);
  yield put(actionHideDialog());
}
export function* handlerFailSaga(error) {
  yield put(actionFailed(error.message));
  yield put(actionShowDialog());
  yield delay(2000);
  yield put(actionHideDialog());
}
