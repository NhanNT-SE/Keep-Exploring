package com.example.keep_exploring.helpers;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class Helper_Date {
    public String getIsoDate() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
        return sdf.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public String convertStringToIsoDate(String stringDate) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'").format(date);
        } catch (ParseException e) {
            Log.d("", "convertStringToIsoDate: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public long getMillisTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public String formatDateDisplay(String stringDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat;
        if (stringDate.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            dateFormat = format.format(calendar.getTime());
        } else {
            Date date = Date.from(Instant.parse(stringDate));
            dateFormat = format.format(date);
        }
        return dateFormat;
    }
}
