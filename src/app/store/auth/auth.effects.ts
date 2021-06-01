import { User } from './../../model/user.model';
import {
  signIn,
  setUser,
  signInFail,
} from './auth.actions';
import { AuthService } from './../../services/auth.service';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { mergeMap, map, catchError, takeLast } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable()
export class AuthEffects {
  getUserList$ = createEffect(() =>
    this.action$.pipe(
      ofType(signIn),
      mergeMap(() => this.authService.getUserList()),
      map((user: User) => setUser({ user })),
      catchError((error) => of(signInFail({ error })))
    )
  );
  constructor(private action$: Actions, private authService: AuthService) {}
}
