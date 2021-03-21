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
  sagaDeleteUser,
  sagaGetUser,
  sagaGetUserList,
  sagaLogin,
  sagaLogout,
  sagaRefreshToken,
  sagaSendNotify,
  sagaSendMultiNotify,
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
  sagaDeleteUser(),
  sagaGetUser(),
  sagaGetUserList(),
  sagaLogin(),
  sagaLogout(),
  sagaSendNotify(),
  sagaSendMultiNotify(),
  sagaRefreshToken(),
];
const sagaList = sagaUser.concat(sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
