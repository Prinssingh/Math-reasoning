package com.Maths.mathematicalreasoning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SplashScreen extends AppCompatActivity {


    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImpFunctions impFun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        //Shared Data /GameData
        sp=getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        //editor.clear().commit();

        //Set Version name
        impFun = new ImpFunctions(getApplicationContext());
        TextView versionName =findViewById(R.id.versionname);

        String vers="Version " + impFun.getVersionName();
        versionName.setText(vers);

        //Splash Screen timer

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if(!sp.getBoolean("Login",false)){
                   Intent LoginIntent = new Intent(SplashScreen.this, CustomLogin.class);
                   startActivity(LoginIntent);
                   finish();
               }
               else{
                   Intent homeintent = new Intent(SplashScreen.this, DashBoard.class);
                   startActivity(homeintent);
                   finish();
               }

            }
        }, 4000);


        //Save Progress if 7 days Complete!!
        if (isAutoSyncAVL() &&sp.getString("User_Name","NoName")!= "NoName") {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    autoSaveProgress();
                }
            });
        }

    }

    public boolean isAutoSyncAVL() {
        long lastTime =sp.getLong("AutoSync",0);
        long nowTime =System.currentTimeMillis();
        long gap = nowTime-lastTime;
        return gap >= 86400000 * 7;
    }

    public void autoSaveProgress(){
        DatabaseReference mdbRef;
        mdbRef = FirebaseDatabase.getInstance().getReference();
        String Name=sp.getString("User_Name","NoName");
        String Email =sp.getString("User_Email","Noemail@gmail.com");
        String key =sp.getString("User_UID","no");
        int Level =sp.getInt("CompletedLevels",0);
        int Hint =sp.getInt("Hint",0);
        int Solution =sp.getInt("Solution",0);
        long DT =sp.getLong("DT",System.currentTimeMillis());

        UserData object =new UserData(Name,Email,Level,Hint,Solution,DT);

        Map<String,Object> map1 = object.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/Users/" + key, map1);
        mdbRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                editor.putLong("AutoSync",System.currentTimeMillis()).apply();
            }
        });


    }








}