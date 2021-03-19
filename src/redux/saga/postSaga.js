import postApi from "api/postApi";
import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionGetAllPost,
  actionSetPostList,
  actionSetSelectedPost,
  actionGetPost,
  actionUpdatePost,
} from "redux/slices/postSlice";
import {
  actionFailed,
  actionHideDialog,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";
import { handlerFailSaga, handlerSuccessSaga } from "./commonSaga";
import GLOBAL_VARIABLE from "utils/global_variable";

function* handlerGetAllPost() {
  try {
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
  try {
    yield put(actionLoading("Loading get post...!"));
    const response = yield call(() => postApi.getPost(action.payload));
    const { data } = response;
    yield put(actionSetSelectedPost(data));
    yield put(actionSuccess("Fetch post successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
  }
}

function* handlerUpdatePost(action) {
  try {
    yield put(actionLoading("Loading updating status post...!"));
    yield call(() => postApi.updatePost(action.payload));
    yield call(() => handlerSuccessSaga("Update post successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
  } catch (error) {
    console.log("post slice: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
// ***** Watcher Functions *****
export function* sagaGetAllPost() {
  yield takeLatest(actionGetAllPost.type, handlerGetAllPost);
}
export function* sagaGetPost() {
  yield takeLatest(actionGetPost.type, handlerGetPost);
}
export function* sagaUpdatePost() {
  yield takeLatest(actionUpdatePost.type, handlerUpdatePost);
}
// ***** Watcher Functions *****