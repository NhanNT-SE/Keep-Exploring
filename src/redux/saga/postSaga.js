import postApi from "api/postApi";
import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionGetAllPost,
  actionSetPosts,
  actionGetPostSuccess,
  actionGetPostFail,
} from "redux/slices/postSlice";

function* handlerGetAllPost() {
  try {
    const response = yield call(postApi.getAll);
    const { data } = response;
    yield put(actionSetPosts(data));
    console.log("data", data);
    yield put(actionGetPostSuccess());
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionGetPostFail(error.message));
  }
}
// ***** Watcher Functions *****
export function* sagaGetAllPost() {
  yield takeLatest(actionGetAllPost.type, handlerGetAllPost);
}
// ***** Watcher Functions *****
