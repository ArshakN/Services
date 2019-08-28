package com.example.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Service";
    private Button btn_service;
    private Button btn_intentService;
    private Button btn_foregroundService;
    private Button btn_boundService;
    private Button btn_unboundService;


    private TextView textView;


    Messenger service=null;
    boolean isBound;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    textView.setText("Recived from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger messenger = new Messenger(new IncomingHandler());

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = new Messenger(iBinder);
            textView.setText("Attached.");

            try {
                Message msg = Message.obtain(null, MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = messenger;
                service.send(msg);

                msg = Message.obtain(null, MessengerService.MSG_SET_VALUE, this.hashCode(), 0);
                service.send(msg);

            } catch (RemoteException e) {
            }

            Toast.makeText(MainActivity.this, "Remote service connected", Toast.LENGTH_SHORT).show();


        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
            textView.setText("Disconected");
            Toast.makeText(MainActivity.this, "Remote service disconnected", Toast.LENGTH_SHORT).show();


        }
    };

    void doBindService() {
        bindService(new Intent(MainActivity.this,MessengerService.class),connection,Context.BIND_AUTO_CREATE);
        isBound=true;
        textView.setText("Binding");
    }

    void doUnbindService() {
        if (isBound) {
            if (service != null) {
                try {
                    Message msg = Message.obtain(null, MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = messenger;
                    service.send(msg);
                } catch (RemoteException e) {
                }
            }
            unbindService(connection);
            isBound = false;
            textView.setText("Unbinding");

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_service=findViewById(R.id.id_service);
        btn_intentService=findViewById(R.id.id_intent_service);
        btn_foregroundService=findViewById(R.id.id_foreground);
        btn_boundService=findViewById(R.id.id_bound);
        btn_unboundService=findViewById(R.id.id_unbound);

        textView=findViewById(R.id.id_textView);

        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(MainActivity.this,MyService.class);
               startService(intent);

                textView.setText(" Service ");


            }
        });

        btn_intentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MyIntentService.class);
                startService(intent);
                textView.setText("Intent Service ");

            }
        });


        btn_foregroundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForegroundService.class);
                intent.putExtra("inputExtra","Foreground Service");

                ContextCompat.startForegroundService(MainActivity.this,intent);

            }
        });


        btn_boundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBindService();
            }
        });

        btn_unboundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUnbindService();
            }
        });
    }

}
