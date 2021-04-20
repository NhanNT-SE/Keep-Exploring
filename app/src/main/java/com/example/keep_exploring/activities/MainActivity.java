package com.example.keep_exploring.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Address;
import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_AddBlog;
import com.example.keep_exploring.fragment.Fragment_AddPost;
import com.example.keep_exploring.fragment.Fragment_BlogList;
import com.example.keep_exploring.fragment.Fragment_Category;
import com.example.keep_exploring.fragment.Fragment_Notification;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //    View
    private BottomNavigationView bottomNavigationView;
    private Helper_Common helper_common;
    private CircleMenuView circleMenuView;
    private FloatingActionButton fab;
    private SpotsDialog spotsDialog;
    //    DAO & Helper
    private Helper_SP helper_sp;
    private DAO_Auth dao_auth;
    private DAO_Address dao_address;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Keep Exploring");
        initView();
        initVariable();
        handlerEvent();
    }

    private void initView() {
        spotsDialog = new SpotsDialog(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottomNavigation);
        circleMenuView = findViewById(R.id.main_circleMenu);
        fab = (FloatingActionButton) findViewById(R.id.main_fabAdd);
    }
    private void initVariable() {
        dao_address = new DAO_Address(this);
        dao_auth = new DAO_Auth(this);
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(this);
        user = helper_sp.getUser();
    }
    private void handlerEvent() {
        helper_common.runtimePermission(this);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        dao_address.getProvinceList(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<String> provinceList = (List<String>) response;
            }

            @Override
            public void failedReq(String msg) {

            }
        });
        circleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                super.onMenuOpenAnimationStart(view);
            }
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                switch (buttonIndex) {
                    case 0:
                        view.setVisibility(View.GONE);
                        replaceFragment(new Fragment_AddBlog());
                        break;
                    case 1:
                        view.setVisibility(View.GONE);
                        replaceFragment(new Fragment_AddPost());
                        break;
                }
            }
            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                super.onMenuCloseAnimationStart(view);
                view.setVisibility(View.GONE);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    helper_common.dialogRequireLogin(MainActivity.this);
                } else {
                    showCircleMenu();
                }
            }
        });
        hideCircleMenu();
        replaceFragment(new Fragment_Category());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bottom_post:
                replaceFragment(new Fragment_Category());
                break;
            case R.id.menu_bottom_blog:
                replaceFragment(new Fragment_BlogList());
                break;
            case R.id.menu_bottom_notify:
                if (user == null) {
                    helper_common.dialogRequireLogin(this);
                } else {
                    replaceFragment(new Fragment_Notification());
                }
                break;
            case R.id.menu_bottom_profile:
                if (user == null) {
                    helper_common.dialogRequireLogin(this);
                } else {
                    replaceFragment(new Fragment_Tab_UserInfo());
                }
                break;
        }
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        hideCircleMenu();
        helper_common.replaceFragment(this, fragment);
    }
    private void showCircleMenu() {
        circleMenuView.setVisibility(View.VISIBLE);
        circleMenuView.open(true);
    }

    private void hideCircleMenu() {
        circleMenuView.close(false);
        circleMenuView.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return (true);
        } else if (item.getItemId() == R.id.menu_home_logout) {
            spotsDialog.show();
            dao_auth.signOut(new Helper_Callback() {
                @Override
                public void successReq(Object response) {
                    spotsDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }

                @Override
                public void failedReq(String msg) {
                    spotsDialog.dismiss();
                }
            });
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
