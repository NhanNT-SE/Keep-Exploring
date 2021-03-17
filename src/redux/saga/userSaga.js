import axiosClient from "api/axiosClient";
import localStorageService from "utils/localStorageService";
import userApi from "api/userApi";
import GLOBAL_VARIABLE from "utils/global_variable";
import { call, put, takeLatest } from "redux-saga/effects";
import { handlerFailSaga, handlerSuccessSaga } from "redux/saga/commonSaga";
import { actionLoading } from "redux/slices/commonSlice";
import {
  actionLogin,
  actionLogout,
  actionRefreshToken,
  actionRefreshTokenEnded,
  actionSetUser,
} from "redux/slices/userSlice";
import rootStore from "rootStore";

function* handlerLogin(action) {
  try {
    const rootState = rootStore.getState();
    const { isRemember } = rootState.common;
    yield put(actionLoading("Loading login user ...!"));
    const response = yield call(() => userApi.login(action.payload));
    const { data } = response;
    data.imageUser = `${GLOBAL_VARIABLE.URL_IMAGE}/user/${data.imageUser}`;
    if (data.role !== "admin") {
      yield call(() =>
        handlerFailSaga("Bạn không đủ quyền để truy cập vào hệ thống!!!!!")
      );
    } else {
      localStorageService.setToken(data.accessToken, data.refreshToken);
      localStorageService.setUser({
        email: data.email,
        displayName: data.displayName,
        imageUser: data.imageUser,
        remember: isRemember,
      });
      yield call(() => handlerSuccessSaga("Login successfully!"));
      yield put(actionSetUser(data));
    }
  } catch (error) {
    console.log("user saga error: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* handleLogout() {
  try {
    yield put(actionLoading("Loading logout user ...!"));
    yield call(userApi.logout);
    yield call(() => handlerSuccessSaga("Logout successfully!"));
    yield put(actionSetUser(null));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* handlerRefreshToken() {
  try {
    yield put(actionLoading("Loading refresh token ...!"));
    const userState = rootStore.getState();
    const { user, isRefreshingToken } = userState.user;
    if (isRefreshingToken) {
      const refreshToken = localStorageService.getRefreshToken();
      const latestAction = localStorageService.getLatestAction();
      const response = yield call(() =>
        userApi.refreshToken({ userId: user._id, refreshToken })
      );
      const { data } = response;
      localStorageService.setAccessToken(data.accessToken);
      axiosClient.defaults.headers.common[
        "Authorization"
      ] = `Bearer ${localStorageService.getAccessToken()}`;
      yield put(actionRefreshTokenEnded());
      yield put({ type: latestAction });
      yield call(() => handlerSuccessSaga("Refresh token successfully!"));
    }
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
// ***** Watcher Functions *****
export function* sagaLogin() {
  yield takeLatest(actionLogin.type, handlerLogin);
}
export function* sagaLogout() {
  yield takeLatest(actionLogout.type, handleLogout);
}
export function* sagaRefreshToken() {
  yield takeLatest(actionRefreshToken.type, handlerRefreshToken);
}
// ***** Watcher Functions *****
