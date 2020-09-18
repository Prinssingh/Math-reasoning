package com.Maths.mathematicalreasoning;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

public class SleepingNotify extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        intent = new Intent(context, ScheduleNotification.class);
        context.startService(intent);

        ComponentName comp = new ComponentName(context.getPackageName(),
                ScheduleNotification.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
    public void NotifyMe(Context context){
        Intent mainIntent = new Intent(context, DashBoard.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "123456")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(PendingIntent.getActivity(context, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setTicker("We Miss You! Please come back and play our game again soon.")
                .setContentTitle("My notification")
                .setTicker("We Miss You! Please come back and play our game again soon.")
                .setContentText("Much longer text that cannot fit one line...prins")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line...prins"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123456", "prins SIngh", importance);
            channel.setDescription("DIsacription For RThe app");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(12, builder.build());
    }
}
