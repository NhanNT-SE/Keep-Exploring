import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from './../../store/app.state';
import { auth_statusSelector } from './../../store/auth/auth.selector';
import * as lodash from 'lodash';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  status$ = this.store.select(auth_statusSelector);
  loginForm = new FormGroup(
    {
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/
        ),
      ]),
    },
    { validators: this.validateUsername() }
  );
  user: any;
  isRemember: boolean = false;
  constructor(private store: Store<AppState>, private router: Router) {}

  ngOnInit(): void {
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
    const errForm = this.loginForm.errors;
    if (nameInput === 'username' && isDirty && errForm) {
      const err = Object.keys(errForm)[0];
      errorMsg = errForm[err].message;
      return errorMsg;
    }
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
          errorMsg = `${nameInput} should contain at least 8 characters, 1 uppercase, 1 lowercase, 1 numeric & special character`;

          break;
        default:
          break;
      }
    }
    return errorMsg;
  }
  validateUsername() {
    return function (formGroup: FormGroup) {
      const regexEmail =
        /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
      const regexUsername = /^[a-zA-Z0-9_]{5,}[a-zA-Z]+[0-9]*$/;
      const { value: emailValue } = formGroup.get('username');
      const isValidEmail = regexEmail.test(emailValue);
      const isValidUsername = regexUsername.test(emailValue);
      if (emailValue.includes('@') && !isValidEmail) {
        return {
          valueNotMatch: {
            message: 'Please enter a valid email address',
          },
        };
      } else if (!emailValue.includes('@') && !isValidUsername) {
        return {
          valueNotMatch: {
            message: `username should contain at least 6 characters. At least 5 alphabetical and the underscore.
            Only alphabetical letters, '-' and numeric acceptable`,
          },
        };
      }
      return null;
    };
  }
  signUp() {
    this.router.navigate(['register']);
  }
}
