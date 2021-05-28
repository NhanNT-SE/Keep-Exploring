import { AuthEffects } from './auth/auth.effects';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { authReducer } from './auth/auth.reducer';

@NgModule({
  declarations: [],
  imports: [
    StoreModule.forFeature('feature_auth', authReducer),
    EffectsModule.forFeature([AuthEffects]),
  ],
})
export class RootStoreModule {}
