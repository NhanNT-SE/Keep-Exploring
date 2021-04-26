package com.example.keep_exploring.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.keep_exploring.R;
import com.example.keep_exploring.activities.MainActivity;
import com.example.keep_exploring.api.Socket_Client;
import com.example.keep_exploring.helpers.Helper_SP;
import com.example.keep_exploring.model.User;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Notification_Job_Service extends JobService {
    private Socket mSocket;
    private int ID_NOTIFY;
    private Intent resultIntent;
    private Helper_SP helper_sp;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("log", "onStartJob");
        doBackgroundWork();
        return true;
    }

    private void doBackgroundWork() {
        helper_sp = new Helper_SP(this);
        User user = helper_sp.getUser();

        resultIntent = new Intent(this, MainActivity.class);
        NotificationChannel channel = new NotificationChannel("Notification", "Notification",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        channel.setShowBadge(true);
//        channel.setSound(null, null);
        assert manager != null;
        manager.createNotificationChannel(channel);
        mSocket = Socket_Client.getSocket();
        mSocket.on("send-notify:" + user.getId(), onNotification);
        mSocket.connect();
    }

    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            String id = "";
            String type = "";
            try {
                String message = data.getString("message");
                String typeNotify = data.getString("type");
                if (data.has("idPost")) {
                    id = data.getString("idPost");
                    type = "post";
                    resultIntent.putExtra("type", type);
                    resultIntent.putExtra("id", id);
                } else if (data.has("idBlog")) {
                    id = data.getString("idBlog");
                    type = "blog";
                    resultIntent.putExtra("type", type);
                    resultIntent.putExtra("id", id);
                } else {
                    type = "system";
                    resultIntent.putExtra("type", type);
                }
                pushNotify(typeNotify, message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void pushNotify(String type, String message) {
        RemoteViews notificationLayout =
                new RemoteViews(getPackageName(), R.layout.custom_notification);
        setIconNotify(notificationLayout, type, message);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, "Notification")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSmallIcon(R.drawable.ic_notifications_black)
                .setCustomContentView(notificationLayout)
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(ID_NOTIFY, builder.build());
    }

    private void setIconNotify(RemoteViews remoteViews, String type, String message) {
        int resource = -1;
        switch (type) {
            case "like":
                ID_NOTIFY = 1;
                resource = R.drawable.ic_like_red;
                break;
            case "comment":
                ID_NOTIFY = 2;
                resource = R.drawable.ic_custom_notify_comment;
                break;
            case "done":
                ID_NOTIFY = 3;
                resource = R.drawable.ic_custom_notify_done;
                break;
            case "pending":
                ID_NOTIFY = 4;
                resource = R.drawable.ic_custom_notify_pending;
                break;
            case "need_update":
                ID_NOTIFY = 5;
                resource = R.drawable.ic_custom_notify_need_update;
                break;
            case "system":
                ID_NOTIFY = 6;
                resource = R.drawable.ic_custom_notify_system;
                break;
        }
        remoteViews.setImageViewResource(R.id.customNotify_image, resource);
        remoteViews.setTextViewText(R.id.customNotify_tvDesc, message);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("log", "onStopJob");
        mSocket.disconnect();
        return false;
    }

}
