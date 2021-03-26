package com.example.project01_backup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project01_backup.R;
import com.example.project01_backup.fragment.Fragment_Admin;

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
