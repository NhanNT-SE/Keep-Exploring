import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from './../../store/app.state';
import { getUserList } from './../../store/auth/auth.actions';
import { auth_statusSelector } from './../../store/auth/auth.selector';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  status$ = this.store.select(auth_statusSelector);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(getUserList());
    
  }
}
