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

import com.example.project01_backup.DAO.DAO_Auth;
import com.example.project01_backup.R;
import com.example.project01_backup.api.Retrofit_config;
import com.example.project01_backup.api.UserApi;
import com.example.project01_backup.helpers.Helper_Image;
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
    private String realPath= "";
    private static final int PICK_IMAGE_CODE = 1;
    private Helper_Image helper_image;
    private DAO_Auth dao_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("SIGN UP");
        userApi = Retrofit_config.retrofit.create(UserApi.class);
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
                String email = "user-android1";
                String pass = "123456";
                String displayName = "User Android 1";
                dao_auth.signUp(realPath, email, pass, displayName);
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
