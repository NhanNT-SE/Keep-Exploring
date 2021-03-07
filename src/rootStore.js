import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import userReducer from "redux/slices/userSlice";
import postReducer from "redux/slices/postSlice";
import createSagaMiddleware from "redux-saga";
import rootSaga from "redux/saga/rootSaga";
const sagaMiddleware = createSagaMiddleware();
const rootStore = configureStore({
  reducer: {
    user: userReducer,
    post: postReducer,
  },
  middleware: [...getDefaultMiddleware({ thunk: false }), sagaMiddleware],
});
sagaMiddleware.run(rootSaga);

export default rootStore;
