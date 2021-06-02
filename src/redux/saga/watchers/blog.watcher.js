import { takeLatest } from "@redux-saga/core/effects";
import {
  actionDeleteBlog,
  actionGetAllBlog,
  actionGetBlog,
  actionUpdateBlog,
} from "redux/slices/blog.slice";
import {
  handlerDeleteBlog,
  handlerGetAllBlog,
  handlerGetBlog,
  handlerUpdateBlog,
} from "../handlers/blog.handler";

export function* sagaDeleteBlog() {
  yield takeLatest(actionDeleteBlog.type, handlerDeleteBlog);
}

export function* sagaGetAllBlog() {
  yield takeLatest(actionGetAllBlog.type, handlerGetAllBlog);
}
export function* sagaGetBlog() {
  yield takeLatest(actionGetBlog.type, handlerGetBlog);
}
export function* sagaUpdateBlog() {
  yield takeLatest(actionUpdateBlog.type, handlerUpdateBlog);
}
