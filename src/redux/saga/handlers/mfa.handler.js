import mfaApi from "api/mfa.api";
import { call, put } from "redux-saga/effects";
import {
  handlerFailSaga,
  handlerSuccessSaga,
} from "redux/saga/handlers/common.handler";
import { actionSetUser } from "redux/slices/auth.slice";
import { actionLoading } from "redux/slices/common.slice";
import { actionHideDialog } from "redux/slices/dialog.slice";
import { actionSetProfile } from "redux/slices/profile.slice";
import rootStore from "rootStore";
import { DIALOG } from "utils/global_variable";
import localStorageService from "utils/localStorageService";
const userStorage = JSON.parse(localStorageService.getUser());
export function* handlerEnableMFA(action) {
  try {
    const rootState = rootStore.getState();
    const { userId, otp } = action.payload;
    const { profile } = rootState.profile;
    yield put(actionLoading("Loading enable MFA...!"));
    const isValid = yield call(() => handlerVerifyOTP({ payload: otp }));
    if (isValid) {
      yield call(() => mfaApi.enableMFA(userId));
      const user = { ...userStorage, mfa: "enable" };
      localStorageService.setUser(user);
      yield put(actionSetUser(user));
      yield put(actionSetProfile({ ...profile, mfa: "enable" }));
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
    const rootState = rootStore.getState();
    const { password, otp } = action.payload;
    const { profile } = rootState.profile;
    yield put(actionLoading("Loading disable MFA...!"));
    yield call(() => mfaApi.disableMFA(password, otp));
    const user = { ...userStorage, mfa: "disable" };
    localStorageService.setUser(user);
    yield put(actionSetUser(user));
    yield put(actionSetProfile({ ...profile, mfa: "disable" }));
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
