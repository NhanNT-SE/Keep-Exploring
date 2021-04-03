package com.example.project01_backup.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Helper_Common {

    public String getBaseUrlImage() {
        String URL_LOCAL = "http://10.0.2.2:3000/images/";
        String URL_GLOBAL = "http://ec2-18-223-15-195.us-east-2.compute.amazonaws.com:3000/images/";
        return URL_GLOBAL;
    }

    private void log(String s) {
        Log.d("log", s);
    }
//    private void toast(String s) {
//        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
//    }
}
