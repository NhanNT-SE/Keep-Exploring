import { call, put, takeLatest } from "@redux-saga/core/effects";
import commentApi from "api/comment.api";
import {
  actionDeleteComment,
  actionGetCommentList,
  actionGetLikeList,
  actionSetCommentList,
  actionSetLikeList,
} from "redux/slices/comment.slice";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/common.slice";
import localStorageService from "utils/localStorageService";
import { handlerFailSaga, handlerSuccessSaga } from "redux/saga/handlers/common.handler";

export function* handlerDeleteComment(action) {
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
export function* handlerGetCommentList(action) {
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

export function* handlerGetLikeList(action) {
  try {
    localStorageService.setLatestAction(actionGetLikeList.type);
    const { body, type } = action.payload;
    yield put(actionLoading("Loading get like list ...!"));
    const response = yield call(() => commentApi.getLikeList(body, type));
    const { data } = response;
    yield put(actionSetLikeList(data));
    yield put(actionSuccess("Fetch like list successfully!"));
  } catch (error) {
    console.log("comment saga: ", error);
    yield put(actionFailed(error.message));
  }
}
