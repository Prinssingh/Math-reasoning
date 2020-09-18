package com.Maths.mathematicalreasoning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.Objects;

public class DashBoard extends AppCompatActivity {

    private AlertDialog.Builder ExitAlertDialog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private CardView play,levels,setting,share,rank,exit;
    ImpFunctions impFun;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dash_board);
        play=(CardView) findViewById(R.id.play);
        levels=(CardView) findViewById(R.id.levels);
        setting=(CardView) findViewById(R.id.setting);
        share=(CardView) findViewById(R.id.share);
        rank=(CardView) findViewById(R.id.rank);
        exit=(CardView) findViewById(R.id.exit);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setup();
            }
        });






    }

    @SuppressLint("CommitPrefEdits")
    public void setup(){

        //Game data
        sp=getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        impFun =new ImpFunctions(getApplicationContext());

        ExitAlertDialog=new AlertDialog.Builder(this);



        // No Internet connection alert
        if (! impFun.isConnectedToInternet()) {
            impFun.ShowToast(getLayoutInflater(),"No Internet Connection!!",
                    "Please, Connect to an Internet for Good Experience!!");
        }


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                SetDisableAll();
                Intent intent1 = new Intent(DashBoard.this, Game_Screen.class);
                intent1.putExtra("Level",sp.getInt("CompletedLevels",0)+1);
                startActivity(intent1);
            }
        });

        levels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                SetDisableAll();
                Intent intent2 = new Intent(DashBoard.this, Levels_Screen.class);
                intent2.putExtra("Page",0);
                startActivity(intent2);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                SetDisableAll();
                Intent intent3 = new Intent(DashBoard.this, Settings.class);
                startActivity(intent3);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                SetDisableAll();
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Math-Reasoning");
                    String shareMessage= "\nMath-Reasoning By The Future Programmers:\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share with"));
                } catch(Exception e) {
                    //e.toString();
                }


            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                SetDisableAll();
                Intent intent6 = new Intent(DashBoard.this, GlobalRanking.class);
                startActivity(intent6);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                SetDisableAll();
                ExitDialog();
            }
        });
    }

    @Override
    public void onBackPressed(){
        impFun.OnclickSound();
        ExitDialog();
    }

    @Override
    protected void onResume() {
        SetEnableAll();
        super.onResume();
    }

    public void ExitDialog(){
        View ExitDialogView= getLayoutInflater().inflate(R.layout.exit_dialog,null);
        ExitAlertDialog.setView(ExitDialogView);

        CardView yes = ExitDialogView.findViewById(R.id.yes);
        CardView no = ExitDialogView.findViewById(R.id.no);

        final AlertDialog Exit123=ExitAlertDialog.create();
        Objects.requireNonNull(Exit123.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Exit123.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SetEnableAll();
            }
        });
        Exit123.show();
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                Exit123.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                Exit123.dismiss();
                finish();
            }
        });
    }

    public  void SetDisableAll(){
        play.setEnabled(false);
        levels.setEnabled(false);
        setting.setEnabled(false);
        rank.setEnabled(false);
        share.setEnabled(false);
        exit.setEnabled(false);

    }

    public void SetEnableAll(){
        play.setEnabled(true);
        levels.setEnabled(true);
        setting.setEnabled(true);
        rank.setEnabled(true);
        share.setEnabled(true);
        exit.setEnabled(true);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}