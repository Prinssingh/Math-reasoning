package com.Math.MathReasoning;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class ImpFunctions {
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    MediaPlayer onclick, right, wrong;

    @SuppressLint("CommitPrefEdits")
    public ImpFunctions(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor = sp.edit();
        try {
            onclick = MediaPlayer.create(context, R.raw.clicks);
            right = MediaPlayer.create(context, R.raw.correct);
            wrong = MediaPlayer.create(context, R.raw.error);
        } catch (Exception e) {
            Log.e("TAG", "ImpFunctions: " + e);
        }

    }

    public void ShowToast(LayoutInflater layoutInflater, String Title, String Message) {
        @SuppressLint("InflateParams")
        View ToastView = layoutInflater.inflate(R.layout.toast_view, null);
        TextView title = ToastView.findViewById(R.id.ToastTitle);
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
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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
        } else {
            status = "No internet is available";
        }

        return isConnected;
    }

    public void SyncData() {
        DatabaseReference mdbRef;

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Saving Your Progress.. !!");
        progressDialog.show();

        mdbRef = FirebaseDatabase.getInstance().getReference();
        String Name = sp.getString("User_Name", "");
        String Email = sp.getString("User_Email", "");
        String key = sp.getString("User_UID", "");

        try {
            if (Name.isEmpty()) {
                progressDialog.hide();
                return;
            }
        } catch (Exception | Error ignored) {
        }

        int Level = sp.getInt("CompletedLevels", 0);
        int Hint = sp.getInt("Hint", 0);
        int Solution = sp.getInt("Solution", 0);
        long DT = sp.getLong("DT", System.currentTimeMillis());

        UserData object = new UserData(Name, Email, Level, Hint, Solution, DT);
        Map<String, Object> map1 = object.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/Users/" + key, map1);


        mdbRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.hide();

            }
        });


    }

    public String getVersionName() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return String.valueOf(packageInfo.versionName);
        } else {
            return "1.0.1";
        }

    }

    public int getTotalLevels() {
        return 100;
    }


    public void OnclickSound() {
        try {
            if (sp.getBoolean("Sound", true)) {
                onclick.start();
            }
        } catch (Exception e) {
            Log.e("TAG", "OnclickSound: " + e);
        }
    }

    public void correctSound() {
        try {
            if (sp.getBoolean("Sound", true)) {
                right.start();
            }
        } catch (Exception e) {
            Log.e("TAG", "OnclickSound: " + e);
        }

    }

    public void wrongSound() {
        try {
            if (sp.getBoolean("Sound", true)) {
                wrong.start();
            }
        } catch (Exception e) {
            Log.e("TAG", "OnclickSound: " + e);
        }
    }

}
