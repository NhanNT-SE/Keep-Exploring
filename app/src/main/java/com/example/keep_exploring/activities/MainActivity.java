package com.example.keep_exploring.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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

import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //    View
    private BottomNavigationView bottomNavigationView;
    private Helper_Common helper_common;
    private FloatingActionButton fabAdd, fabAddPost, fabAddBlog;
    private SpotsDialog spotsDialog;
    //    DAO & Helper
    private Helper_SP helper_sp;
    private DAO_Auth dao_auth;
    private DAO_Address dao_address;
    private User user;
    private Animation animOpen, animClose, animFromBottom, animToBottom;
    private boolean clicked = false;
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
        fabAdd = (FloatingActionButton) findViewById(R.id.main_fabAdd);
        fabAddPost = (FloatingActionButton) findViewById(R.id.main_fabAddPost);
        fabAddBlog = (FloatingActionButton) findViewById(R.id.main_fabAddBlog);
    }
    private void initVariable() {
        dao_address = new DAO_Address(this);
        dao_auth = new DAO_Auth(this);
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(this);
        animOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open_anim);
        animClose = AnimationUtils.loadAnimation(this, R.anim.fab_close_anim);
        animFromBottom = AnimationUtils.loadAnimation(this, R.anim.fab_from_bottom_anim);
        animToBottom = AnimationUtils.loadAnimation(this, R.anim.fab_to_bottom_anim);
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
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    helper_common.dialogRequireLogin(MainActivity.this);
                } else {
                    toggleAnim();
                }
            }
        });

        fabAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnim();
                replaceFragment(new Fragment_AddPost());
            }
        });

        fabAddBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnim();
                replaceFragment(new Fragment_AddBlog());
            }
        });
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
        helper_common.replaceFragment(this, fragment);
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

    private void toggleAnim() {
        setVisibility(clicked);
        setClickable(clicked);
        setAnimation(clicked);
        clicked = !clicked;

    }
    private void setVisibility(boolean clicked) {
        if (!clicked) {
            fabAddPost.setVisibility(View.VISIBLE);
            fabAddBlog.setVisibility(View.VISIBLE);
        } else {
            fabAddPost.setVisibility(View.GONE);
            fabAddBlog.setVisibility(View.GONE);

        }

    }
    private void setAnimation(boolean clicked) {
        if (!clicked) {
            fabAddPost.startAnimation(animFromBottom);
            fabAddBlog.startAnimation(animFromBottom);
            fabAdd.startAnimation(animOpen);

        } else {
            fabAddPost.startAnimation(animToBottom);
            fabAddBlog.startAnimation(animToBottom);
            fabAdd.startAnimation(animClose);
        }
    }
    private void setClickable(boolean clicked) {
        if (!clicked) {
            fabAddPost.setClickable(true);
            fabAddBlog.setClickable(true);
        } else {
            fabAddPost.setClickable(false);
            fabAddBlog.setClickable(false);
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
