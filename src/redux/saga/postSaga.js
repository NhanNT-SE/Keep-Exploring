import postApi from "api/postApi";
import { call, put, takeLatest } from "redux-saga/effects";
import { actionGetAllPost, actionSetPostList } from "redux/slices/postSlice";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";

function* handlerGetAllPost() {
  try {
    yield put(actionLoading("Loading get all post list ...!"));
    const response = yield call(postApi.getAll);
    const { data } = response;
    yield put(actionSetPostList(data));
    console.log("data", data);
    yield put(actionSuccess("Fetch post list successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
  }
}
// ***** Watcher Functions *****
export function* sagaGetAllPost() {
  yield takeLatest(actionGetAllPost.type, handlerGetAllPost);
}
// ***** Watcher Functions *****
