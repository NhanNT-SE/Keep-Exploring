package com.example.keep_exploring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.google.android.material.textfield.TextInputLayout;

import dmax.dialog.SpotsDialog;

public class SignInActivity extends AppCompatActivity {
    //    View
    private TextInputLayout tilEmail, tilPassword;
    private Button btnSignIn, btnJustGo, btnSignUp, btnForget;
    private CheckBox chkRemember;
    private SpotsDialog spotsDialog;
    //    DAO & Helper
    private DAO_Auth dao_auth;
    private Helper_Common helper_common;
    private Helper_SP helper_sp;
    //    Variable
    private boolean isValid;
    private boolean isRemember;
    private String suffixEmil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initVariable();
        handlerEvent();
    }

    private void initView() {
        spotsDialog = new SpotsDialog(this);
        tilEmail = (TextInputLayout) findViewById(R.id.signIn_tilEmail);
        tilPassword = (TextInputLayout) findViewById(R.id.signIn_tilPassword);
        btnSignIn = (Button) findViewById(R.id.signIn_btnSignIn);
        btnSignUp = (Button) findViewById(R.id.signIn_btnSignUp);
        btnJustGo = (Button) findViewById(R.id.signIn_btnJustGo);
        btnForget = (Button) findViewById(R.id.signIn_btnForget);
        chkRemember = (CheckBox) findViewById(R.id.singIn_chkRemember);
    }

    private void initVariable() {
        dao_auth = new DAO_Auth(this);
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(this);
        helper_common.runtimePermission(this);
        isValid = false;
        isRemember = false;
        suffixEmil = tilEmail.getSuffixText().toString();

    }

    private void handlerEvent() {
        validateEmail();
        validatePassword();
        btnJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_sp.clearSP();
                moveToMain();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilEmail.getEditText().getText().toString();
                String pass = tilPassword.getEditText().getText().toString();
                if (email.isEmpty() || pass.isEmpty()) {
                    toast("Vui lòng điền đầy đủ email và mật khẩu");
                } else {
                    spotsDialog.show();
                    dao_auth.signIn(email + suffixEmil, pass, new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            User user = (User) response;
                            spotsDialog.dismiss();
                            toast("Xin chào " + user.getDisplayName());
                            helper_sp.setIsRemember(isRemember);
                            moveToMain();

                        }
                        @Override
                        public void failedReq(String message) {
                            toast(message);
                            spotsDialog.dismiss();
                        }


                    });
                }
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                Animatoo.animateSlideLeft(SignInActivity.this);
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemember = isChecked;
            }
        });
    }

    private void moveToMain() {
        finish();
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        Animatoo.animateSlideUp(SignInActivity.this);
    }

    private void validatePassword() {
        tilPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    tilPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                    tilPassword.setErrorEnabled(true);
                    isValid = false;
                } else {
                    tilPassword.setErrorEnabled(false);
                    isValid = true;
                }
                toggleBtnSignIn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validateEmail() {
        tilEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    tilEmail.setError("Email phải chứa ít nhất 4 ký tự");
                    tilEmail.setErrorEnabled(true);
                    isValid = false;
                } else {
                    tilEmail.setErrorEnabled(false);
                    isValid = true;
                }
                toggleBtnSignIn();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void toggleBtnSignIn() {
        btnSignIn.setEnabled(isValid);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
