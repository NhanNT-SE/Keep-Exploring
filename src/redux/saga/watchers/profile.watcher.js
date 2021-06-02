import {
  actionChangePassword,
  actionGetMyProfile,
  actionUpdateProfile,
} from "redux/slices/profile.slice";
import {
  handlerChangePassword,
  handlerGetMyProfile,
  handlerUpdateProfile,
} from "redux/saga/handlers/profile.handler";
import { takeLatest } from "@redux-saga/core/effects";

export function* sagaChangePassword() {
  yield takeLatest(actionChangePassword.type, handlerChangePassword);
}

export function* sagaGetMyProfile() {
  yield takeLatest(actionGetMyProfile.type, handlerGetMyProfile);
}

export function* sagaUpdateProfile() {
  yield takeLatest(actionUpdateProfile.type, handlerUpdateProfile);
}
