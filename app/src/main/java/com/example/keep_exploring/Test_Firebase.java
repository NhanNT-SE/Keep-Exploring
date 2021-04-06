package com.example.keep_exploring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Test_Firebase extends AppCompatActivity {
    private ImageView img;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase);

        img = (ImageView) findViewById(R.id.aTest_img);
        btn = (Button) findViewById(R.id.aTest_btnUpload);

    }
}