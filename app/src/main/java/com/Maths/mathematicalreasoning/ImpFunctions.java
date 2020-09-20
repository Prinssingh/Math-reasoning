package com.Maths.mathematicalreasoning;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class ImpFunctions{
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final MediaPlayer onclick,right,wrong;

    @SuppressLint("CommitPrefEdits")
    public ImpFunctions(Context context){
        this.context=context;
        sp=context.getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();
        onclick= MediaPlayer.create(context,R.raw.numberclick);
        right =MediaPlayer.create(context,R.raw.numberclick);
        wrong =MediaPlayer.create(context,R.raw.numberclick);
    }

    public void ShowToast(LayoutInflater layoutInflater, String Title, String Message) {
        @SuppressLint("InflateParams")
        View ToastView= layoutInflater.inflate(R.layout.toast_view,null);
        TextView title =ToastView.findViewById(R.id.ToastTitle);
        TextView message = ToastView.findViewById(R.id.ToastMsg);
        title.setText(Title);
        message.setText(Message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(ToastView);
        toast.show();
    }

    public Boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        String status = null;
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
            }
        }else{
            status = "No internet is available";
        }

        return isConnected;
    }

    public void SyncData(){
        DatabaseReference mdbRef;

        final ProgressDialog progressDialog =new ProgressDialog(context);
        progressDialog.setMessage("Saving Your Progress.. !!");
        progressDialog.show();

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
                progressDialog.hide();

            }
        });


    }

    public void NotifyMe(){
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

    public void OnclickSound() {

        if (sp.getBoolean("Sound",true))
        {
            onclick.start();
        }

    }

    public String getVersionName(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null){
            return String.valueOf(packageInfo.versionName);
        }else{
            return "1.0.1";}

    }

    public int getTotalLevels(){
        return 100;
    }





}
