package com.example.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static com.example.services.MainActivity.TAG;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID="ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChanel();
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification=new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        Log.d(TAG,"Foreground Service start");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChanel(){
             if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
               NotificationManager manager = getSystemService(NotificationManager.class);
               manager.createNotificationChannel(serviceChannel);
            }

        }
}

