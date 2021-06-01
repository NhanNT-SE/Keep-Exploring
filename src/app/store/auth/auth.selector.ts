import { AuthState } from './auth.state';
import { createFeatureSelector, createSelector } from '@ngrx/store';

export const feature_auth = createFeatureSelector<AuthState>('feature_auth');

export const auth_userSelector = createSelector(
  feature_auth,
  (state) => state.user
);
export const auth_statusSelector = createSelector(
  feature_auth,
  (state) => state.status
);
export const auth_errorSelector = createSelector(
  feature_auth,
  (state) => state.error
);
