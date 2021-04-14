package com.example.keep_exploring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;

public class SignInActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private Button btnLogIn, btnJustGo;
    private TextView tvSignUp, tvForgot;
    private DAO_Auth dao_auth;
    private Helper_Common helper_common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle("SIGN IN");
        dao_auth = new DAO_Auth(this);
        helper_common= new Helper_Common();
        helper_common.runtimePermission(this);
        initView();
    }
    private void initView() {
        etEmail = (EditText) findViewById(R.id.signIn_etEmail);
        etPass = (EditText) findViewById(R.id.signIn_etPass);
        etPass = (EditText) findViewById(R.id.signIn_etPass);
        btnLogIn = (Button) findViewById(R.id.signIn_btnSignIn);
        btnJustGo = (Button) findViewById(R.id.signIn_btnJustGo);
        tvSignUp = (TextView) findViewById(R.id.signIn_tvSignUp);
        tvForgot = (TextView) findViewById(R.id.signIn_tvForgot);
        btnJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });

      btnLogIn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String email = etEmail.getText().toString();
              String pass = etPass.getText().toString();
              dao_auth.signIn(email,pass,new Helper_Callback(){
                  @Override
                  public void failedReq(String message) {
                      toast(message);
                  }

                  @Override
                  public void successReq(Object response) {
                      toast("Đăng nhâp thành công");
                      startActivity(new Intent(SignInActivity.this, MainActivity.class));
                  }
              });
          }
      });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    //dialogForgot
//    private void dialogForgot() {
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_forgot_pasword);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        final EditText dEtEmail = (EditText) dialog.findViewById(R.id.dForgot_etEmail);
//        final EditText dEtPass = (EditText) dialog.findViewById(R.id.dForgot_etPass);
//        final EditText dEtPhone = (EditText) dialog.findViewById(R.id.dForgot_etPhone);
//        final EditText dEtBirthDay = (EditText) dialog.findViewById(R.id.dForgot_etBirthDay);
//        Button btnNext = (Button) dialog.findViewById(R.id.dForgot_btnNext);
//        Button btnCancel = (Button) dialog.findViewById(R.id.dForgot_btnCancel);
//        dEtPass.setKeyListener(null);
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String email = dEtEmail.getText().toString();
//                final String birthday = dEtBirthDay.getText().toString();
//                final String phone = dEtPhone.getText().toString();
//                if (email.isEmpty() || birthday.isEmpty() || phone.isEmpty()) {
//                    toast("Please, fill up the form!");
//                    return;
//                }
//                dao_user.getAllData(new FirebaseCallback() {
//                    @Override
//                    public void userList(List<User> userList) {
//                        for (User user : userList) {
//                            if (user.getEmail().equals(email) && user.getBirthDay().equals(birthday)
//                                    && user.getPhoneNumber().equals(phone)) {
//                                dEtPass.setText("Your password: " + user.getPassword());
//                                return;
//                            }
//                        }
//                        dEtPass.setText("Couldn't find your email");
//                    }
//                });
//
//
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//
//    }
    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
