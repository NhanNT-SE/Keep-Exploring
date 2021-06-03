import profileApi from "api/profile.api";
import { call, put } from "redux-saga/effects";
import {
  handlerFailSaga,
  handlerSuccessSaga,
} from "redux/saga/handlers/common.handler";
import { actionSetUser } from "redux/slices/auth.slice";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/common.slice";
import { actionHideDialog } from "redux/slices/dialog.slice";
import {
  actionChangePassword,
  actionGetMyProfile,
  actionUpdateProfile,
} from "redux/slices/profile.slice";
import { actionSetSelectedUser } from "redux/slices/user.slice";
import rootStore from "rootStore";
import GLOBAL_VARIABLE from "utils/global_variable";
import localStorageService from "utils/localStorageService";

export function* handlerChangePassword(action) {
  try {
    localStorageService.setLatestAction(actionChangePassword.type);
    const { data, history } = action.payload;
    yield put(actionLoading("Loading change your password...!"));
    yield call(() => profileApi.changePassword(data));
    console.log("Change Password:", data);
    yield call(() => handlerSuccessSaga("Change password successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_DELETE_USER));
    yield put(actionSetUser(null));
    localStorageService.clearUser();
    history.push("/login");
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}

export function* handlerGetMyProfile() {
  try {
    localStorageService.setLatestAction(actionGetMyProfile.type);
    yield put(actionLoading("Loading get my profile...!"));
    const response = yield call(profileApi.getProfile);
    const { data } = response;
    yield put(actionSetSelectedUser(data));
    yield put(actionSuccess("Get my profile successfully!"));
  } catch (error) {
    console.log("user saga: ", error);
    yield put(actionFailed(error.message));
  }
}

export function* handlerUpdateProfile(action) {
  try {
    localStorageService.setLatestAction(actionUpdateProfile.type);
    const rootState = rootStore.getState();
    const { isRemember } = rootState.common;
    yield put(actionLoading("Loading update profile...!"));
    const response = yield call(() => profileApi.updateProfile(action.payload));
    const { data } = response;
    data.imgUser = `${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${data.imgUser}`;
    localStorageService.setUser({
      _id: data._id,
      email: data.email,
      displayName: data.displayName,
      imgUser: data.imgUser,
      remember: isRemember,
    });
    yield call(() => handlerSuccessSaga("Update Profile successfully!"));
    yield put(actionSetUser(data));
  } catch (error) {
    console.log("user saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
