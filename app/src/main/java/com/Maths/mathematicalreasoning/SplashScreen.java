package com.Maths.mathematicalreasoning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;


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
                   Intent LoginIntent = new Intent(SplashScreen.this, LoginActivity.class);
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

    }

    public void setDataBase(){
        try {
            DataBaseHandler db = new DataBaseHandler(this);
            Log.d("Insert: ", "Inserting ..");
            int[] ans= {0,24,30,32,29,27,81,12,25,9,8};

            for (int i=1;i<=10;i++)
            {
                db.addGameLevel(new GameLevel(i, "question"+String.valueOf(i),ans[i], "hint"+String.valueOf(i), "solution"+String.valueOf(i), 0));

            }

            editor.putBoolean("DataBase", true).commit();
        }catch (Exception e){finish();}
    }









}