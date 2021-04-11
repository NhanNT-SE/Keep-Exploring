import { all } from "redux-saga/effects";
import {
  sagaDeleteBlog,
  sagaGetAllBlog,
  sagaGetBlog,
  sagaUpdateBlog,
} from "./blogSaga";
import { sagaDeleteComment, sagaGetCommentList } from "./commentSaga";
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
  sagaUpdateProfile,
  sagaChangePassword,
} from "./userSaga";

const sagaComment = [sagaDeleteComment(), sagaGetCommentList()];
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
  sagaRefreshToken(),
  sagaUpdateProfile(),
];
const sagaList = sagaUser.concat(sagaComment, sagaBlog, sagaPost);

export default function* rootSaga() {
  yield all(sagaList);
}
