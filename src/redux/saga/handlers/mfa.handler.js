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
    yield put(actionLoading("MFA is activating...!"));
    const isValid = yield call(() => handlerVerifyOTP({ payload: otp }));
    if (isValid) {
      yield call(() => mfaApi.enableMFA(userId));
      const user = { ...userStorage, mfa: "enable" };
      localStorageService.setUser(user);
      yield put(actionSetUser(user));
      yield call(() => handlerSuccessSaga("Your MFA was activated"));
      yield put(actionHideDialog(DIALOG.DIALOG_ENABLE_MFA));
    }
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
// export function* enableMFA(action) {
//   try {
//     const isValid = yield call(verifyToken, action.payload);
//     if (isValid) {
//       yield put(actionLoading("Loading enable MFA ...!"));
//       yield call(() => mfaApi.enableMFA(action.payload));
//       yield put(actionLoading("Enable MFA successfully...!"));
//     }
//   } catch (error) {
//     console.log("mfa saga error: ", error);
//     yield call(() => handlerFailSaga(error));
//   }
// }
