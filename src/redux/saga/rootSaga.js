import { all } from "redux-saga/effects";
import {
  sagaDeleteBlog,
  sagaDeleteComment,
  sagaGetAllBlog,
  sagaGetBlog,
  sagaUpdateBlog,
} from "./blogSaga";
import {
  sagaDeletePost,
  sagaGetAllPost,
  sagaGetPost,
  sagaGetStatistics,
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
  sagaUpdateProfile,
  sagaChangePassword,
} from "./userSaga";
const sagaBlog = [
  sagaDeleteBlog(),
  sagaGetAllBlog(),
  sagaGetBlog(),
  sagaUpdateBlog(),
  sagaDeleteComment(),
];
const sagaPost = [
  sagaDeletePost(),
  sagaGetAllPost(),
  sagaGetPost(),
  sagaUpdatePost(),
  sagaGetStatistics(),
];
const sagaUser = [
  sagaChangePassword(),
  sagaDeleteUser(),
  sagaGetUser(),
  sagaGetUserList(),
  sagaLogin(),
  sagaLogout(),
  sagaSendNotify(),
  sagaSendMultiNotify(),
  sagaRefreshToken(),
  sagaUpdateProfile(),
];
const sagaList = sagaUser.concat(sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
