import mfaApi from "api/mfa.api";
import { call, put } from "redux-saga/effects";
import {
  handlerFailSaga,
  handlerSuccessSaga,
} from "redux/saga/handlers/common.handler";
import { actionSetUser } from "redux/slices/auth.slice";
import { actionLoading } from "redux/slices/common.slice";
import { actionHideDialog } from "redux/slices/dialog.slice";
import { DIALOG } from "utils/global_variable";
import localStorageService from "utils/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());

export function* handlerEnableMFA(action) {
  try {
    const { userId, otp } = action.payload;
    yield put(actionLoading("Loading enable MFA...!"));
    const isValid = yield call(() => handlerVerifyOTP({ payload: otp }));
    if (isValid) {
      yield call(() => mfaApi.enableMFA(userId));
      const user = { ...userStorage, mfa: "enable" };
      yield put(actionSetUser(user));
      localStorageService.setUser(user);
      yield call(() => handlerSuccessSaga("Your MFA was enabled"));
      yield put(actionHideDialog(DIALOG.DIALOG_ENABLE_MFA));
    }
  } catch (error) {
    console.log("mfa saga error: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
export function* handlerDisableMFA(action) {
  try {
    const { password, otp } = action.payload;
    yield put(actionLoading("Loading disable MFA...!"));
    yield call(() => mfaApi.disableMFA(password, otp));
    const user = { ...userStorage, mfa: "disable" };
    yield put(actionSetUser(user));
    localStorageService.setUser(user);
    yield call(() => handlerSuccessSaga("Your MFA was disabled"));
    yield put(actionHideDialog(DIALOG.DIALOG_DISABLE_MFA));
  } catch (error) {
    console.log("mfa saga error: ", error);
    yield call(() => handlerFailSaga(error));
  }
}

export function* handlerVerifyOTP(action) {
  try {
    const response = yield call(() => mfaApi.verifyOTP(action.payload));
    const { isValid } = response.data;
    return isValid;
  } catch (error) {
    console.log("mfa saga error: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
