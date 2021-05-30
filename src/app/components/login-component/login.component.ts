import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from './../../store/app.state';
import { getUserList } from './../../store/auth/auth.actions';
import { auth_statusSelector } from './../../store/auth/auth.selector';
import * as lodash from 'lodash';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  status$ = this.store.select(auth_statusSelector);
  loginForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/),
    ]),
  });
  user: any;
  isRemember: boolean = false;
  constructor(private store: Store<AppState>, private router: Router) {}

  ngOnInit(): void {
    this.store.dispatch(getUserList());
    this.user = {
      username: '',
      password: '',
    };
  }

  signIn() {
    const user = lodash.cloneDeep(this.loginForm.value);
    console.log(user);
  }

  getErrorValidation(nameInput: string) {
    let errorMsg;
    const control = this.loginForm.get(nameInput);
    const controlErrors = control?.errors;
    const isDirty = control?.dirty;
    if (isDirty && controlErrors != null) {
      const err = Object.keys(controlErrors)[0];
      switch (err) {
        case 'required':
          errorMsg = `${nameInput} is required field`;
          break;
        case 'email':
          errorMsg = `${nameInput} not valid email address`;
          break;
        case 'minlength':
          errorMsg = `${nameInput} should contain at least 6 characters`;
          break;
        case 'pattern':
          errorMsg = `${nameInput} should contain at least 8 characters, 1 uppercase, 1 lowercase, 1 numeric`;
          break;
        default:
          break;
      }
    }
    return errorMsg;
  }

  signUp() {
    this.router.navigate(['register']);
  }
}
