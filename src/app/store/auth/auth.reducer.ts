import {
  AuthActions,
  setUser,
  signIn,
  signInFail,
  signOut,
  signOutFail,
} from './auth.actions';
import { createReducer, on } from '@ngrx/store';
import { AuthState } from './auth.state';
const initialState: AuthState = {
  user: null,
  status: 'idle',
  error: '',
};

const _authReducer = createReducer(
  initialState,
  on(signIn, (state) => ({ ...state, status: 'loading' })),
  on(signOut, (state) => ({ ...state, status: 'loading' })),
  on(signInFail, (state, { error }) => ({ user: null, status: 'fail', error })),
  on(signOutFail, (state, { error }) => ({ ...state, status: 'fail', error })),
  on(setUser, (state, { user }) => ({
    ...state,
    status: 'success',
    user,
  }))
);
export const authReducer = (state: AuthState, action: AuthActions) => {
  return _authReducer(state, action);
};
