import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import userReducer from "redux/slices/userSlice";
import blogReducer from "redux/slices/blogSlice";
import postReducer from "redux/slices/postSlice";
import commonReducer from "redux/slices/commonSlice";
import createSagaMiddleware from "redux-saga";
import rootSaga from "redux/saga/rootSaga";
const sagaMiddleware = createSagaMiddleware();
const rootStore = configureStore({
  reducer: {
    user: userReducer,
    blog: blogReducer,
    post: postReducer,
    common: commonReducer,
  },
  middleware: [
    ...getDefaultMiddleware({ serializableCheck: false, thunk: false }),
    sagaMiddleware,
  ],
});
sagaMiddleware.run(rootSaga);

export default rootStore;
