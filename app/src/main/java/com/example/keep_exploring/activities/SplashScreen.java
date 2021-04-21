package com.example.keep_exploring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.keep_exploring.R;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;

public class SplashScreen extends AppCompatActivity {
    private ImageView imgLogo;
    private TextView tvSlogan, tvAppName;
    private Animation animTop, animBottom;
    private Helper_SP helper_sp;
    private boolean isRemember;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imgLogo = (ImageView) findViewById(R.id.splash_screen_imgLogo);
        tvSlogan = (TextView) findViewById(R.id.splash_screen_tvSlogan);
        tvAppName = (TextView) findViewById(R.id.splash_screen_tvAppName);
        animTop = AnimationUtils.loadAnimation(this, R.anim.splash_top_anim);
        animBottom = AnimationUtils.loadAnimation(this, R.anim.splash_bottom_anim);
        imgLogo.setAnimation(animTop);
        tvAppName.setAnimation(animBottom);
        tvSlogan.setAnimation(animBottom);
        helper_sp = new Helper_SP(this);
        isRemember = helper_sp.getIsRemember();
        user = helper_sp.getUser();
        if (user != null && isRemember) {
            moveToMain();
        } else {
            moveToSinIn();
        }
    }
    private void moveToMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                finish();
                startActivity(intent);
                Animatoo.animateSlideUp(SplashScreen.this);

            }
        }, 3000);
    }
    private void moveToSinIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, SignInActivity.class);
                finish();
                startActivity(intent);
                Animatoo.animateSlideLeft(SplashScreen.this);
            }
        }, 3000);
    }
}
