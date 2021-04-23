package com.example.keep_exploring;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.keep_exploring.DAO.DAO_Auth;
import com.example.keep_exploring.DAO.DAO_Comment;
import com.example.keep_exploring.DAO.DAO_Like;
import com.example.keep_exploring.DAO.DAO_Post;
import com.example.keep_exploring.activities.MainActivity;
import com.example.keep_exploring.api.Api_Test;
import com.example.keep_exploring.api.Retrofit_config;
import com.example.keep_exploring.api.Socket_Client;
import com.example.keep_exploring.helpers.Helper_Callback;
import com.example.keep_exploring.helpers.Helper_Common;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;
import com.example.keep_exploring.services.Notification_Service;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TestUI extends AppCompatActivity {
    private Button btnLikePost, btnCommentPost, btnSystem;
    private DAO_Auth dao_auth;
    private DAO_Post dao_post;
    private DAO_Like dao_like;
    private DAO_Comment dao_comment;
    private Helper_SP helper_sp;
    private User user;
    private String accessToken;
    private Helper_Common helper_common;
    private Socket mSocket;
    private Api_Test api_test;
    private int ID_NOTIFY;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ui);
        dao_auth = new DAO_Auth(this);
        dao_auth = new DAO_Auth(this);
        helper_sp = new Helper_SP(this);
        dao_post = new DAO_Post(this);
        dao_like = new DAO_Like(this);
        dao_comment = new DAO_Comment(this);
        btnLikePost = (Button) findViewById(R.id.testUI_btnLikePost);
        btnCommentPost = (Button) findViewById(R.id.testUI_btnCommentPost);
        btnSystem = (Button) findViewById(R.id.testUI_btnSystem);
        accessToken = helper_sp.getAccessToken();
        helper_common = new Helper_Common();
        api_test = Retrofit_config.retrofit.create(Api_Test.class);
        user = helper_sp.getUser();
        Intent serviceIntent = new Intent(this, Notification_Service.class);
        startService(serviceIntent);

        NotificationChannel channel = new NotificationChannel("Notification", "Notification",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        channel.setShowBadge(true);
        channel.setSound(null,null);
        manager.createNotificationChannel(channel);


        btnLikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost();
            }
        });

        btnCommentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });

        btnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushNotify("system", "Hello");


            }
        });
    }


    private void likePost() {
        dao_like.setLike(helper_sp.getAccessToken(), "60691b64b450484668ac470c", "post", new Helper_Callback() {
            @Override
            public void successReq(Object response) {

            }

            @Override
            public void failedReq(String msg) {

            }
        });
    }

    private void comment() {
        dao_comment.addComment(helper_sp.getAccessToken(),
                "6073075019c68e0b99291ffe",
                "blog",
                "content test", "", new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {

                    }

                    @Override
                    public void failedReq(String msg) {

                    }
                });
    }

    private void pushNotify(String type, String message) {
        RemoteViews notificationLayout =
                new RemoteViews(getPackageName(), R.layout.custom_notification);
        setIconNotify(notificationLayout, type, message);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, "Notification")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notifications_black)
                .setCustomContentView(notificationLayout);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(ID_NOTIFY, builder.build());
    }

    private void setIconNotify(RemoteViews remoteViews, String type, String message) {
        int resource = -1;
        switch (type) {
            case "like":
                ID_NOTIFY = 1;
                resource = R.drawable.ic_custom_notify_like;
                break;
            case "comment":
                ID_NOTIFY = 2;
                resource = R.drawable.ic_custom_notify_comment;
                break;
            case "pending":
                ID_NOTIFY = 3;
                resource = R.drawable.ic_custom_notify_pending;
                break;
            case "need_update":
                ID_NOTIFY = 4;
                resource = R.drawable.ic_custom_notify_need_update;
                break;
            case "system":
                ID_NOTIFY = 5;
                resource = R.drawable.ic_custom_notify_system;
                break;
        }
        remoteViews.setImageViewResource(R.id.customNotify_image, resource);
        remoteViews.setTextViewText(R.id.customNotify_tvDesc, message);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String msg) {
        Log.d("log", "Test UI: " + msg);
    }


}