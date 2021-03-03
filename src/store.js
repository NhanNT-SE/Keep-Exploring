import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import userReducer from "redux/slices/userSlice";
import createSagaMiddleware from "redux-saga";
import rootSaga from "redux/saga/rootSaga";
const sagaMiddleware = createSagaMiddleware();
const store = configureStore({
  reducer: {
    user: userReducer,
  },
  middleware: [...getDefaultMiddleware({ thunk: false }), sagaMiddleware],
});
sagaMiddleware.run(rootSaga);

export default store;
