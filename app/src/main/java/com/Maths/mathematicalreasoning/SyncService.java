package com.Maths.mathematicalreasoning;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SyncService extends Service {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //Game data
        sp=getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();
        if (sp.contains("LastPeriodicSynce"))
        {
            if(System.currentTimeMillis()-sp.getLong("LastPeriodicSynce",0)>=86400000*7)
            {
                SyncData();
            }
        }
        else{
            SyncData();
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void NotifyMe(Context context){
        Intent mainIntent = new Intent(context, DashBoard.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "123456")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(PendingIntent.getActivity(context, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...prins")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line...prins"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("123456", "prins SIngh", importance);
            channel.setDescription("DIsacription For RThe app");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123451, builder.build());
    }


    public void SyncData(){
        NotifyMe(getApplicationContext());
        DatabaseReference mdbRef;

        mdbRef = FirebaseDatabase.getInstance().getReference();
        String Name=sp.getString("User_Name","NoName");
        String Email =sp.getString("User_Email","Noemail@gmail.com");
        String key =sp.getString("User_UID","no");
        int Level =sp.getInt("CompletedLevels",0);

        UserData object =new UserData(Name,Email,Level);
        Map<String,Object> map1 = object.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/Users/" + key, map1);


        mdbRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            notificationManager.cancelAll();
            }
        });


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}