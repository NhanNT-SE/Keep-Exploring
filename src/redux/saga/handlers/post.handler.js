import notifyApi from "api/notify.api";
import postApi from "api/post.api";
import { call, put } from "redux-saga/effects";
import {
  handlerFailSaga,
  handlerSuccessSaga,
} from "redux/saga/handlers/common.handler";
import {
  actionFailed,
  actionLoading,
  actionSuccess,
} from "redux/slices/common.slice";
import { actionHideDialog } from "redux/slices/dialog.slice";
import {
  actionDeletePost,
  actionGetAllPost,
  actionGetPost,
  actionSetPostList,
  actionSetSelectedPost,
  actionUpdatePost,
} from "redux/slices/post.slice";
import {DIALOG} from "utils/global_variable";
import localStorageService from "utils/localStorageService";
export function* handlerDeletePost(action) {
  try {
    localStorageService.setLatestAction(actionDeletePost.type);

    const { postId, history } = action.payload;
    yield put(actionLoading("Loading deleting post...!"));
    yield call(() => postApi.deletePost(postId));
    yield call(() => notifyApi.sendNotify(action.payload));
    yield call(() => handlerSuccessSaga("Delete post successfully!"));
    yield put(actionHideDialog(DIALOG.DIALOG_EDIT_POST));
    history.push("/post");
  } catch (error) {
    console.log("post slice: ", error);
    yield call(() => handlerFailSaga(error));
  }
}

export function* handlerGetAllPost() {
  try {
    localStorageService.setLatestAction(actionGetAllPost.type);
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
export function* handlerGetPost(action) {
  const { postId, history } = action.payload;
  try {
    localStorageService.setLatestAction(actionGetPost.type);
    yield put(actionLoading("Loading get post...!"));
    const response = yield call(() => postApi.getPost(postId));
    const { data } = response;
    yield put(actionSetSelectedPost(data));
    yield put(actionSuccess("Fetch post successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
    history.push("/post");
  }
}

export function* handlerUpdatePost(action) {
  try {
    localStorageService.setLatestAction(actionUpdatePost.type);
    const { contentAdmin } = action.payload;
    yield put(actionLoading("Loading updating status post...!"));
    yield call(() => postApi.updatePost(action.payload));
    if (contentAdmin) {
      yield call(() => notifyApi.sendNotify(action.payload));
    }
    yield call(() => handlerSuccessSaga("Update post successfully!"));
    yield put(actionHideDialog(DIALOG.DIALOG_EDIT_POST));
  } catch (error) {
    console.log("post slice: ", error);
    yield call(() => handlerFailSaga(error));
  }
}
