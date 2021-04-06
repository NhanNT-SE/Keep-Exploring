package com.example.keep_exploring.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.keep_exploring.R;
import com.example.keep_exploring.fragment.Fragment_Admin;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_FrameLayout, new Fragment_Admin())
                .commit();
    }

}
