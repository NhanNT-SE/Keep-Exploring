package com.example.keep_exploring.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keep_exploring.DAO.DAO_Address;
import com.example.keep_exploring.R;

import com.example.keep_exploring.fragment.Fragment_AddPost;
import com.example.keep_exploring.fragment.Fragment_EditBlog;
import com.example.keep_exploring.fragment.Fragment_Notification;
import com.example.keep_exploring.fragment.Fragment_TabCategory;
import com.example.keep_exploring.fragment.Fragment_Tab_UserInfo;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    //    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FirebaseUser currentUser;
    private String password;
    private String email;
    private TextView number;
    private Helper_SP helper_sp;
    private DAO_Address dao_address;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        helper_sp = new Helper_SP(this);
        dao_address = new DAO_Address(this);
        Intent getPass = getIntent();
        if (getPass != null) {
            password = getPass.getStringExtra("pass");
            email = getPass.getStringExtra("email");
        }
        dao_address.getProvinceList(new Helper_Callback() {
            @Override
            public void successReq(Object response) {
                List<String> provinceList = (List<String>) response;
            }

            @Override
            public void failedReq(String msg) {

            }
        });
        initView();

    }

    private void initView() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
//            Toast.makeText(this, "Hello " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

//        toggle.syncState();
        replaceFragment(new Fragment_TabCategory());
//        showInfo();

    }


    private void insertPlaces() {
        String[] placeList = {"An Ging", "Vung Tau", "Bac Giang", "Bac Kan", "Bac Lieu", "Bac Ninh",
                "Ben Tre", "Binh Dinh", "Binh Duong", "Binh Phuoc", "Binh Thuan", "Ca Mau", "Cao Bang",
                "Dak Lak", "Dak Nong", "Dien Bien", "Dong Nai", "Dong Thap", "Gia Lai", "Ha Giang", "Ha Nam",
                "Ha Tinh", "Hai Duong", "Hau Gian", "Hoa Binh", "Hung Yen", "Khanh Hoa", "Kien Giang", "Kon Tum",
                "Lai Chau", "Lam Dong", "Lang Son", "Lao Cai", "Long An", "Nam Dinh", "Nghe An", "Ninh Binh",
                "Ninh Thuan", "Phu Tho", "Quang Binh", "Quang Nam", "Quang Ngai", "Quang Ninh", "Quang Tri",
                "Soc Trang", "Son La", "Tay Ninh", "Thai Binh", "Thai Binh", "Thai Nguyen", "Thanh Hoa", "Thua Thien Hue",
                "Tien Giang", "Tra Vinh", "Tuyen Quang", "Vinh Long", "Vinh Phuc", "Yen Bai", "Phu Yen", "Ha Noi",
                "TP HCM", "Da Nang", "Can Tho", "Hai Phong", "Da Lat", "Phu Quoc", "Nha Trang"};
        for (int i = 0; i < placeList.length; i++) {
            Places places = new Places();
            places.setName(placeList[i]);
        }
    }

    private void runtimePermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, "All granted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private String getToken(String key) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("storage_token", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home
        ) {
            getSupportFragmentManager().popBackStack();
//            getFragmentManager().popBackStackImmediate();

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        intentFragment(new Fragment_TabCategory());
        switch (item.getItemId()) {
            case R.id.menu_home:
                intentFragment(new Fragment_TabCategory());
                break;
//                    case R.id.menu_search:
//                        intentFragment(new Fragment_Search());
            case R.id.menu_addPost:
                intentFragment(new Fragment_AddPost());
                break;
            case R.id.menu_notification:
                intentFragment(new Fragment_Notification());
                break;
            case R.id.menu_profile:
                intentFragment(new Fragment_Tab_UserInfo());
                break;

        }
        return true;
    }

    public void intentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_FrameLayout, fragment)
                .addToBackStack(null).commit();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_FrameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }


}
