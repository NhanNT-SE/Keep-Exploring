import { User } from './../../model/user.model';
import { ActionType, createAction, props } from '@ngrx/store';
export const GET_USER_LIST = '[Auth] get user list';
export const GET_USER_LIST_SUCCESS = '[Auth] get user list success';
export const GET_USER_LIST_FAIL = '[Auth] get user list fail';
export const GET_USER = '[Auth] get user';
export const GET_USER_SUCCESS = '[Auth] get user success';
export const GET_USER_FAIL = '[Auth] get user fail';
export const ADD_USER = '[Auth] add user';
export const UPDATE_USER = '[Auth] update user';
export const DELETE_USER = '[Auth] delete user';
export const SORT_USER_LIST = '[Auth] sort user list';

export const getUserList = createAction(GET_USER_LIST);
export const getUserListSuccess = createAction(
  GET_USER_LIST_SUCCESS,
  props<{ userList: User[] }>()
);
export const getUserListFail = createAction(
  GET_USER_LIST_FAIL,
  props<{ error?: string }>()
);
export const getUser = createAction(GET_USER, props<{ userId: number }>());
export const getUserSuccess = createAction(
  GET_USER_SUCCESS,
  props<{ user: User }>()
);
export const getUserFail = createAction(
  GET_USER_FAIL,
  props<{ error?: string }>()
);
export const addUser = createAction(ADD_USER, props<{ user: User }>());
export const updateUser = createAction(
  UPDATE_USER,
  props<{ userId: number; user: User }>()
);
export const deleteUser = createAction(
  DELETE_USER,
  props<{ userId: number }>()
);
export const sortUserList = createAction(
  SORT_USER_LIST,
  props<{ sort: 'asc' | 'desc' | null }>()
);


export type AuthActions =
  | ActionType<typeof getUserList>
  | ActionType<typeof getUserListSuccess>
  | ActionType<typeof getUserListFail>
  | ActionType<typeof getUser>
  | ActionType<typeof getUserSuccess>
  | ActionType<typeof getUserFail>
  | ActionType<typeof addUser>
  | ActionType<typeof updateUser>
  | ActionType<typeof deleteUser>
  | ActionType<typeof sortUserList>
