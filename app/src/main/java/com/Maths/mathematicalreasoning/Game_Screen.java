package com.Maths.mathematicalreasoning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.facebook.ads.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Objects;


public class Game_Screen extends AppCompatActivity implements View.OnClickListener {

    //DataBase Handling
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    //Game Level Handling
    private int level,Answer;
    private Bitmap HintImg;
    private Bitmap SolutionImg;

    // View Handling
    private ImageView quiz;
    private TextView userdisply,adsNotLoaded;
    private TextView Wrong;
    private TextView CurrentLevel;
    private AlertDialog.Builder rightDialog,hintDialog,solutionDialog;

    CardView button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    Button enter;
    ImageButton  cleardisplay,GoBack;

    String UD="";
    Boolean levelScreen;
    ImpFunctions impFun;

    // FaceBook Ads declaration
    private RewardedVideoAd HintRewardedVideoAd,SolutionRewardedVideoAd;
    int adsCount=0;
    String TAG="Rewarded Video Ad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                AudienceNetworkAds.initialize(Game_Screen.this);
                setup();
            }
        });

    }

    @SuppressLint("CommitPrefEdits")
    public void setup(){

        //Level Setup
        Intent intent = getIntent();
        level=intent.getIntExtra("Level",1);
        levelScreen =intent.getBooleanExtra("LevelScreen",false);


        sp=getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        impFun= new ImpFunctions(getApplicationContext());

        quiz=findViewById(R.id.questionimg);
        rightDialog=new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar);
        hintDialog=new AlertDialog.Builder(this);
        solutionDialog=new AlertDialog.Builder(this);


        button0 = findViewById(R.id.number0);
        button1 = findViewById(R.id.number1);
        button2 = findViewById(R.id.number2);
        button3 = findViewById(R.id.number3);
        button4 = findViewById(R.id.number4);
        button5 = findViewById(R.id.number5);
        button6 = findViewById(R.id.number6);
        button7 = findViewById(R.id.number7);
        button8 = findViewById(R.id.number8);
        button9 = findViewById(R.id.number9);
        enter = findViewById(R.id.enter);
        userdisply=findViewById(R.id.userdisplay);
        cleardisplay =findViewById(R.id.cleardisplay);
        Wrong=findViewById(R.id.WrongAns);
        TextView getHint = findViewById(R.id.getHint);
        GoBack =findViewById(R.id.goBAck);
        CurrentLevel =findViewById(R.id.currentLevel);
        CurrentLevel.setText("Level : "+level);


        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        enter.setOnClickListener(this);
        cleardisplay.setOnClickListener(this);
        getHint.setOnClickListener(this);
        GoBack.setOnClickListener(this);


        //Getting Initial game
        newGame();
        loadHindAd();
        loadSolutionAd();
    }

    @Override
    public void onBackPressed() {
        impFun.OnclickSound();
        if (levelScreen) {
           // continue;
            int page=0;
            if(level<=20){ page=0;}
            else if (level <= 40){ page=1;}
            else if(level <= 60){page=2;}
            else if(level <= 80){page=3;}
            else if(level <= 100){page=4;}
            Intent intent2 = new Intent(this, Levels_Screen.class);
            intent2.putExtra("Page",page);
            startActivity(intent2);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        view.playSoundEffect(SoundEffectConstants.CLICK);
        switch (view.getId()){
            case R.id.enter:
                validate();
                userdisply.setText("");
                UD="";
                return;
            case R.id.number0:
                setUserDisplay("0");
                break;
            case R.id.number1:
                setUserDisplay("1");
                break;
            case R.id.number2:
                setUserDisplay("2");
                break;
            case R.id.number3:
                setUserDisplay("3");
                break;
            case R.id.number4:
                setUserDisplay("4");
                break;
            case R.id.number5:
                setUserDisplay("5");
                break;
            case R.id.number6:
                setUserDisplay("6");
                break;
            case R.id.number7:
                setUserDisplay("7");
                break;
            case R.id.number8:
                setUserDisplay("8");
                break;
            case R.id.number9:
                setUserDisplay("9");
                break;
            case R.id.cleardisplay:
                userdisply.setText("");
                UD="";
                break;
            case  R.id.getHint:
                if (impFun.isConnectedToInternet()){
                    ShowHintDialog();
                }else{impFun.ShowToast(getLayoutInflater(),"No Internet Connection!!","Please, Connect to an Internet for Good Experience!!");}
                break;
            case R.id.goBAck:
                GoBackToLevels();
                break;
            default:
                break;

        }


    }

    public void setUserDisplay(String d){
        if (userdisply.getText().length()<=5) {
            UD += d;
            if (UD.startsWith("0") && UD.length() >= 2) {
                UD = String.valueOf(Integer.parseInt(UD));
            }
            userdisply.setText(UD);
        }
    }

    public void newGame() {
        CurrentLevel.setText("Level : "+level);
        if(level >impFun.getTotalLevels())
        {
            level=1;
            Completed();
        }
        Bitmap questionImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("question"+level, "drawable", getPackageName()));
        HintImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier( "hint"+level , "drawable", getPackageName()));
        SolutionImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier( "solution"+level , "drawable", getPackageName()));
        Answer=Constants.Answers[level];
        quiz.setImageBitmap(questionImg);
    }

    public void validate() {

        String answer = String.valueOf(Answer);
        String userdisplay = userdisply.getText().toString();

        if(answer.equalsIgnoreCase(userdisplay))
        {
            //show write dialogBox
            impFun.correctSound();
            rightAlert();
            if (level > sp.getInt("CompletedLevels",0)){
                editor.putInt("CompletedLevels",level).commit();
                editor.putLong("DT",System.currentTimeMillis()).commit();}

        }
        else
        {
            impFun.wrongSound();
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Wrong.setVisibility(View.VISIBLE);
                }

                public void onFinish() {
                    Wrong.setVisibility(View.INVISIBLE);
                }

            }.start();
        }

        userdisply.setText("");
    }

    public void rightAlert() {

        View rightDialogView= getLayoutInflater().inflate(R.layout.right,null);
        rightDialog.setView(rightDialogView);

        CardView next = rightDialogView.findViewById(R.id.NextGame);

        final AlertDialog right=rightDialog.create();
        right.show();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level=level+1;
                impFun.OnclickSound();
                newGame();
                right.dismiss();
            }
        });

    }

    private AlertDialog hint123;
    public void ShowHintDialog() {
        View HintDialogView= getLayoutInflater().inflate(R.layout.hint_dialog,null);
        hintDialog.setView(HintDialogView);
        hintDialog.setCancelable(false);
        CardView hint = HintDialogView.findViewById(R.id.ShowHintRewardVideo);
        CardView solution = HintDialogView.findViewById(R.id.ShowSolutionRewardVideo);
        Button close =HintDialogView.findViewById(R.id.CloseGetHintDialog);
        adsNotLoaded =HintDialogView.findViewById(R.id.adsNotLoaded);

        hint123=hintDialog.create();
        Objects.requireNonNull(hint123.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        hint123.show();
        // Close the Dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                hint123.dismiss();
            }
        });
        // Hint video
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                // Show A video ad
                ShowHintAd();

            }
        });
        // Solution Video
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                ShowSolutionAd();
            }
        });


    }

    public  void ShowSolution(Bitmap ans, final String type){
        View SolutionView= getLayoutInflater().inflate(R.layout.solution_dialog,null);
        solutionDialog.setView(SolutionView);
        solutionDialog.setCancelable(false);

        ImageView solution = SolutionView.findViewById(R.id.SolutionImg);
        solution.setImageBitmap(ans);
        Button close =SolutionView.findViewById(R.id.closeSolutionDialog);

        final AlertDialog Sol=solutionDialog.create();
        Objects.requireNonNull(Sol.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Sol.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                Sol.dismiss();
                if(type.equals("Hint")){editor.putInt("Hint",sp.getInt("Hint",0)+1);}
                else{editor.putInt("Solution",sp.getInt("Solution",0)+1);}
            }
        });


    }

    public void Completed(){
        AlertDialog.Builder Complete;
        Complete=new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar);
        View SuccessView= getLayoutInflater().inflate(R.layout.completed_100_levels,null);
        Complete.setView(SuccessView);
        Button Rank =SuccessView.findViewById(R.id.Rank);

        final AlertDialog Done=Complete.create();
        Done.show();
        Rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                Intent intent6 = new Intent(Game_Screen.this, GlobalRanking.class);
                startActivity(intent6);
                Done.dismiss();
                finish();
            }
        });
    }

    public void GoBackToLevels(){

        int page=0;
        if(level<=20){ page=0;}
        else if (level <= 40){ page=1;}
        else if(level <= 60){page=2;}
        else if(level <= 80){page=3;}
        else if(level <= 100){page=4;}
        Intent intent2 = new Intent(this, Levels_Screen.class);
        intent2.putExtra("Page",page);
        startActivity(intent2);
        finish();

    }

    public void ShowHintAd(){
        if (HintRewardedVideoAd == null || !HintRewardedVideoAd.isAdLoaded()) {
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            }catch (Exception ignored){}
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (HintRewardedVideoAd.isAdInvalidated()) {
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            }catch (Exception ignored){}
            return;
        }

        if(adAVL()){
            HintRewardedVideoAd.show();
            adsCount+=1;
            if(adsCount%2==0){
                editor.putLong("LastAdTime",System.currentTimeMillis()).commit();
            }
        }
        else{
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            }catch (Exception ignored){}
        }
    }

    public void ShowSolutionAd(){
        if (SolutionRewardedVideoAd == null || !SolutionRewardedVideoAd.isAdLoaded()) {
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            }catch (Exception ignored){}
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (SolutionRewardedVideoAd.isAdInvalidated()) {
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            }catch (Exception ignored){}
            return;
        }
        if(adAVL()){
            SolutionRewardedVideoAd.show();
            adsCount+=1;
            if(adsCount%2==0){
                editor.putLong("LastAdTime",System.currentTimeMillis()).commit();
            }
        }
        else{
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            }catch (Exception ignored){}
        }


    }

    public void loadHindAd(){
        HintRewardedVideoAd = new RewardedVideoAd(this, "823602718393633_827604441326794");
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                try {
                    adsNotLoaded.setVisibility(View.INVISIBLE);
                }catch (Exception ignored){}
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                ShowSolution(HintImg,"Hint");
            }

            @Override
            public void onRewardedVideoClosed() {
                try{
                    hint123.dismiss();
                }catch(Exception ignored){}
                loadHindAd();
            }
        };
        HintRewardedVideoAd.loadAd(
                HintRewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

    }

    public void loadSolutionAd(){

        SolutionRewardedVideoAd = new RewardedVideoAd(this, "823602718393633_826571811430057");
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
                try {
                    adsNotLoaded.setVisibility(View.INVISIBLE);
                }catch (Exception ignored){}
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                ShowSolution(SolutionImg,"Solution");
            }

            @Override
            public void onRewardedVideoClosed() {
                try{
                    hint123.dismiss();
                }catch(Exception ignored){}
                loadSolutionAd();
            }
        };
        SolutionRewardedVideoAd.loadAd(
                SolutionRewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());


    }

    public boolean adAVL(){
        if(adsCount%2==0){
            return System.currentTimeMillis() - sp.getLong("LastAdTime", 0) >= 1000 * 60 * 5;
        }
        else return true;
    }


}