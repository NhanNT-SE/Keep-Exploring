package com.example.keep_exploring.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.model.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private Button btnLogIn, btnJustGo;


    private TextView tvSignUp, tvForgot;
    private DAO_Auth dao_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle("SIGN IN");
        dao_auth = new DAO_Auth(this);
        runtimePermission();
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
                      User user = (User) response;
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


    private void runtimePermission() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    log("All permissions granted");
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
