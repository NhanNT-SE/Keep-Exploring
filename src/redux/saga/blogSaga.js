import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionFailed,
  actionHideDialog,
  actionLoading,
  actionSuccess,
} from "redux/slices/commonSlice";
import { handlerFailSaga, handlerSuccessSaga } from "./commonSaga";
import GLOBAL_VARIABLE from "utils/global_variable";
import blogApi from "api/blogApi";
import {
  actionDeleteBlog,
  actionDeleteComment,
  actionGetAllBlog,
  actionGetBlog,
  actionSetBlogList,
  actionSetSelectedBlog,
  actionUpdateBlog,
} from "redux/slices/blogSlice";
import notifyApi from "api/notifyApi";
import commentApi from "api/commentApi";
import localStorageService from "utils/localStorageService";
function* handlerDeleteBlog(action) {
  try {
    localStorageService.setLatestAction(actionDeleteBlog.type);

    const { blogId, history } = action.payload;
    yield put(actionLoading("Loading deleting blog...!"));
    yield call(() => blogApi.deleteBlog(blogId));
    yield call(() => notifyApi.sendNotify(action.payload));
    yield call(() => handlerSuccessSaga("Delete blog successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
    history.push("/blog");
  } catch (error) {
    console.log("post slice: ", error);
    yield call(() => handlerFailSaga(error));
  }
}


function* handlerGetAllBlog() {
  try {
    localStorageService.setLatestAction(actionGetAllBlog.type);
    yield put(actionLoading("Loading get all blog list ...!"));
    const response = yield call(blogApi.getAll);
    const { data } = response;
    console.log("blog list:", data);
    yield put(actionSetBlogList(data));
    yield put(actionSuccess("Fetch blog list successfully!"));
  } catch (error) {
    console.log("blog slice: ", error);
    yield put(actionFailed(error.message));
  }
}
function* handlerGetBlog(action) {
  const { blogId, history } = action.payload;
  localStorageService.setLatestAction(actionGetBlog.type);
  try {
    yield put(actionLoading("Loading get blog ...!"));
    const response = yield call(() => blogApi.getBlog(blogId));
    const { data } = response;
    yield put(actionSetSelectedBlog(data));
    yield put(actionSuccess("Fetch blog list successfully!"));
  } catch (error) {
    console.log("blog saga: ", error);
    yield put(actionFailed(error.message));
    history.push("/blog");
  }
}

function* handlerUpdateBlog(action) {
  try {
    localStorageService.setLatestAction(actionUpdateBlog.type);

    yield put(actionLoading("Loading updating status blog...!"));
    yield call(() => blogApi.updateBlog(action.payload));
    yield call(() => notifyApi.sendNotify(action.payload));

    yield call(() => handlerSuccessSaga("Update blog successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
  } catch (error) {
    console.log("post saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
// ***** Watcher Functions *****

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
// ***** Watcher Functions *****
