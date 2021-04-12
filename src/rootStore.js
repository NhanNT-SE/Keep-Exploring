import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import commentReducer from "redux/slices/commentSlice";
import commonReducer from "redux/slices/commonSlice";
import userReducer from "redux/slices/userSlice";
import blogReducer from "redux/slices/blogSlice";
import postReducer from "redux/slices/postSlice";
import createSagaMiddleware from "redux-saga";
import rootSaga from "redux/saga/rootSaga";
const sagaMiddleware = createSagaMiddleware();
const rootStore = configureStore({
  reducer: {
    comment: commentReducer,
    common: commonReducer,
    blog: blogReducer,
    post: postReducer,
    user: userReducer,
  },
  middleware: [
    ...getDefaultMiddleware({ serializableCheck: false, thunk: false }),
    sagaMiddleware,
  ],
});
sagaMiddleware.run(rootSaga);

export default rootStore;
