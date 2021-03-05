import userApi from "api/userApi";
import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionLogin,
  actionSetUser,
  actionSetErr,
  actionLogout,
} from "redux/slices/userSlice";

function* handlerLogin(action) {
  try {
    const response = yield call(() => userApi.getById(action.payload));
    const { data } = response;
    yield put(actionSetUser(data));
  } catch (error) {
    console.log("user slice: ", error);
    yield put(actionSetErr(error.message));
  }
}
function* handleLogout() {
  try {
    // Set expiry date token or remove session in server here
    // Set expiry date token or remove session in server here
    yield put(actionSetUser(null));
  } catch (error) {
    console.log("user slice: ", error);
    yield put(actionSetErr(error.message));
  }
}
// ***** Watcher Functions *****
export function* sagaLogin() {
  yield takeLatest(actionLogin.type, handlerLogin);
}
export function* sagaLogout() {
  yield takeLatest(actionLogout.type, handleLogout);
}
// ***** Watcher Functions *****
