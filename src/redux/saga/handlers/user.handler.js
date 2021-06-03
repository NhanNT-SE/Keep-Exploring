import notifyApi from "api/notify.api";
import userApi from "api/user.api";
import { call, put } from "redux-saga/effects";
import {
  handlerFailSaga,
  handlerSuccessSaga,
} from "redux/saga/handlers/common.handler";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/common.slice";
import { actionHideDialog } from "redux/slices/dialog.slice";
import {
  actionDeleteUser,
  actionGetSelectedUser,
  actionGetUserList,
  actionSendNotify,
  actionSetSelectedUser,
  actionSetUserList,
} from "redux/slices/user.slice";
import GLOBAL_VARIABLE from "utils/global_variable";
import localStorageService from "utils/localStorageService";

export function* handlerDeleteUser(action) {
  try {
    localStorageService.setLatestAction(actionDeleteUser.type);
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

export function* handlerGetSelectedUser(action) {
  try {
    localStorageService.setLatestAction(actionGetSelectedUser.type);
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
export function* handlerGetUserList() {
  try {
    localStorageService.setLatestAction(actionGetUserList.type);
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
export function* handlerSendNotify(action) {
  try {
    localStorageService.setLatestAction(actionSendNotify.type);
    yield put(actionLoading("Loading send notify for user...!"));
    yield call(() => notifyApi.sendNotify(action.payload));
    yield call(() => handlerSuccessSaga("Send notify successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
