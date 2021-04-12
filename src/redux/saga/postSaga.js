import postApi from "api/postApi";
import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionGetAllPost,
  actionSetPostList,
  actionSetSelectedPost,
  actionGetPost,
  actionUpdatePost,
  actionDeletePost,
  // actionSetCommentList,
  actionSetStatistics,
  actionGetStatistics,
} from "redux/slices/postSlice";
import {
  actionFailed,
  actionHideDialog,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";
import { handlerFailSaga, handlerSuccessSaga } from "./commonSaga";
import GLOBAL_VARIABLE from "utils/global_variable";
import notifyApi from "api/notifyApi";
import statisticsApi from "api/statisticsApi";
import localStorageService from "utils/localStorageService";
function* handlerDeletePost(action) {
  try {
    const { postId, history } = action.payload;
    yield put(actionLoading("Loading deleting post...!"));
    yield call(() => postApi.deletePost(postId));
    yield call(() => notifyApi.sendNotify(action.payload));
    yield call(() => handlerSuccessSaga("Delete post successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
    history.push("/post");
  } catch (error) {
    console.log("post slice: ", error);
    yield call(() => handlerFailSaga(error));
  }
}

function* handlerGetAllPost() {
  try {
    localStorageService.setLatestAction(actionGetAllPost.type);
    yield put(actionLoading("Loading get all post list ...!"));
    const response = yield call(postApi.getAll);
    const { data } = response;
    yield put(actionSetPostList(data));
    yield put(actionSuccess("Fetch post list successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
  }
}
function* handlerGetPost(action) {
  const { postId, history } = action.payload;
  try {
    localStorageService.setLatestAction(actionGetPost.type);
    yield put(actionLoading("Loading get post...!"));
    const response = yield call(() => postApi.getPost(postId));
    const { data } = response;
    yield put(actionSetSelectedPost(data));
    yield put(actionSuccess("Fetch post successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
    history.push("/post");
  }
}
function* handlerGetStatistics() {
  try {
    localStorageService.setLatestAction(actionGetStatistics.type);
    const response = yield call(statisticsApi.getData);
    const { data } = response;
    yield put(actionSetStatistics(data));
    yield put(actionSuccess("Fetch statistics data successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
  }
}

function* handlerUpdatePost(action) {
  try {
    localStorageService.setLatestAction(actionUpdatePost.type);
    yield put(actionLoading("Loading updating status post...!"));
    yield call(() => postApi.updatePost(action.payload));
    yield call(() => notifyApi.sendNotify(action.payload));
    yield call(() => handlerSuccessSaga("Update post successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
  } catch (error) {
    console.log("post slice: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
// ***** Watcher Functions *****
export function* sagaDeletePost() {
  yield takeLatest(actionDeletePost.type, handlerDeletePost);
}

export function* sagaGetAllPost() {
  yield takeLatest(actionGetAllPost.type, handlerGetAllPost);
}
export function* sagaGetPost() {
  yield takeLatest(actionGetPost.type, handlerGetPost);
}
export function* sagaUpdatePost() {
  yield takeLatest(actionUpdatePost.type, handlerUpdatePost);
}
export function* sagaGetStatistics() {
  yield takeLatest(actionGetStatistics.type, handlerGetStatistics);
}
// ***** Watcher Functions *****
