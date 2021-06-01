import { User } from '../../model/user.model';
import { ActionType, createAction, props } from '@ngrx/store';
export const SIGN_IN = '[Auth] Sign in';
export const SIGN_OUT = '[Auth] Sign out';
export const SIGN_IN_FAIL = '[Auth] sign in fail';
export const SIGN_OUT_FAIL = '[Auth] sign out fail';
export const SET_USER = '[Auth] set user';

export const signIn = createAction(SIGN_IN);

export const signOut = createAction(SIGN_OUT);

export const signInFail = createAction(
  SIGN_IN_FAIL,
  props<{ error?: string }>()
);
export const signOutFail = createAction(
  SIGN_IN_FAIL,
  props<{ error?: string }>()
);
export const setUser = createAction(SET_USER, props<{ user: User }>());

export type AuthActions =
  | ActionType<typeof signIn>
  | ActionType<typeof signOut>
  | ActionType<typeof signInFail>
  | ActionType<typeof signOutFail>
  | ActionType<typeof setUser>;
