package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.api.Retrofit_config;
import com.example.project01_backup.api.UserApi;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private Button btnLogIn, btnJustGo;
    private TextView tvSignUp, tvForgot;
    private UserApi userApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle("SIGN IN");
        userApi = Retrofit_config.retrofit.create(UserApi.class);
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
                if (etEmail.getText().toString().isEmpty() || etPass.getText().toString().isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    String email = etEmail.getText().toString();
                    String pass = etPass.getText().toString();
                    User user = new User();
                    user.setEmail(email);
                    user.setPass(pass);
                    Log.d("TAG", "onClickuser: " + user);


                    Call<String> callSignIn = userApi.signIn(user);
                    callSignIn.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d("TAG", "onResponse: " + response.body());
                            try {
                                JSONObject responseData = new JSONObject(response.body());
                                JSONObject data = responseData.getJSONObject("data");
                                if (responseData.has("error")) {
                                    JSONObject error = responseData.getJSONObject("error");
                                    String status = error.getString("status");
                                    if (status == "201") {
                                        Toast.makeText(SignInActivity.this, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (status == "202") {
                                        Toast.makeText(SignInActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                String accessToken = data.getString("accessToken");
                                String refreshToken = data.getString("refreshToken");
                                String idUser = data.getString("_id");
                                saveToken("accessToken", accessToken);
                                saveToken("refreshToken", refreshToken);
                                saveToken("idUser", idUser);



                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("TAG", "onFailure: " + t.getMessage());
                        }
                    });
                }
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

    public void saveToken(String key, String value) {
        // The created file can only be accessed by the calling application
        SharedPreferences sharedPreferences = this.getSharedPreferences("storage_token", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        // Save.
        editor.apply();
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

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
