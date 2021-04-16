package com.example.keep_exploring.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.api.Api_User;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Image;
import com.example.keep_exploring.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout etDisplayName, etEmail, etPassword;

    private TextView tvSignIn;
    private CircleImageView imgAvatar;
    private Button btnSignUp;
    private AlertDialog dialog;
    private Api_User apiUser;
    private String realPath= "";
    private static final int PICK_IMAGE_CODE = 1;
    private Helper_Image helper_image;
    private DAO_Auth dao_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("SIGN UP");
        apiUser = Retrofit_config.retrofit.create(Api_User.class);
        helper_image = new Helper_Image(this);
        dao_auth = new DAO_Auth(this);
        initView();
    }
    private void initView() {
        etDisplayName = (TextInputLayout) findViewById(R.id.signUp_etDisplayName);
        etEmail = (TextInputLayout) findViewById(R.id.signUp_etEmail);
        etPassword = (TextInputLayout) findViewById(R.id.signUp_etPassword);
        imgAvatar = (CircleImageView) findViewById(R.id.signUp_imgAvatar);
        btnSignUp = (Button) findViewById(R.id.signUp_btnRegister);
        tvSignIn = (TextView) findViewById(R.id.signUp_tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        dialog = new SpotsDialog(this);

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
                String email = etEmail.getEditText().getText().toString();
                String pass = etPassword.getEditText().getText().toString();
                String displayName = etDisplayName.getEditText().getText().toString();
                dao_auth.signUp(realPath, email, pass, displayName,new Helper_Callback(){
                    @Override
                    public void successReq(Object response) {
                        User user = (User) response;
                        toast("Tạo tài khoản thành công!");
//                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                        log(user.toString());
                    }

                    @Override
                    public void failedReq(String msg) {
                        toast(msg);
                    }
                });
            }
        });

    }
    private void log(String s) {
        Log.d("log",s);
    }
    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CODE && data.getData() != null) {
            Uri uri = data.getData();
            imgAvatar.setImageURI(uri);
            realPath = helper_image.getPathFromUri(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
