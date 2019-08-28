package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.Nullable;

import static com.example.services.MainActivity.TAG;
import static java.lang.Thread.sleep;

public class MyService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        for (int i=0;i<10;i++) {
            Log.d(TAG,"Service started " + i);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
