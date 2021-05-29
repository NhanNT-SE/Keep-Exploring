import { User } from './../../model/user.model';
import {
  getUserList,
  getUserListSuccess,
  getUserListFail,
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
      ofType(getUserList),
      mergeMap(() => this.authService.getUserList()),
      map((userList: any) => getUserListSuccess({ userList })),
      catchError((error) => of(getUserListFail({ error })))
    )
  );
  constructor(private action$: Actions, private authService: AuthService) {}
}
