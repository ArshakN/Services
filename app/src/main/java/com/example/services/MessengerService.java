package com.example.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;


public class MessengerService extends Service {
    public static final String CHANNEL_ID="BoundServiceChannel";
    NotificationManager notificationManager;
    ArrayList<Messenger> clients = new ArrayList<>();
    int value=0;

    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_SET_VALUE = 3;


    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    clients.add(msg.replyTo);
                    break;

                case MSG_UNREGISTER_CLIENT:
                    clients.remove(msg.replyTo);
                    break;

                case MSG_SET_VALUE:
                    value = msg.arg1;
                    for (int i = clients.size() - 1; i >= 0; i--) {
                        try {
                            clients.get(i).send(Message.obtain(null, MSG_SET_VALUE, value, 0));
                        } catch (RemoteException e) {
                            clients.remove(i);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);

            }
        }

    }


    final Messenger messenger =new Messenger(new IncomingHandler());


    @Override
    public void onCreate() {

        createNotificationChannel();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Bound Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(serviceChannel);
        }

        showNotification();

    }


    @Override
    public void onDestroy() {
        notificationManager.cancel(1);

        Toast.makeText(this,"remote service stopped",Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private void showNotification(){
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);

        NotificationCompat.Builder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_android_black)
                .setTicker("Remote service STARTED")
                .setWhen(System.currentTimeMillis())
                .setContentText("Remote service STARTED")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_android_black)
                .setTicker("Remote service STARTED")
                .setWhen(System.currentTimeMillis())
                .setContentText("Remote service STARTED")
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(1,notification);

    }


}
