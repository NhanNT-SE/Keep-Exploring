import { all } from "redux-saga/effects";
import { sagaLogin } from "./handlers/user";
const sagaAuth = [];
const sagaBlog = [];
const sagaPost = [];
const sagaUser = [sagaLogin()];
const sagaList = sagaAuth.concat(sagaBlog, sagaBlog, sagaPost, sagaUser);

export default function* rootSaga() {
  yield all(sagaList);
}
