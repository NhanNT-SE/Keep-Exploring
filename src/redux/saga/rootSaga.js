import { all } from "redux-saga/effects";
import { sagaGetAllBlog, sagaGetBlog } from "./blogSaga";
import {
  sagaDeletePost,
  sagaGetAllPost,
  sagaGetPost,
  sagaUpdatePost,
} from "./postSaga";
import { sagaLogin, sagaLogout, sagaRefreshToken } from "./userSaga";
const sagaBlog = [sagaGetAllBlog(), sagaGetBlog()];
const sagaPost = [
  sagaDeletePost(),
  sagaGetAllPost(),
  sagaGetPost(),
  sagaUpdatePost(),
];
const sagaUser = [sagaLogin(), sagaLogout(), sagaRefreshToken()];
const sagaList = sagaUser.concat(sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
