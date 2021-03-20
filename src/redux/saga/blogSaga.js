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
  actionGetAllBlog,
  actionGetBlog,
  actionSetBlogList,
  actionSetSelectedBlog,
} from "redux/slices/blogSlice";

function* handlerGetAllBlog() {
  try {
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
  try {
    yield put(actionLoading("Loading get blog ...!"));
    const response = yield call(() => blogApi.getBlog(blogId));
    const { data } = response;
    console.log("blog:", data);

    yield put(actionSetSelectedBlog(data));
    yield put(actionSuccess("Fetch blog list successfully!"));
  } catch (error) {
    console.log("blog slice: ", error);
    yield put(actionFailed(error.message));
    history.put("/blog");
  }
}

// ***** Watcher Functions *****

export function* sagaGetAllBlog() {
  yield takeLatest(actionGetAllBlog.type, handlerGetAllBlog);
}
export function* sagaGetBlog() {
  yield takeLatest(actionGetBlog.type, handlerGetBlog);
}
// ***** Watcher Functions *****
