package com.lawlett.notification1_0;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import static com.lawlett.notification1_0.MainActivity.CHANNEL_ID;
import static java.util.concurrent.TimeUnit.HOURS;


public class ForegroundService extends Service {
    private static final int NOTIFY_ID = 101;
    Random random;

    public static final long THREE_HOUR = 1000*60*180;

    private NotificationManager notificationManager;
    List<String> messages = new ArrayList<>();
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private Timer timer;
    final int REFRESH = 0;
    Context context;

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        messages.add("1");
        messages.add("2");
        messages.add("3");
        messages.add("4");
        messages.add("5");
        messages.add("6");
        messages.add("7");
        messages.add("9");
        messages.add("10");


        context = this;
        random = new Random();

        TimerTask refresher;

        timer = new Timer();
        refresher = new TimerTask() {
            public void run() {
                           handler.sendEmptyMessage(0);
            }
        };
        timer.scheduleAtFixedRate(refresher, 0, THREE_HOUR);

    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    scheduler.scheduleWithFixedDelay(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            for (int i = 0; i < random.nextInt(messages.size()); i++) {
                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle("Напоминание")
                                                .setDefaults(Notification.DEFAULT_SOUND)
                                                .setAutoCancel(true)
                                                .setOnlyAlertOnce(true).setStyle(new NotificationCompat
                                                .BigTextStyle().bigText(messages.get(i)));

                                notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.notify(NOTIFY_ID, builder.build());
                            }
                        }
                    }, 0, 5, HOURS);

                    break;
                                 default:
                                 break;
            }

        }
    };
}