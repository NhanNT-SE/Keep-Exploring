import { takeLatest } from "@redux-saga/core/effects";
import {
  actionDeleteComment,
  actionGetCommentList,
  actionGetLikeList,
} from "redux/slices/comment.slice";
import {
  handlerDeleteComment,
  handlerGetCommentList,
  handlerGetLikeList,
} from "../handlers/comment.handler";

export function* sagaDeleteComment() {
  yield takeLatest(actionDeleteComment.type, handlerDeleteComment);
}
export function* sagaGetCommentList() {
  yield takeLatest(actionGetCommentList.type, handlerGetCommentList);
}

export function* sagaGetLikeList() {
  yield takeLatest(actionGetLikeList.type, handlerGetLikeList);
}
