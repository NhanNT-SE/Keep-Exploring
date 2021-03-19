import { all } from "redux-saga/effects";
import { sagaGetAllPost, sagaGetPost, sagaUpdatePost } from "./postSaga";
import { sagaLogin, sagaLogout, sagaRefreshToken } from "./userSaga";
const sagaBlog = [];
const sagaPost = [sagaGetAllPost(), sagaGetPost(), sagaUpdatePost()];
const sagaUser = [sagaLogin(), sagaLogout(), sagaRefreshToken()];
const sagaList = sagaUser.concat(sagaBlog, sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
