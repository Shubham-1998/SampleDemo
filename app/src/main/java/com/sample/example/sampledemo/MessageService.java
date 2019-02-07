package com.sample.example.sampledemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URI;
import java.util.Objects;

public class MessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        PendingIntent intent=PendingIntent.getActivity(getApplicationContext(),0,
                new Intent(getApplicationContext(),MainActivity.class),0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel_id");
        builder.setContentIntent(intent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        builder.setSound(notificationSound);
        builder.setVibrate(new long[] {100,100});
        builder.setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel ("channel_id", "app_name",NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
        if (manager != null) {
            manager.notify(0,builder.build());
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
