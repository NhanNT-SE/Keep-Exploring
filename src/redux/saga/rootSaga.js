import { all } from "redux-saga/effects";
import { sagaLogin, sagaLogout } from "./handlers/user";
const sagaBlog = [];
const sagaPost = [];
const sagaUser = [sagaLogin(), sagaLogout()];
const sagaList = sagaUser.concat(sagaBlog, sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
