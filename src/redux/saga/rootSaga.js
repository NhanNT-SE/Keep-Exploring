import { all } from "redux-saga/effects";
import {
  sagaLogin,
  sagaLogout,
  sagaRefreshToken,
} from "./watchers/auth.watcher";
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
  sagaDisableMFA,
  sagaEnableMFA,
  sagaVerifyOTP,
} from "./watchers/mfa.watcher";
import {
  sagaDeletePost,
  sagaGetAllPost,
  sagaGetPost,
  sagaUpdatePost,
} from "./watchers/post.watcher";
import {
  sagaChangePassword,
  sagaGetMyProfile,
  sagaUpdateProfile,
} from "./watchers/profile.watcher";
import {
  sagaGetStatisticsNumber,
  sagaGetStatisticsTimeLine,
} from "./watchers/statistics.watcher";
import {
  sagaDeleteUser,
  sagaGetSelectedUser,
  sagaGetUserList,
  sagaSendNotify,
} from "./watchers/user.watcher";

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
const sagaMFA = [sagaEnableMFA(), sagaDisableMFA(), sagaVerifyOTP()];

const sagaAuth = [sagaLogin(), sagaLogout(), sagaRefreshToken()];
const sagaList = sagaUser.concat(
  sagaAuth,
  sagaProfile,
  sagaComment,
  sagaBlog,
  sagaMFA,
  sagaPost,
  sagaStatistics
);

export default function* rootSaga() {
  yield all(sagaList);
}
