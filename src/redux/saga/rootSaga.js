import { all } from "redux-saga/effects";
import { sagaGetAllPost } from "./postSaga";
import { sagaLogin, sagaLogout, sagaRefreshToken } from "./userSaga";
const sagaBlog = [];
const sagaPost = [sagaGetAllPost()];
const sagaUser = [sagaLogin(), sagaLogout(), sagaRefreshToken()];
const sagaList = sagaUser.concat(sagaBlog, sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
