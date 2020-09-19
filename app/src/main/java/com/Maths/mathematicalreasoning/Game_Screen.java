package com.Maths.mathematicalreasoning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Objects;

public class Game_Screen extends AppCompatActivity implements View.OnClickListener {

    //DataBase Handling

    DataBaseHandler db;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //Google Ads Handling
    private static final String HintAdsId="ca-app-pub-3940256099942544/5224354917";// Test ID ca-app-pub-3940256099942544/5224354917
    private static final String SolutionAdsId="ca-app-pub-3940256099942544/5224354917";// Test ID ca-app-pub-3940256099942544/5224354917
    private RewardedAd HintRewardedAd,SolutionRewardedAd;

    //Game Level Handling
    private int level,Answer;
    private Bitmap HintImg;
    private Bitmap SolutionImg;

    // View Handling
    private ImageView quiz;
    private TextView userdisply,Wrong;
    private AlertDialog.Builder rightDialog,hintDialog,solutionDialog;

    CardView button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    Button enter;
    ImageButton  cleardisplay,getHint;

    String UD="";
    Boolean levelScreen;
    ImpFunctions impFun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
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

        db=new DataBaseHandler(this);
        sp=getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        impFun= new ImpFunctions(getApplicationContext());

        quiz=findViewById(R.id.questionimg);
        rightDialog=new AlertDialog.Builder(this,android.R.style.Theme_Translucent_NoTitleBar);
        hintDialog=new AlertDialog.Builder(this);
        solutionDialog=new AlertDialog.Builder(this);


        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.register);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        enter = findViewById(R.id.enter);
        userdisply=findViewById(R.id.userdisplay);
        cleardisplay =findViewById(R.id.cleardisplay);
        Wrong=findViewById(R.id.WrongAns);
        getHint =findViewById(R.id.getHint);

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

        // initiallygoogle Ads Setup
        HintRewardedAd     =  createAndLoadRewardedAd(HintAdsId);
        SolutionRewardedAd = createAndLoadRewardedAd(SolutionAdsId);

        //Getting Initial game
        newGame();

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
        impFun.OnclickSound();
        switch (view.getId()){
            case R.id.enter:
                validate();
                userdisply.setText("");
                UD="";
                return;
            case R.id.button0:
                setUserDisplay("0");
                break;
            case R.id.register:
                setUserDisplay("1");
                break;
            case R.id.button2:
                setUserDisplay("2");
                break;
            case R.id.button3:
                setUserDisplay("3");
                break;
            case R.id.button4:
                setUserDisplay("4");
                break;
            case R.id.button5:
                setUserDisplay("5");
                break;
            case R.id.button6:
                setUserDisplay("6");
                break;
            case R.id.button7:
                setUserDisplay("7");
                break;
            case R.id.button8:
                setUserDisplay("8");
                break;
            case R.id.button9:
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

        GameLevel leveX = db.getGameLevel(level);
        Bitmap questionImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(leveX.getQuestion(), "drawable", getPackageName()));
        HintImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier( leveX.getHint() , "drawable", getPackageName()));
        SolutionImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier( leveX.getSolution() , "drawable", getPackageName()));
        Answer=leveX.getAnswer();
        quiz.setImageBitmap(questionImg);
    }

    public void validate() {

        String answer = String.valueOf(Answer);
        String userdisplay = userdisply.getText().toString();

        if(answer.equalsIgnoreCase(userdisplay))
        {
            //show write dialogBox
            rightAlert();
            if (level > sp.getInt("CompletedLevels",0)){
                editor.putInt("CompletedLevels",level).commit();}

        }
        else
        {
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
                newGame();
                right.dismiss();
            }
        });

    }

    public void ShowHintDialog() {
        View HintDialogView= getLayoutInflater().inflate(R.layout.hint_dialog,null);
        hintDialog.setView(HintDialogView);
        hintDialog.setCancelable(false);
        CardView hint = HintDialogView.findViewById(R.id.ShowHintRewardVideo);
        CardView solution = HintDialogView.findViewById(R.id.ShowSolutionRewardVideo);
        CardView close =HintDialogView.findViewById(R.id.CloseGetHintDialog);

        final AlertDialog hint123=hintDialog.create();
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
                ShowHintVideoAds();
                hint123.dismiss();
            }
        });
        // Solution Video
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                ShowSolutionVideoAds();
                hint123.dismiss();
            }
        });


    }

    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(this, adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void ShowHintVideoAds(){
        if (HintRewardedAd.isLoaded()) {
            Activity activityContext =this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {

                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    HintRewardedAd = createAndLoadRewardedAd(HintAdsId);
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    ShowSolution(HintImg);
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            };
            HintRewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            impFun.ShowToast(getLayoutInflater(),"NO Internet!!","Please, Connect to a good internet for using Hint !!");
        }
    }

    public void ShowSolutionVideoAds(){
        if (SolutionRewardedAd.isLoaded()) {
            Activity activityContext = this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    SolutionRewardedAd = createAndLoadRewardedAd(SolutionAdsId);
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    ShowSolution(SolutionImg);
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            };
            SolutionRewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            impFun.ShowToast(getLayoutInflater(),"NO Internet!!","Please, Connect to a good internet for using Solution !!");

        }
    }

    public  void ShowSolution(Bitmap ans){
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
            }
        });


    }




}