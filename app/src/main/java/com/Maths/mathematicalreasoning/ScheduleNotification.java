package com.Maths.mathematicalreasoning;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class ScheduleNotification extends Service {

    private final static String TAG = "CheckRecentPlay";
    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;

    //  private static long delay = MILLISECS_PER_MIN * 3;   // 3 minutes (for testing)
    private static long delay = MILLISECS_PER_DAY * 3;   // 3 days

    @Override
    public void onCreate() {
        super.onCreate();
        setAlarm();
        NotifyMe();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotifyMe();
        Toast.makeText(getApplicationContext(),"AlermSet",Toast.LENGTH_LONG).show();

        return Service.START_STICKY;
    }

    public void setAlarm() {

        Intent serviceIntent = new Intent(this, ScheduleNotification.class);
        PendingIntent pi = PendingIntent.getService(this, 131313, serviceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*60, pi);

    }

    public void sendNotification() {

        Intent mainIntent = new Intent(this, DashBoard.class);

        Notification noti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("We Miss You!")
                .setContentText("Please play our game again soon.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logo)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .getNotification();


        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

    }

    public void NotifyMe(){
        Intent mainIntent = new Intent(this, DashBoard.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123456")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setTicker("We Miss You! Please come back and play our game again soon.")
                 .setLargeIcon(BitmapFactory. decodeResource (getResources() , R.drawable. logo ))
                .setContentTitle("My notification")
                .setTicker("We Miss You! Please come back and play our game again soon.")
                .setContentText("Much longer text that cannot fit one line...prins")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line...prins"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.google_app_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123456", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(12, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        setAlarm();
        super.onTaskRemoved(rootIntent);
    }
}

/*        Log.v(TAG, "Service started");
        SharedPreferences settings = getSharedPreferences("MathsResoninngData", MODE_PRIVATE);

//        // Are notifications enabled?
//        if (settings.getBoolean("enabled", true)) {
//            // Is it time for a notification?
//            if (settings.getLong("lastRun", Long.MAX_VALUE) < System.currentTimeMillis() - delay)
//                sendNotification();
//                NotifyMe();
//
//        } else {
//            Log.i(TAG, "Notifications are disabled");
//        }

        // Set an alarm for the next time this service should run:*/