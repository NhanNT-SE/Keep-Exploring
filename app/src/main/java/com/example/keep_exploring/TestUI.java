package com.example.keep_exploring;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TestUI extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab, fabPost, fabBlog;
    private Animation animOpen, animClose, animFromBottom, animToBottom;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ui);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.testUI_bottomNavigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        animOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open_anim);
        animClose = AnimationUtils.loadAnimation(this, R.anim.fab_close_anim);
        animFromBottom = AnimationUtils.loadAnimation(this, R.anim.fab_from_bottom_anim);
        animToBottom = AnimationUtils.loadAnimation(this, R.anim.fab_to_bottom_anim);
        fab = (FloatingActionButton) findViewById(R.id.testUI_fabAdd);
        fabPost = (FloatingActionButton) findViewById(R.id.testUI_fabPost);
        fabBlog = (FloatingActionButton) findViewById(R.id.testUI_fabBlog);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
            }
        });

        fabBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
                Toast.makeText(TestUI.this, "Add blog", Toast.LENGTH_SHORT).show();
            }
        });

        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
                Toast.makeText(TestUI.this, "Add post", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void onAddClick() {
        setVisibility(clicked);
        setClickable(clicked);
        setAnimation(clicked);
        clicked = !clicked;

    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            fabPost.setVisibility(View.VISIBLE);
            fabBlog.setVisibility(View.VISIBLE);
        } else {
            fabPost.setVisibility(View.GONE);
            fabBlog.setVisibility(View.GONE);

        }

    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            fabPost.startAnimation(animFromBottom);
            fabBlog.startAnimation(animFromBottom);
            fab.startAnimation(animOpen);

        } else {
            fabPost.startAnimation(animToBottom);
            fabBlog.startAnimation(animToBottom);
            fab.startAnimation(animClose);
        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            fabPost.setClickable(true);
            fabBlog.setClickable(true);
        } else {
            fabPost.setClickable(false);
            fabBlog.setClickable(false);
        }
    }
}