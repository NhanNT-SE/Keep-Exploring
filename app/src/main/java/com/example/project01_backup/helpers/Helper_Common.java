package com.example.project01_backup.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Helper_Common {
    private Context context;

    public Helper_Common(Context context) {
        this.context = context;
    }

    private void log(String s) {
        Log.d("log", s);
    }
    private void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
