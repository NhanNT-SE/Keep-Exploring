package com.example.keep_exploring;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.circlemenu.CircleMenuView;

public class TestUI extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private CircleMenuView circleMenuView;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ui);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.testUI_bottomNavigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        circleMenuView = findViewById(R.id.testUI_cMenu);
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
                        Toast.makeText(TestUI.this, "Food", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        view.setVisibility(View.GONE);
                        Toast.makeText(TestUI.this, "Hotel", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        view.setVisibility(View.GONE);
                        Toast.makeText(TestUI.this, "Check in", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                super.onMenuCloseAnimationStart(view);
                view.setVisibility(View.GONE);
            }
        });
        hideCircleMenu();
         fab = (FloatingActionButton) findViewById(R.id.testUI_fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideCircleMenu();
//                popupMenu();
                showCircleMenu();
            }
        });

    }

    private void popupMenu(){
        PopupMenu popup = new PopupMenu(TestUI.this, fab);
        popup.getMenuInflater().inflate(R.menu.menu_popup_fab, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_popup_fab_post){
                    Toast.makeText(TestUI.this, "Add Post", Toast.LENGTH_SHORT).show();
                }if (item.getItemId() == R.id.menu_popup_fab_blog){
                    Toast.makeText(TestUI.this, "Add Blog", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

        popup.show();

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_bottom_home:
                hideCircleMenu();
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_bottom_post:
                hideCircleMenu();
                break;
            case R.id.menu_bottom_blog:
                hideCircleMenu();
                Toast.makeText(this, "Blog", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_bottom_notify:
                hideCircleMenu();
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void showCircleMenu() {
        circleMenuView.setVisibility(View.VISIBLE);
        circleMenuView.open(true);
    }
    private void hideCircleMenu(){
        circleMenuView.close(false);
        circleMenuView.setVisibility(View.INVISIBLE);
    }
}