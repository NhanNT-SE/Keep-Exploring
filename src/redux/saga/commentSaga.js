import { call, put, takeLatest } from "@redux-saga/core/effects";
import commentApi from "api/commentApi";
import {
  actionDeleteComment,
  actionGetCommentList,
  actionSetCommentList,
} from "redux/slices/commentSlice";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";
import localStorageService from "utils/localStorageService";
import { handlerFailSaga, handlerSuccessSaga } from "./commonSaga";

function* handlerDeleteComment(action) {
  try {
    localStorageService.setLatestAction(actionDeleteComment.type);
    const commentId = action.payload;
    yield put(actionLoading("Loading deleting comment...!"));
    yield call(() => commentApi.deleteComment(commentId));
    yield call(() => handlerSuccessSaga("Delete comment successfully!"));
  } catch (error) {
    console.log("comment saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
function* handlerGetCommentList(action) {
  try {
    localStorageService.setLatestAction(actionGetCommentList.type);
    const { id, type } = action.payload;
    yield put(actionLoading("Loading get comment list ...!"));
    const response = yield call(() => commentApi.getCommentList(id, type));
    const { data } = response;
    yield put(actionSetCommentList(data));
    yield put(actionSuccess("Fetch comment list successfully!"));
  } catch (error) {
    console.log("comment saga: ", error);
    yield put(actionFailed(error.message));
  }
}
// ***** Watcher Functions *****

export function* sagaDeleteComment() {
  yield takeLatest(actionDeleteComment.type, handlerDeleteComment);
}
export function* sagaGetCommentList() {
  yield takeLatest(actionGetCommentList.type, handlerGetCommentList);
}
// ***** Watcher Functions *****
