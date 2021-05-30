import {
  addUser,
  updateUser,
  deleteUser,
  getUserList,
  getUserListSuccess,
  getUserListFail,
  getUser,
  getUserSuccess,
  getUserFail,
  sortUserList,
  AuthActions,
} from './auth.actions';
import { createReducer, on } from '@ngrx/store';
import { AuthState } from './auth.state';
const initialState: AuthState = {
  userList: [],
  user: null,
  status: 'idle',
  sort: null,
  error: '',
};

const _authReducer = createReducer(
  initialState,
  on(getUserList, (state) => ({ ...state, status: 'loading' })),
  on(getUserListSuccess, (state, { userList }) => ({
    ...state,
    status: 'success',
    userList,
  })),
  on(getUserListFail, (state, { error }) => ({
    ...state,
    status: 'fail',
    error,
  })),
  on(getUser, (state) => ({ ...state, status: 'loading' })),
  on(getUserSuccess, (state, { user }) => ({
    ...state,
    status: 'success',
    user,
  })),
  on(getUserFail, (state, { error }) => ({ ...state, status: 'fail', error })),
  on(addUser, (state, { user }) => ({
    ...state,
    userList: [user, ...state.userList],
  })),
  on(updateUser, (state, { userId, user }) => {
    const index = state.userList.findIndex((user) => user.id === userId);
    if (index > -1) {
      const tempList = [...state.userList];
      tempList[index] = user;
      return { ...state, userList: tempList };
    }
    return { ...state };
  }),
  on(deleteUser, (state, { userId }) => ({
    ...state,
    userList: state.userList.filter((user) => user.id !== userId),
  }))
);
export const authReducer = (state: AuthState, action: AuthActions) => {
  return _authReducer(state, action);
};
