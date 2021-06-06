import authApi from "api/auth.api";
import axiosClient from "api/axiosClient";
import { call, put, take } from "redux-saga/effects";
import {
  handlerFailSaga,
  handlerSuccessSaga,
} from "redux/saga/handlers/common.handler";
import { actionLogout, actionSetUser } from "redux/slices/auth.slice";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/common.slice";
import { actionShowDialog } from "redux/slices/dialog.slice";
import { actionVerifyOTP } from "redux/slices/mfa.slice";
import rootStore from "rootStore";
import { CONFIG_URL, DIALOG } from "utils/global_variable";
import localStorageService from "utils/localStorageService";

export function* handlerLogin(action) {
  try {
    yield put(actionLoading("Loading login user ...!"));
    const response = yield call(() => authApi.login(action.payload));
    const { data } = response;
    const user = data.user;
    user.avatar = `${CONFIG_URL.BASE_URL_IMAGE}/user/${user.avatar}`;
    if (user.mfa === "enable") {
      yield put(actionSuccess());
      yield put(actionShowDialog(DIALOG.DIALOG_VERIFY_OTP));
      while (true) {
        const { payload } = yield take(actionVerifyOTP);
        const isValid = yield call(() =>
          handlerVerifyOTPLogin(payload, user._id)
        );
        if (isValid) {
          yield call(() => successLogin(data, user));
        }
        yield take(actionLogout);
      }
    } else {
      if (user.role !== "admin") {
        yield call(() =>
          handlerFailSaga("Bạn không đủ quyền để truy cập vào hệ thống!!!!!")
        );
      } else {
        yield call(() => successLogin(data, user));
      }
    }
  } catch (error) {
    console.log("user saga error: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
export function* handleLogout(action) {
  try {
    yield put(actionLoading("Loading logout user ...!"));
    yield call(() => {
      authApi.logout(action.payload);
    });
    localStorageService.clearStorage();
    localStorageService.clearUser();
    yield call(() => handlerSuccessSaga("Logout successfully!"));
    yield put(actionSetUser(null));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
export function* handlerRefreshToken(action) {
  try {
    if (action.payload > 7) {
      const userStorage = JSON.parse(localStorageService.getUser());
      yield put(actionLoading("Loading logout user ...!"));
      yield call(() => {
        authApi.logout({ userId: userStorage._id });
      });
      localStorageService.clearStorage();
      localStorageService.clearUser();
      yield call(() => handlerFailSaga("Vui lòng đăng nhập lại để tiếp tục"));
      yield put(actionSetUser(null));
    } else {
      yield put(actionLoading("Loading refresh token ...!"));
      const userState = rootStore.getState();
      const { user } = userState.user;
      const refreshToken = localStorageService.getRefreshToken();
      const latestAction = localStorageService.getLatestAction();
      const response = yield call(() =>
        authApi.refreshToken({ userId: user._id, refreshToken })
      );
      const { data } = response;
      localStorageService.setAccessToken(data);
      axiosClient.defaults.headers.common[
        "Authorization"
      ] = `Bearer ${localStorageService.getAccessToken()}`;
      yield put({ type: latestAction });
      yield call(() => actionSuccess("Refresh token successfully!"));
    }
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => actionFailed(error));
  }
}
function* handlerVerifyOTPLogin(otp, userId) {
  try {
    const response = yield call(() => authApi.verifyOTPLogin(otp, userId));
    const { isValid } = response.data;
    return isValid;
  } catch (error) {
    console.log("mfa saga error: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* successLogin(data, user) {
  localStorageService.setToken(data.accessToken, data.refreshToken);
  localStorageService.setUser(user);
  yield call(() => handlerSuccessSaga("Login successfully!"));
  yield put(actionSetUser(user));
}
