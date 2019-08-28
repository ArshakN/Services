package com.example.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import static com.example.services.MainActivity.TAG;
import static java.lang.Thread.sleep;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("IntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        for (int i=0;i<10;i++) {
            Log.d(TAG,"Intent Service started " + i);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
