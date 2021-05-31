import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import * as lodash from 'lodash';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm = new FormGroup(
    {
      username: new FormControl('', [
        Validators.required,
        Validators.pattern(/^[a-zA-Z0-9_]{5,}[a-zA-Z]+[0-9]*$/),
      ]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/
        ),
      ]),
      confirmPassword: new FormControl('', [Validators.required]),
    },
    { validators: this.validateConfirmPassword('password', 'confirmPassword') }
  );
  user: any;
  isAccept: boolean = false;
  constructor(private router: Router) {}

  ngOnInit(): void {
    this.user = {
      username: '',
      email: '',
      password: '',
    };
  }
  signUp() {
    const user = lodash.pick(this.registerForm.value, [
      'username',
      'email',
      'password',
    ]);
    console.log(user);
  }

  getErrorValidation(nameInput: string) {
    let errorMsg;
    const control = this.registerForm.get(nameInput);
    const controlErrors = control?.errors;

    const isDirty = control?.dirty;
    const errForm = this.registerForm.errors;
    if (nameInput === 'confirmPassword' && isDirty && errForm) {
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
          errorMsg = 'Please enter a valid email address';
          break;
        case 'minlength':
          errorMsg = `${nameInput} should contain at least 6 characters`;
          break;
        case 'pattern':
          if (nameInput === 'password') {
            errorMsg = `${nameInput} should contain at least 8 characters, 1 uppercase, 1 lowercase, 1 numeric & special character`;
          }
          if (nameInput === 'username') {
            errorMsg = `${nameInput} should contain at least 6 characters. At least 5 alphabetical and the underscore.
             Only alphabetical letters, '-' and numeric acceptable`;
          }
          break;
        default:
          break;
      }
    }
    return errorMsg;
  }
  validateConfirmPassword(password: string, confirmPassword: string) {
    return function (formGroup: FormGroup) {
      const { value: passwordValue } = formGroup.get(password);
      const { value: confirmValue } = formGroup.get(confirmPassword);
      return passwordValue === confirmValue
        ? null
        : {
            valueNotMatch: {
              message: 'Password and confirm password does not match',
            },
          };
    };
  }
  signIn() {
    this.router.navigate(['login']);
  }
}
