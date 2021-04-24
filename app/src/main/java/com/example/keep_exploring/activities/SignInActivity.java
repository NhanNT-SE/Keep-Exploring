package com.example.keep_exploring.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_Date;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.services.Notification_Service;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
    private Helper_Date helper_date;
    //    Variable
    private boolean isValid;
    private boolean isRemember;
    private String suffixEmil;
    private Intent serviceNotify;
    private String emailSubmit;

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
        helper_date = new Helper_Date();
        helper_common.runtimePermission(this);
        isValid = false;
        isRemember = false;
        suffixEmil = tilEmail.getSuffixText().toString();
        serviceNotify = new Intent(this, Notification_Service.class);
    }

    private void handlerEvent() {
        validateEmail();
        validatePassword();
        btnJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceNotify != null) {
                    stopService(serviceNotify);
                }
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
                        validateRequire(email, pass);
                } else {
                    spotsDialog.show();
                    dao_auth.signIn(email + suffixEmil, pass, new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            spotsDialog.dismiss();
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
                dialogForgetPass();
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

    private void validateRequire(String email, String pass) {
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
        isValid = false;
        toggleBtnSignIn();
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

    private void dialogForgetPass() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_forgot_pasword);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnSubmit, btnCancel;
        TextInputLayout tilDEmail, tilDisplayName, tilBirthday;

        btnSubmit = (Button) dialog.findViewById(R.id.dForget_btnSubmit);
        btnCancel = (Button) dialog.findViewById(R.id.dForget_btnCancel);
        tilDEmail = (TextInputLayout) dialog.findViewById(R.id.dForget_tilEmail);
        tilDisplayName = (TextInputLayout) dialog.findViewById(R.id.dForget_tilDisplayName);
        tilBirthday = (TextInputLayout) dialog.findViewById(R.id.dForget_tilBirthday);


        tilBirthday.getEditText().setKeyListener(null);
        tilBirthday.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(tilBirthday.getEditText());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilDEmail.getEditText().getText().toString();
                String displayName = tilDisplayName.getEditText().getText().toString();
                String birthDay = tilBirthday.getEditText().getText().toString();

                if (email.isEmpty() || displayName.isEmpty() || birthDay.isEmpty()) {
                    if (email.isEmpty()) {
                        tilDEmail.setError("Email không được để trống");
                        tilDEmail.setErrorEnabled(true);
                        btnSubmit.setEnabled(false);
                    } else {
                        btnSubmit.setEnabled(true);
                    }
                    if (displayName.isEmpty()) {
                        tilDisplayName.setError("Tên hiển thị không được để trống");
                        tilDisplayName.setErrorEnabled(true);
                        btnSubmit.setEnabled(false);
                    } else {
                        btnSubmit.setEnabled(true);

                    }
                    if (birthDay.isEmpty()) {
                        tilBirthday.setError("Ngày sinh không được để trống");
                        tilBirthday.setErrorEnabled(true);
                        btnSubmit.setEnabled(false);
                    } else {
                        tilBirthday.setErrorEnabled(false);
                        btnSubmit.setEnabled(true);

                    }
                } else {
                    emailSubmit = email + tilDEmail.getSuffixText().toString();
                    String bBirthday = helper_date.convertStringToIsoDate(birthDay);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("bod", bBirthday);
                    map.put("email", emailSubmit);
                    map.put("displayName", displayName);
                    forgetPassword(map, dialog);

                }
            }
        });

        tilDisplayName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    tilDisplayName.setError("Tên hiển thị phải chứa ít nhất 4 ký tự");
                    tilDisplayName.setErrorEnabled(true);
                    btnSubmit.setEnabled(false);
                } else {
                    tilDisplayName.setErrorEnabled(false);
                    btnSubmit.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tilDEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    tilDEmail.setError("Eamil phải chứa ít nhất 4 ký tự");
                    tilDEmail.setErrorEnabled(true);
                    btnSubmit.setEnabled(false);
                } else {
                    tilDEmail.setErrorEnabled(false);
                    btnSubmit.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tilBirthday.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilBirthday.setErrorEnabled(false);
                btnSubmit.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    private void dialogGetNewPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_get_new_pasword);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnSubmit, btnCancel;
        TextInputLayout tilNewPass, tilConfirmPass;
        btnSubmit = (Button) dialog.findViewById(R.id.dNewPassword_btnSubmit);
        btnCancel = (Button) dialog.findViewById(R.id.dNewPassword_btnCancel);
        tilNewPass = (TextInputLayout) dialog.findViewById(R.id.dNewPassword_tILNewPassword);
        tilConfirmPass = (TextInputLayout) dialog.findViewById(R.id.dNewPassword_tILConfirmPassword);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = tilNewPass.getEditText().getText().toString();
                String confirmPass = tilConfirmPass.getEditText().getText().toString();
                if (newPass.isEmpty() || confirmPass.isEmpty()) {
                    if (newPass.isEmpty()) {
                        tilNewPass.setError("Mật khẩu không được để trống");
                        tilNewPass.setErrorEnabled(true);
                    }
                    if (confirmPass.isEmpty()) {
                        tilConfirmPass.setError("Mật khẩu xác nhận không được để trống");
                        tilConfirmPass.setErrorEnabled(true);
                    }
                    btnSubmit.setEnabled(false);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", emailSubmit);
                    map.put("newPass", newPass);
                    getNewPassword(map);
                    dialog.dismiss();
                }
            }
        });

        tilNewPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    tilNewPass.setError("Mật khẩu phải chứa ít nhất 6 ký tự");
                    tilNewPass.setErrorEnabled(true);
                    btnSubmit.setEnabled(false);
                } else {
                    tilNewPass.setErrorEnabled(false);
                    btnSubmit.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tilConfirmPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String match = tilNewPass.getEditText().getText().toString();
                if (s.toString().equals(match)) {
                    tilConfirmPass.setErrorEnabled(false);
                    btnSubmit.setEnabled(true);
                } else {
                    tilConfirmPass.setError("Mật khẩu xác nhận của bạn không khớp với mật khẩu mới");
                    tilConfirmPass.setErrorEnabled(true);
                    btnSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.show();
    }

    private void toggleBtnSignIn() {
        btnSignIn.setEnabled(isValid);
    }

    private void getNewPassword(HashMap<String, String> map) {
        spotsDialog.show();
        dao_auth.getNewPassword(map, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                spotsDialog.dismiss();
                toast("Mật khẩu của bạn đã được cập nhật");
            }

            @Override
            public void failedReq(String msg) {
                spotsDialog.dismiss();
            }
        });
    }

    private void forgetPassword(HashMap<String, String> map, Dialog dialog) {
        spotsDialog.show();
        dao_auth.forgetPassword(map, new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                dialog.dismiss();
                dialogGetNewPassword();
                spotsDialog.dismiss();
            }

            @Override
            public void failedReq(String msg) {
                spotsDialog.dismiss();
                toast(msg);
            }
        });
    }

    private void datePicker(final EditText et) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                et.setText(format.format(calendar.getTime()));
            }
        }, year, month, day);

        dialog.show();
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
