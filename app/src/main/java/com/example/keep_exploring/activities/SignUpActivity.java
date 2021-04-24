package com.example.keep_exploring.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.api.Api_User;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Image;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity {
    //View
    private TextInputLayout tilDisplayName, tilEmail, tilPassword;
    private Button btnSignUp, btnSignIn;
    private SpotsDialog spotsDialog;
    private ImageView imgAvatar;
    //DAO & Helper;
    private static final int PICK_IMAGE_CODE = 1;
    private Api_User apiUser;
    private Helper_Image helper_image;
    private DAO_Auth dao_auth;
    private boolean isValid;
    private String path, suffixEmil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initVariable();
        handlerEvent();
    }

    private void initView() {
        tilDisplayName = (TextInputLayout) findViewById(R.id.signUp_tilDisplayName);
        tilEmail = (TextInputLayout) findViewById(R.id.signUp_tilEmail);
        tilPassword = (TextInputLayout) findViewById(R.id.signUp_tilPassword);
        btnSignUp = (Button) findViewById(R.id.signUp_btnSignUp);
        btnSignIn = (Button) findViewById(R.id.signUp_btnSignIn);
        imgAvatar = (ImageView) findViewById(R.id.signUp_imgAvatar);
        spotsDialog = new SpotsDialog(this);
    }

    private void initVariable() {
        apiUser = Retrofit_config.retrofit.create(Api_User.class);
        helper_image = new Helper_Image(this);
        dao_auth = new DAO_Auth(this);
        suffixEmil = tilEmail.getSuffixText().toString();
        path = "";
        isValid = false;
    }
    private void handlerEvent() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignIn();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_CODE);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilEmail.getEditText().getText().toString();
                String pass = tilPassword.getEditText().getText().toString();
                String displayName = tilDisplayName.getEditText().getText().toString();

                if (email.isEmpty() || pass.isEmpty() || displayName.isEmpty()) {
                    validateRequire(email, pass, displayName);
                } else {
                    spotsDialog.show();
                    dao_auth.signUp(path, email + tilEmail.getSuffixText().toString(), pass, displayName, new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            spotsDialog.dismiss();
                            toast("Tạo tài khoản thành công. Đăng nhập để tiếp tục");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    moveToSignIn();
                                }
                            }, 2000);
                        }

                        @Override
                        public void failedReq(String msg) {
                            spotsDialog.dismiss();
                            toast(msg);
                        }
                    });
                }
            }
        });
        validInput();

    }

    public Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void validInput() {
        tilDisplayName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    tilDisplayName.setError("Tên hiển thị chứa ít nhất 4 ký tự!");
                    tilDisplayName.setErrorEnabled(true);
                    isValid = false;
                } else {
                    tilDisplayName.setErrorEnabled(false);
                    isValid = true;
                }
                toggleBtnSignUp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tilEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    tilEmail.setError("Email phải chứa ít nhất 4 ký tự!");
                    tilEmail.setErrorEnabled(true);
                    isValid = false;
                } else {
                    tilEmail.setErrorEnabled(false);
                    isValid = true;
                }
                toggleBtnSignUp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                toggleBtnSignUp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validateRequire(String email, String pass, String displayName) {
        if (pass.isEmpty()) {
            tilPassword.setError("Mật khẩu không được để trống");
            tilPassword.setErrorEnabled(true);
        } else {
            tilPassword.setErrorEnabled(false);
        }
        if (email.isEmpty()) {
            tilEmail.setError("Email không được để trống");
            tilEmail.setErrorEnabled(true);
        } else {
            tilEmail.setErrorEnabled(false);
        }
        if (displayName.isEmpty()) {
            tilDisplayName.setError("Tên hiển thị không được để trống");
            tilDisplayName.setErrorEnabled(true);
        } else {
            tilDisplayName.setErrorEnabled(false);
        }
        isValid = false;
        toggleBtnSignUp();
    }

    private void toggleBtnSignUp() {
        btnSignUp.setEnabled(isValid);
    }

    private void moveToSignIn() {
        finish();
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        Animatoo.animateSlideRight(SignUpActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CODE && data.getData() != null) {
            Uri uri = data.getData();
            imgAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            imgAvatar.setImageURI(uri);
            path = helper_image.getPathFromUri(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
