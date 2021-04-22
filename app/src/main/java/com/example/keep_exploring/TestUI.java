package com.example.keep_exploring;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Post;
import com.example.keep_exploring.model.User;

import java.util.List;

public class TestUI extends AppCompatActivity {

    private Button btnSignIn, btnGetPost, btnRefreshToken;
    private DAO_Auth dao_auth;
    private DAO_Post dao_post;
    private Helper_SP helper_sp;
    private User user;
    private String accessToken;
    private Helper_Common helper_common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ui);
        dao_auth = new DAO_Auth(this);
        dao_auth = new DAO_Auth(this);
        helper_sp = new Helper_SP(this);
        dao_post = new DAO_Post(this);
        btnSignIn = (Button) findViewById(R.id.testUI_btnSignIn);
        btnGetPost = (Button) findViewById(R.id.testUI_btnGetPost);
        btnRefreshToken = (Button) findViewById(R.id.testUI_btnRefreshToken);
        accessToken = helper_sp.getAccessToken();
        helper_common = new Helper_Common();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "user-test@keep-exploring.com";
                String password = "123456";
                dao_auth.signIn(email, password, new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {
                        user = (User) response;
                        log("LOGIN:\n" + helper_sp.getAccessToken());
                    }

                    @Override
                    public void failedReq(String msg) {

                    }
                });

            }
        });


        btnGetPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPost();
            }
        });

        btnRefreshToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getPost() {
        dao_post.getPostByUser(helper_sp.getAccessToken(), helper_sp.getUser().getId(), new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<Post> postList = (List<Post>) response;
                log("IN Activity\n" + postList.size());

            }

            @Override
            public void failedReq(String msg) {
                if (msg.equalsIgnoreCase(helper_common.REFRESH_TOKEN())) {
                    refresh(new Helper_Callback() {
                        @Override
                        public void successReq(Object response) {
                            getPost();
                        }

                        @Override
                        public void failedReq(String msg) {

                        }
                    });
                } else {
                    helper_common.logOut(TestUI.this);
                }
            }
        });
    }

    private void refresh(Helper_Callback callback) {
        dao_auth.refreshToken(callback);
    }

    private void log(String msg) {
        Log.d("log", "Test UI: " + msg);
    }


}