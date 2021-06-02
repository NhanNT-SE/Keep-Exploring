import blogApi from "api/blog.api";
import notifyApi from "api/notify.api";
import { call, put, takeLatest } from "redux-saga/effects";
import {
  actionDeleteBlog,
  actionGetAllBlog,
  actionGetBlog,
  actionSetBlogList,
  actionSetSelectedBlog,
  actionUpdateBlog,
} from "redux/slices/blog.slice";
import {
  actionFailed,
  actionHideDialog,
  actionLoading,
  actionSuccess,
} from "redux/slices/common.slice";
import GLOBAL_VARIABLE from "utils/global_variable";
import localStorageService from "utils/localStorageService";
import { handlerFailSaga, handlerSuccessSaga } from "redux/saga/handlers/common.handler";
export function* handlerDeleteBlog(action) {
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

export function* handlerGetAllBlog() {
  try {
    localStorageService.setLatestAction(actionGetAllBlog.type);
    yield put(actionLoading("Loading get all blog list ...!"));
    const response = yield call(blogApi.getAll);
    const { data } = response;
    yield put(actionSetBlogList(data));
    yield put(actionSuccess("Fetch blog list successfully!"));
  } catch (error) {
    console.log("blog slice: ", error);
    yield put(actionFailed(error.message));
  }
}
export function* handlerGetBlog(action) {
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

export function* handlerUpdateBlog(action) {
  try {
    localStorageService.setLatestAction(actionUpdateBlog.type);
    const { contentAdmin } = action.payload;

    yield put(actionLoading("Loading updating status blog...!"));
    yield call(() => blogApi.updateBlog(action.payload));
    if (contentAdmin) {
      yield call(() => notifyApi.sendNotify(action.payload));
    }
    yield call(() => handlerSuccessSaga("Update blog successfully!"));
    yield put(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
  } catch (error) {
    console.log("post saga: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
