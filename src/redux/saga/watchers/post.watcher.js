import { takeLatest } from "@redux-saga/core/effects";
import {
  actionDeletePost,
  actionGetAllPost,
  actionGetPost,
  actionUpdatePost,
} from "redux/slices/post.slice";
import {
  handlerDeletePost,
  handlerGetAllPost,
  handlerGetPost,
  handlerUpdatePost,
} from "../handlers/post.handler";

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
