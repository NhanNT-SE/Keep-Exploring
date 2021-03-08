import axiosClient from "api/axiosClient";
import localStorageService from "api/localStorageService";
import userApi from "api/userApi";
import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";
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
    yield put(actionLoading("Loading login user ...!"));
    const response = yield call(() => userApi.login(action.payload));
    const { data } = response;
    localStorageService.setToken(data.accessToken, data.refreshToken);
    axiosClient.defaults.headers.common[
      "Authorization"
    ] = localStorageService.getAccessToken();
    yield put(actionSetUser(data));
    yield put(actionSuccess("Login successfully!"));
  } catch (error) {
    console.log("user slice: ", error);
    yield put(actionFailed(error.message));
  }
}
function* handleLogout() {
  try {
    yield put(actionLoading("Loading logout user ...!"));
    const userState = rootStore.getState();
    const { user } = userState.user;
    if (user && user._id) {
      yield call(() => userApi.logout({ userId: user._id }));
      localStorageService.clearStorage();
      yield put(actionSetUser(null));
      yield put(actionSuccess("Logout successfully"));
    }
  } catch (error) {
    console.log("user slice: ", error);
    yield put(actionFailed(error.message));
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
      ] = localStorageService.getAccessToken();
      yield put(actionRefreshTokenEnded());
      yield put({ type: latestAction });
      yield put(actionSuccess("Refresh token successfully"));
    }
  } catch (error) {
    console.log("user slice: ", error);
    yield put(actionFailed(error.message));
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
