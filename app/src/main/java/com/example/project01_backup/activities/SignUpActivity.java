package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.api.Retrofit_config;
import com.example.project01_backup.api.UserApi;
import com.example.project01_backup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout etDisplayName, etEmail, etPassword;

    private TextView tvSignIn;
    private CircleImageView imgAvatar;
    private Button btnSignUp;
    private AlertDialog dialog;
    private UserApi userApi;
    private String realPath = "";
    private static final int PICK_IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("SIGN UP");
        userApi = Retrofit_config.retrofit.create(UserApi.class);
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
                String displayName = etDisplayName.getEditText().getText().toString();
                String email = etEmail.getEditText().getText().toString();
                String pass = etPassword.getEditText().getText().toString();

                if (displayName.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                File file = new File(realPath);

                RequestBody requestBody;
                if (realPath.isEmpty()) {
                    requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                } else {
                    requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                }
                MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", realPath, requestBody);

                RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), displayName);
                RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
                RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), pass);



                Call<String> callSignUp = userApi.signUp(nameBody,emailBody,passBody);
                callSignUp.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("TAG", "onResponse: " + response.body());
                        try {
                            JSONObject responseData = new JSONObject(response.body());
//                            JSONObject data = responseData.getJSONObject("data");
                            if (responseData.has("error")) {
                                JSONObject error = responseData.getJSONObject("error");
                                String status = error.getString("status");
                                if (status == "201") {
                                    Toast.makeText(SignUpActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
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
        });

    }




    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


//    private String getPathFromUri(Uri uri) {
//        final String docId = DocumentsContract.getDocumentId(uri);
//        final String[] split = docId.split(":");
//        final String type = split[0];
//        Uri contentUri = null;
//        if ("image".equals(type)) {
//            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        } else if ("video".equals(type)) {
//            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        } else if ("audio".equals(type)) {
//            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        }
//        final String selection = "_id=?";
//        final String[] selectionArgs = new String[]{
//                split[1]
//        };
//        return getDataColumn(contentUri, selection, selectionArgs);
//
//    }
//
//
//    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//        try {
//            cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return "";
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && data != null) {
            Uri uri = data.getData();
            imgAvatar.setImageURI(uri);
//            realPath = getPathFromUri(uri);
//            Log.d("realpath", realPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
