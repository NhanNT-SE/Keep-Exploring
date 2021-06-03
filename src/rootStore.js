import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import commentReducer from "redux/slices/comment.slice";
import commonReducer from "redux/slices/common.slice";
import userReducer from "redux/slices/user.slice";
import authReducer from "redux/slices/auth.slice";
import dialogReducer from "redux/slices/dialog.slice";
import profileReducer from "redux/slices/profile.slice";
import blogReducer from "redux/slices/blog.slice";
import postReducer from "redux/slices/post.slice";
import statisticsReducer from "redux/slices/statistics.slice";
import createSagaMiddleware from "redux-saga";
import rootSaga from "redux/saga/rootSaga";
const sagaMiddleware = createSagaMiddleware();
const rootStore = configureStore({
  reducer: {
    auth: authReducer,
    blog: blogReducer,
    comment: commentReducer,
    common: commonReducer,
    dialog: dialogReducer,
    post: postReducer,
    profile: profileReducer,
    statistics: statisticsReducer,
    user: userReducer,
  },
  middleware: [
    ...getDefaultMiddleware({ serializableCheck: false, thunk: false }),
    sagaMiddleware,
  ],
});
sagaMiddleware.run(rootSaga);

export default rootStore;
