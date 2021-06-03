import { delay, put } from "@redux-saga/core/effects";
import { actionFailed, actionSuccess } from "redux/slices/common.slice";
import { actionHideDialog, actionShowDialog } from "redux/slices/dialog.slice";
import GLOBAL_VARIABLE from "utils/global_variable";

export function* handlerSuccessSaga(message) {
  yield put(actionSuccess(message));
  yield put(actionShowDialog(GLOBAL_VARIABLE.DIALOG_MESSAGE));
  yield delay(2000);
  yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_MESSAGE));
}
export function* handlerFailSaga(error) {
  yield put(actionFailed(error.message || error));
  yield put(actionShowDialog(GLOBAL_VARIABLE.DIALOG_MESSAGE));
  yield delay(2000);
  yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_MESSAGE));
}
