import { all } from "redux-saga/effects";
import {
  sagaDeleteBlog,
  sagaGetAllBlog,
  sagaGetBlog,
  sagaUpdateBlog,
} from "./blogSaga";
import {
  sagaDeletePost,
  sagaGetAllPost,
  sagaGetPost,
  sagaUpdatePost,
} from "./postSaga";
import {
  sagaGetUser,
  sagaGetUserList,
  sagaLogin,
  sagaLogout,
  sagaRefreshToken,
  sagaSendNotify,
} from "./userSaga";
const sagaBlog = [
  sagaDeleteBlog(),
  sagaGetAllBlog(),
  sagaGetBlog(),
  sagaUpdateBlog(),
];
const sagaPost = [
  sagaDeletePost(),
  sagaGetAllPost(),
  sagaGetPost(),
  sagaUpdatePost(),
];
const sagaUser = [
  sagaGetUser(),
  sagaGetUserList(),
  sagaLogin(),
  sagaLogout(),
  sagaSendNotify(),
  sagaRefreshToken(),
];
const sagaList = sagaUser.concat(sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
