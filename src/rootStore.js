import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import userReducer from "redux/slices/userSlice";
import postReducer from "redux/slices/postSlice";
import commonReducer from "redux/slices/commonSlice";
import createSagaMiddleware from "redux-saga";
import rootSaga from "redux/saga/rootSaga";
const sagaMiddleware = createSagaMiddleware();
const rootStore = configureStore({
  reducer: {
    user: userReducer,
    post: postReducer,
    common: commonReducer,
  },
  middleware: [...getDefaultMiddleware({ thunk: false }), sagaMiddleware],
});
sagaMiddleware.run(rootSaga);

export default rootStore;
