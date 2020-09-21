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


        //setupdatabase
        if (! sp.getBoolean("DataBase",false)) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setDataBase();
                }
            });
        }

        //Save Progress if 7 days Complete!!
        if (isAutoSyncAVL()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    autoSaveProgress();
                }
            });
        }
        
    }

    public void setDataBase(){
        try {
            DataBaseHandler db = new DataBaseHandler(this);
            Log.d("Insert: ", "Inserting ..");
            int[] ans= {0,24,30,32,29,27,81,12,25,9,8,
                          6,19,30,14,81191,1,30,3,41,70,
                          8,84,16,2,91,13,24,436,17,11,
                          72,59,28,45,135,59,69,16,7,408,
                          4,121,5,42,969,17,9,54,2,83,
                          3125,88,267,60,18,7,98,8,96,28,
                          4,26,91,467,412,368,10,8,4,42,
                          1,5,27,24,29,14,13,2,10,15,
                          92,408,27,38,41,748,18,10,1,2880,
                          469,232,2031,12,136,36,7766,19,4,23};

            for (int i=1;i<=10;i++)
            {
                db.addGameLevel(new GameLevel(i, "question"+String.valueOf(i),ans[i], "hint"+String.valueOf(i), "solution"+String.valueOf(i), 0));

            }
            editor.putBoolean("DataBase", true).commit();
            Log.d("AllLevels",""+db.getAllGameLevels());
        }catch (Exception e){finish();}
    }

    public boolean isAutoSyncAVL()
    {
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

        UserData object =new UserData(Name,Email,Level);
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