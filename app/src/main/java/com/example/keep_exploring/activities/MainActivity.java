package com.example.keep_exploring.activities;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Address;
import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Notification;
import com.example.keep_exploring.R;
import com.example.keep_exploring.api.Socket_Client;
import com.example.keep_exploring.fragment.Fragment_AddBlog;
import com.example.keep_exploring.fragment.Fragment_AddPost;
import com.example.keep_exploring.fragment.Fragment_BlogList;
import com.example.keep_exploring.fragment.Fragment_Blog_Detail;
import com.example.keep_exploring.fragment.Fragment_Category;
import com.example.keep_exploring.fragment.Fragment_Notification;
import com.example.keep_exploring.fragment.Fragment_Post_Details;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Notification;
import com.example.keep_exploring.model.User;
import com.example.keep_exploring.services.Notification_Job_Service;
import com.example.keep_exploring.services.Notification_Service;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //    View
    private BottomNavigationView bottomNavigationView;
    private Helper_Common helper_common;
    private FloatingActionButton fabAdd, fabAddPost, fabAddBlog;
    private LinearLayout layoutAddPost, layoutAddBlog;
    private SpotsDialog spotsDialog;
    private BadgeDrawable badgeNotify;
    //    DAO & Helper
    private DAO_Auth dao_auth;
    private DAO_Address dao_address;
    private DAO_Notification dao_notification;
    private User user;
    private Helper_SP helper_sp;
    private Socket mSocket;
    private Animation animOpen, animClose, animFromBottom, animToBottom;
    private boolean clicked = false;
    private boolean isNotify = false;
    private JobScheduler jobScheduler;

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
        layoutAddPost = (LinearLayout) findViewById(R.id.main_layoutAddPost);
        layoutAddBlog = (LinearLayout) findViewById(R.id.main_layoutAddBlog);
        badgeNotify = bottomNavigationView.getOrCreateBadge(R.id.menu_bottom_notify);
        badgeNotify.setMaxCharacterCount(3);
        badgeNotify.setBadgeTextColor(Color.WHITE);
        badgeNotify.setBackgroundColor(Color.parseColor("#366577"));
    }
    private void initVariable() {
        dao_address = new DAO_Address(this);
        dao_auth = new DAO_Auth(this);
        dao_notification = new DAO_Notification(this);
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(this);
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        animOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open_anim);
        animClose = AnimationUtils.loadAnimation(this, R.anim.fab_close_anim);
        animFromBottom = AnimationUtils.loadAnimation(this, R.anim.fab_from_bottom_anim);
        animToBottom = AnimationUtils.loadAnimation(this, R.anim.fab_to_bottom_anim);
        user = helper_sp.getUser();
        isNotify = helper_sp.getIsNotify();
        if (user != null && isNotify) {
            mSocket = Socket_Client.getSocket();
            mSocket.on("send-notify:" + user.getId(), onNotification);
            startJob();
        }
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
        setBadgeNotify();
        initFragment();
    }
    private void initFragment() {
        String type = getIntent().getStringExtra("type");
        String id = getIntent().getStringExtra("id");
        Log.d("TAG", "initFragment: " + type);
        if (type != null) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            if (type.equalsIgnoreCase("post")) {
                fragment = new Fragment_Post_Details();
                bundle.putString("idPost", id);
                fragment.setArguments(bundle);
            } else if (type.equalsIgnoreCase("blog")) {
                fragment = new Fragment_Blog_Detail();
                bundle.putString("idBlog", id);
                fragment.setArguments(bundle);
            } else {
                fragment = new Fragment_Notification();
            }
            if (user != null) {
                helper_common.replaceFragment(this, fragment);
            }
        } else {
            replaceFragment(new Fragment_Tab_UserInfo());

        }
    }

    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                badgeNotify.setNumber(badgeNotify.getNumber() + 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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
        MenuItem itemNotify = menu.findItem(R.id.menu_home_switchNotify);
        MenuItem itemLogout = menu.findItem(R.id.menu_home_logout);
        if (helper_sp.getUser() == null) {
            itemLogout.setTitle("Đăng nhập");
        }
        if (helper_sp.getUser() == null) {
            itemNotify.setEnabled(false);
        }
        if (helper_sp.getIsNotify()) {
            itemNotify.setIcon(R.drawable.ic_toggle_on);
            itemNotify.setTitle("Tắt thông báo");
        } else {
            itemNotify.setIcon(R.drawable.ic_toggle_off);
            itemNotify.setTitle("Nhận thông báo");
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_home_main:
                replaceFragment(new Fragment_Category());
                break;
            case R.id.menu_home_logout: {
                spotsDialog.show();
                dao_auth.signOut(new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {
                        spotsDialog.dismiss();
                        stopJob();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    }
                    @Override
                    public void failedReq(String msg) {
                        spotsDialog.dismiss();
                    }
                });
                break;
            }
            case R.id.menu_home_switchNotify:
                toggleNotify(item);
                break;
            case R.id.menu_home_exit_app:
                helper_common.dialogExitApp(this);
                break;
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1){
            replaceFragment(new Fragment_Category());
        }else {
            super.onBackPressed();

        }
    }

    private void toggleNotify(MenuItem item) {
        if (helper_sp.getUser() == null) {
            item.setEnabled(false);
        }
        if (helper_sp.getIsNotify()) {
            item.setIcon(R.drawable.ic_toggle_off);
            item.setTitle("Nhận thông báo");
            stopJob();
        } else {
            item.setIcon(R.drawable.ic_toggle_on);
            item.setTitle("Tắt thông báo");
            startJob();
        }
        helper_sp.setIsNotify(!helper_sp.getIsNotify());
    }

    private void toggleAnim() {
        if (!clicked) {
            layoutAddPost.setVisibility(View.VISIBLE);
            layoutAddBlog.setVisibility(View.VISIBLE);
            layoutAddPost.startAnimation(animFromBottom);
            layoutAddBlog.startAnimation(animFromBottom);
            fabAdd.startAnimation(animOpen);
            layoutAddPost.setClickable(true);
            layoutAddBlog.setClickable(true);
        } else {
            layoutAddPost.setVisibility(View.INVISIBLE);
            layoutAddBlog.setVisibility(View.INVISIBLE);
            layoutAddPost.startAnimation(animToBottom);
            layoutAddBlog.startAnimation(animToBottom);
            fabAdd.startAnimation(animClose);
            layoutAddPost.setClickable(false);
            layoutAddBlog.setClickable(false);
        }
        clicked = !clicked;

    }

    public void setBadgeNotify() {
        if (user != null) {
            dao_notification.getAll(helper_sp.getAccessToken(), new Helper_Callback() {
                @Override
                public void successReq(Object response) {
                    List<Notification> notificationList = (List<Notification>) response;
                    int numberBadge = (int) notificationList.stream().filter(item -> item.getStatus().equals("new")).count();
                    badgeNotify.setNumber(numberBadge);
                }

                @Override
                public void failedReq(String msg) {

                }
            });
        } else {
            badgeNotify.setNumber(0);
        }
    }

    private void startJob() {
        ComponentName componentName = new ComponentName(this, Notification_Job_Service.class);
        JobInfo jobInfo = new JobInfo.Builder(101, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(15 * 60 * 1000)
                .setRequiresCharging(false)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);

    }

    private void stopJob() {
        jobScheduler.cancel(101);
    }
}