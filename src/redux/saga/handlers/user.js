import userApi from "api/userApi";
import { call, put, takeLatest } from "redux-saga/effects";
import { actionLogin, actionSetUser } from "redux/slices/userSlice";

export function* handlerGetUser(action) {
  try {
    const response = yield call(() => userApi.getById(action.payload));
    const { data } = response;
    yield put(actionSetUser(data));
  } catch (error) {
    console.log(error);
  }
}

// ***** Watcher Functions *****
export function* sagaLogin() {
  yield takeLatest(actionLogin.type, handlerGetUser);
}
// ***** Watcher Functions *****
