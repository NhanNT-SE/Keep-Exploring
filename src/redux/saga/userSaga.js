import axiosClient from "api/axiosClient";
import localStorageService from "utils/localStorageService";
import userApi from "api/userApi";
import GLOBAL_VARIABLE from "utils/global_variable";
import { call, put, takeLatest } from "redux-saga/effects";
import { handlerFailSaga, handlerSuccessSaga } from "redux/saga/commonSaga";
import {
  actionFailed,
  actionHideDialog,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";
import {
  actionGetListUser,
  actionGetUser,
  actionLogin,
  actionLogout,
  actionRefreshToken,
  actionRefreshTokenEnded,
  actionSetSelectedUser,
  actionSetUser,
  actionSetUserList,
  actionSendNotify,
  actionDeleteUser,
  actionSendMultiNotify,
  actionUpdateProfile,
  actionChangePassword,
} from "redux/slices/userSlice";
import rootStore from "rootStore";
import notifyApi from "api/notifyApi";

function* handlerChangePassword(action) {
  try {
    const { data, history } = action.payload;
    yield put(actionLoading("Loading change your password...!"));
    // yield call(() => userApi.changePassword(data));
    console.log("Change Password:", action.payload);
    yield call(() => handlerSuccessSaga("Change password successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_DELETE_USER));
    localStorageService.clearUser();
    history.push("/login");
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}

function* handlerDeleteUser(action) {
  try {
    const { userId, history } = action.payload;
    yield put(actionLoading("Loading deleting user...!"));
    yield call(() => userApi.deleteUser(userId));
    yield call(() => handlerSuccessSaga("Delete user successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_DELETE_USER));
    history.push("/user");
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* handlerGetUser(action) {
  try {
    yield put(actionLoading("Loading get user profile...!"));
    const response = yield call(() => userApi.getUser(action.payload));
    const { data } = response;
    yield put(actionSetSelectedUser(data));
    yield put(actionSuccess("Get user profile successfully!"));
  } catch (error) {
    console.log("user saga: ", error);
    yield put(actionFailed(error.message));
  }
}

function* handlerGetUserList() {
  try {
    yield put(actionLoading("Loading get all users...!"));
    const response = yield call(userApi.getAllUser);
    const { data } = response;
    yield put(actionSetUserList(data));
    yield put(actionSuccess("Fetch list user successfully!"));
  } catch (error) {
    console.log("user saga: ", error);
    yield put(actionFailed(error.message));
  }
}

function* handlerLogin(action) {
  try {
    const rootState = rootStore.getState();
    const { isRemember } = rootState.common;
    yield put(actionLoading("Loading login user ...!"));
    const response = yield call(() => userApi.login(action.payload));
    const { data } = response;
    data.imgUser = `${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${data.imgUser}`;
    if (data.role !== "admin") {
      yield call(() =>
        handlerFailSaga("Bạn không đủ quyền để truy cập vào hệ thống!!!!!")
      );
    } else {
      localStorageService.setToken(data.accessToken, data.refreshToken);
      localStorageService.setUser({
        _id: data._id,
        email: data.email,
        displayName: data.displayName,
        imgUser: data.imgUser,
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

function* handlerSendNotify(action) {
  try {
    yield put(actionLoading("Loading send notify for user...!"));
    yield call(() => notifyApi.sendNotify(action.payload));
    yield call(() => handlerSuccessSaga("Send notify successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* handlerSendMultiNotify(action) {
  try {
    yield put(actionLoading("Loading send notify for user list ...!"));
    // yield call(() => notifyApi.sendMultiNotify(action.payload));
    yield call(() => handlerSuccessSaga("Send notify successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* handlerUpdateProfile(action) {
  try {
    yield put(actionLoading("Loading update profile...!"));
    // yield call(() => userApi.updateProfile(action.payload));
    console.log("update profile:", action.payload);
    yield call(() => handlerSuccessSaga("Update Profile successfully!"));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}

// ***** Watcher Functions *****
export function* sagaChangePassword() {
  yield takeLatest(actionChangePassword.type, handlerChangePassword);
}
export function* sagaDeleteUser() {
  yield takeLatest(actionDeleteUser.type, handlerDeleteUser);
}
export function* sagaLogin() {
  yield takeLatest(actionLogin.type, handlerLogin);
}
export function* sagaLogout() {
  yield takeLatest(actionLogout.type, handleLogout);
}
export function* sagaRefreshToken() {
  yield takeLatest(actionRefreshToken.type, handlerRefreshToken);
}
export function* sagaGetUser() {
  yield takeLatest(actionGetUser.type, handlerGetUser);
}
export function* sagaGetUserList() {
  yield takeLatest(actionGetListUser.type, handlerGetUserList);
}
export function* sagaSendMultiNotify() {
  yield takeLatest(actionSendMultiNotify.type, handlerSendMultiNotify);
}
export function* sagaSendNotify() {
  yield takeLatest(actionSendNotify.type, handlerSendNotify);
}
export function* sagaUpdateProfile() {
  yield takeLatest(actionUpdateProfile.type, handlerUpdateProfile);
}
// ***** Watcher Functions *****
