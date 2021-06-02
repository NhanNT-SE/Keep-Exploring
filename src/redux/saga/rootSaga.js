import { all } from "redux-saga/effects";
import {
  sagaDeleteBlog,
  sagaGetAllBlog,
  sagaGetBlog,
  sagaUpdateBlog,
} from "./watchers/blog.watcher";
import {
  sagaDeleteComment,
  sagaGetCommentList,
  sagaGetLikeList,
} from "./watchers/comment.watcher";
import {
  sagaDeletePost,
  sagaGetAllPost,
  sagaGetPost,
  sagaGetStatistics,
  sagaGetTimeLineStatistics,
  sagaUpdatePost,
} from "./watchers/post.watcher";

import {
  sagaLogin,
  sagaLogout,
  sagaRefreshToken,
} from "./watchers/auth.watcher";
import {
  sagaChangePassword,
  sagaGetMyProfile,
  sagaUpdateProfile,
} from "./watchers/profile.watcher";
import {
  sagaDeleteUser,
  sagaGetSelectedUser,
  sagaGetUserList,
  sagaSendNotify,
} from "./watchers/user.watcher";
import {
  sagaGetStatisticsNumber,
  sagaGetStatisticsTimeLine,
} from "./watchers/statistics.watcher";

const sagaComment = [
  sagaDeleteComment(),
  sagaGetCommentList(),
  sagaGetLikeList(),
];
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
const sagaStatistics = [sagaGetStatisticsNumber(), sagaGetStatisticsTimeLine()];
const sagaUser = [
  sagaDeleteUser(),
  sagaGetSelectedUser(),
  sagaGetUserList(),
  sagaSendNotify(),
];
const sagaProfile = [
  sagaChangePassword(),
  sagaGetMyProfile(),
  sagaUpdateProfile(),
];
const sagaAuth = [sagaLogin(), sagaLogout(), sagaRefreshToken()];
const sagaList = sagaUser.concat(
  sagaAuth,
  sagaProfile,
  sagaComment,
  sagaBlog,
  sagaPost,
  sagaStatistics
);

export default function* rootSaga() {
  yield all(sagaList);
}
