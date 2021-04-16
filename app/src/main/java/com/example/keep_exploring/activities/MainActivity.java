package com.example.keep_exploring.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.keep_exploring.DAO.DAO_Address;
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
import com.example.keep_exploring.model.Places;
import com.example.keep_exploring.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser currentUser;
    private String password;
    private String email;
    private TextView number;
    private Helper_SP helper_sp;
    private BottomNavigationView bottomNavigationView;
    private Helper_Common helper_common;
    private DAO_Address dao_address;
    private CircleMenuView circleMenuView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initVariable();
        handlerEvent();
    }
    private void initView() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottomNavigation);
        circleMenuView = findViewById(R.id.main_circleMenu);
        fab = (FloatingActionButton) findViewById(R.id.main_fabAdd);
    }
    private void initVariable() {
        helper_common = new Helper_Common();
        helper_sp = new Helper_SP(this);
        dao_address = new DAO_Address(this);
    }
    private void handlerEvent() {
        helper_common.runtimePermission(this);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        if (currentUser != null) {
//            Toast.makeText(this, "Hello " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
        dao_address.getProvinceList(new Helper_Callback() {
            @Override
            public List<User> successReq(Object response) {
                List<String> provinceList = (List<String>) response;
                return null;
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
                showCircleMenu();
            }
        });
        hideCircleMenu();
        replaceFragment(new Fragment_Category());
    }

    private void insertPlaces(){
        String[] placeList = {"An Ging", "Vung Tau", "Bac Giang", "Bac Kan","Bac Lieu","Bac Ninh",
        "Ben Tre", "Binh Dinh", "Binh Duong", "Binh Phuoc", "Binh Thuan", "Ca Mau", "Cao Bang",
        "Dak Lak", "Dak Nong", "Dien Bien", "Dong Nai", "Dong Thap", "Gia Lai", "Ha Giang","Ha Nam",
        "Ha Tinh", "Hai Duong", "Hau Gian","Hoa Binh", "Hung Yen","Khanh Hoa","Kien Giang", "Kon Tum",
        "Lai Chau", "Lam Dong", "Lang Son","Lao Cai","Long An", "Nam Dinh","Nghe An", "Ninh Binh",
                "Ninh Thuan", "Phu Tho", "Quang Binh", "Quang Nam", "Quang Ngai", "Quang Ninh", "Quang Tri",
                "Soc Trang", "Son La", "Tay Ninh", "Thai Binh", "Thai Binh", "Thai Nguyen", "Thanh Hoa", "Thua Thien Hue",
                "Tien Giang", "Tra Vinh", "Tuyen Quang", "Vinh Long", "Vinh Phuc", "Yen Bai", "Phu Yen", "Ha Noi",
                "TP HCM", "Da Nang", "Can Tho", "Hai Phong", "Da Lat", "Phu Quoc", "Nha Trang"};
        for (int i = 0; i < placeList.length; i++) {
            Places places = new Places();
            places.setName(placeList[i]);
        }
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
                replaceFragment(new Fragment_Notification());
                break;
            case R.id.menu_bottom_profile:
                replaceFragment(new Fragment_Tab_UserInfo());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return (true);
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
