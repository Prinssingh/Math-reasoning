package com.Math.MathReasoning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class Game_Screen extends AppCompatActivity implements View.OnClickListener {

    //DataBase Handling
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    //Game Level Handling
    private int level, Answer;
    private Bitmap HintImg;
    private Bitmap SolutionImg;
    private Bitmap questionImg;

    // View Handling
    private ImageView quiz;
    private TextView userdisply, adsNotLoaded;
    private TextView Wrong;
    private TextView CurrentLevel;
    private AlertDialog.Builder rightDialog, hintDialog, solutionDialog, PolicyDialog;

    CardView button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button enter;
    ImageButton cleardisplay, GoBack, sharequestion;

    String UD = "";
    Boolean levelScreen;
    ImpFunctions impFun;


    //Google Ads Declaration
    private RewardedAd HintRewardedVideoAd, SolutionRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setup();
                ShowPolicyChangedDialog();
            }
        });

    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    public void setup() {

        //Level Setup
        Intent intent = getIntent();
        level = intent.getIntExtra("Level", 1);
        levelScreen = intent.getBooleanExtra("LevelScreen", false);


        sp = getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor = sp.edit();

        impFun = new ImpFunctions(getApplicationContext());

        quiz = findViewById(R.id.questionimg);
        rightDialog = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar);
        hintDialog = new AlertDialog.Builder(this);
        PolicyDialog = new AlertDialog.Builder(this);
        solutionDialog = new AlertDialog.Builder(this);


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
        userdisply = findViewById(R.id.userdisplay);
        cleardisplay = findViewById(R.id.cleardisplay);
        Wrong = findViewById(R.id.WrongAns);
        ImageButton getHint = findViewById(R.id.getHint);
        GoBack = findViewById(R.id.goBAck);
        sharequestion = findViewById(R.id.shareQuestion);
        CurrentLevel = findViewById(R.id.currentLevel);
        CurrentLevel.setText("Level : " + level);


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
        sharequestion.setOnClickListener(this);


        //Getting Initial game
        newGame();
        HintRewardedVideoAd = createAndLoadHintRewardedAd();
        SolutionRewardedVideoAd = createAndLoadSolutionRewardedAd();
        //loadSolutionAd();
    }

    @Override
    public void onBackPressed() {
        impFun.OnclickSound();
        if (levelScreen) {
            GoBackToLevels();
        }
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        impFun.OnclickSound();
        switch (view.getId()) {
            case R.id.enter:
                validate();
                userdisply.setText("");
                UD = "";
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
                ClearDisplay();
                break;
            case R.id.getHint:
                if (impFun.isConnectedToInternet()) {
                    ShowHintDialog();
                } else {
                    impFun.ShowToast(getLayoutInflater(), "No Internet Connection!!", "Please, Connect to an Internet for Good Experience!!");
                }
                break;
            case R.id.goBAck:
                GoBackToLevels();
                break;
            case R.id.shareQuestion:
                shareimg();
            default:
                break;

        }


    }

    private void ClearDisplay() {
        try {
            String data = userdisply.getText().toString();
            data = data.substring(0, data.length() - 1);
            userdisply.setText(data);
            UD = data;
        } catch (Error | Exception e) {
            userdisply.setText("");
            UD = "";
        }

    }

    public void setUserDisplay(String d) {
        if (userdisply.getText().length() <= 5) {
            UD += d;
            if (UD.startsWith("0") && UD.length() >= 2) {
                UD = String.valueOf(Integer.parseInt(UD));
            }
            userdisply.setText(UD);
        }
    }

    @SuppressLint("SetTextI18n")
    public void newGame() {
        if (level > impFun.getTotalLevels()) {
            level = 1;
            Completed();
        }
        CurrentLevel.setText("Level : " + level);
        questionImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("question" + level, "drawable", getPackageName()));
        HintImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("hint" + level, "drawable", getPackageName()));
        SolutionImg = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("solution" + level, "drawable", getPackageName()));
        Answer = Constants.Answers[level];
        quiz.setImageBitmap(questionImg);
    }

    public void validate() {

        String answer = String.valueOf(Answer);
        String userdisplay = userdisply.getText().toString();

        if (answer.equalsIgnoreCase(userdisplay)) {
            //show write dialogBox
            impFun.correctSound();
            rightAlert();
            if (level > sp.getInt("CompletedLevels", 0)) {
                editor.putInt("CompletedLevels", level).commit();
                editor.putLong("DT", System.currentTimeMillis()).commit();
            }

        } else {
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

        View rightDialogView = getLayoutInflater().inflate(R.layout.right, null);
        rightDialog.setView(rightDialogView);

        CardView next = rightDialogView.findViewById(R.id.NextGame);

        final AlertDialog right = rightDialog.create();
        right.show();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level = level + 1;
                impFun.OnclickSound();
                newGame();
                right.dismiss();
            }
        });

    }

    private AlertDialog hint123;

    public void ShowHintDialog() {
        View HintDialogView = getLayoutInflater().inflate(R.layout.hint_dialog, null);
        hintDialog.setView(HintDialogView);
        hintDialog.setCancelable(false);
        CardView hint = HintDialogView.findViewById(R.id.ShowHintRewardVideo);
        CardView solution = HintDialogView.findViewById(R.id.ShowSolutionRewardVideo);
        Button close = HintDialogView.findViewById(R.id.CloseGetHintDialog);
        adsNotLoaded = HintDialogView.findViewById(R.id.adsNotLoaded);

        hint123 = hintDialog.create();
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
                ShowRewardedHintAd(); // For Testing  : ShowSolution(HintImg,"Hint");

            }
        });
        // Solution Video
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                ShowRewardedSolutionAd();
                //ShowSolutionAd(); // For Testing : ShowSolution(SolutionImg,"Solution");

            }
        });


    }

    public void ShowSolution(Bitmap ans, final String type) {
        View SolutionView = getLayoutInflater().inflate(R.layout.solution_dialog, null);
        solutionDialog.setView(SolutionView);
        solutionDialog.setCancelable(false);

        ImageView solution = SolutionView.findViewById(R.id.SolutionImg);
        solution.setImageBitmap(ans);
        Button close = SolutionView.findViewById(R.id.closeSolutionDialog);

        final AlertDialog Sol = solutionDialog.create();
        Objects.requireNonNull(Sol.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Sol.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impFun.OnclickSound();
                Sol.dismiss();
                if (type.equals("Hint")) {
                    editor.putInt("Hint", sp.getInt("Hint", 0) + 1);
                } else {
                    editor.putInt("Solution", sp.getInt("Solution", 0) + 1);
                }
                // Per Day Limit Function Call
                LimitAdsPerDay();
            }
        });


    }

    public void Completed() {
        AlertDialog.Builder Complete;
        Complete = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar);
        View SuccessView = getLayoutInflater().inflate(R.layout.completed_100_levels, null);
        Complete.setView(SuccessView);
        Button Rank = SuccessView.findViewById(R.id.Rank);

        final AlertDialog Done = Complete.create();
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

    public void GoBackToLevels() {

        int page = 0;
        if (level <= 20) {
            page = 0;
        } else if (level <= 40) {
            page = 1;
        } else if (level <= 60) {
            page = 2;
        } else if (level <= 80) {
            page = 3;
        } else if (level <= 100) {
            page = 4;
        }
        Intent intent2 = new Intent(this, Levels_Screen.class);
        intent2.putExtra("Page", page);
        startActivity(intent2);
        finish();

    }

    public RewardedAd createAndLoadHintRewardedAd() {
        Log.d("TAG", "createAndLoadHintRewardedAd: FunctionCalled!!");
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");// TODO Add HINT ADD ID : ca-app-pub-9095339188186410/5824836857
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                try {
                    adsNotLoaded.setVisibility(View.INVISIBLE);
                } catch (Exception ignored) {
                }
                Log.d("TAG", "createAndLoadHintRewardedAd: Hint Loaded!!");
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Log.d("TAG", "createAndLoadHintRewardedAd: Hint Loading Failed!!" + adError);

                try {
                    adsNotLoaded.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void ShowRewardedHintAd() {

        if (adAVL() && HintRewardedVideoAd.isLoaded()) {
            Log.d("TAG", "ShowRewardedHintAd: Loaded " + HintRewardedVideoAd.isLoaded());
            try {
                //HintRewardedVideoAd1.show();
                RewardedAdCallback adCallback = new RewardedAdCallback() {

                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                        try {
                            hint123.dismiss();
                        } catch (Exception ignored) {
                        }
                        HintRewardedVideoAd = createAndLoadHintRewardedAd();
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.
                        ShowSolution(HintImg, "Hint");
                    }

                    @Override
                    public void onRewardedAdFailedToShow(AdError adError) {
                        // Ad failed to display.
                        try {
                            adsNotLoaded.setVisibility(View.VISIBLE);
                        } catch (Exception ignored) {
                        }
                    }
                };
                HintRewardedVideoAd.show(this, adCallback);

            } catch (Exception | Error ignored) {
            }
        } else {
            try {

                adsNotLoaded.setVisibility(View.VISIBLE);
            } catch (Exception ignored) {
            }
        }

    }

    public RewardedAd createAndLoadSolutionRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");// TODO Add SOLUTION ADD ID : ca-app-pub-9095339188186410/5441693477
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                try {
                    adsNotLoaded.setVisibility(View.INVISIBLE);
                } catch (Exception ignored) {
                }
                Log.d("TAG", "createAndLoadHintRewardedAd: Hint Loaded!!");
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Log.d("TAG", "createAndLoadHintRewardedAd: Hint Loading Failed!!" + adError);

                try {
                    adsNotLoaded.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void ShowRewardedSolutionAd() {

        if (adAVL() && SolutionRewardedVideoAd.isLoaded()) {
            try {
                    RewardedAdCallback adCallback = new RewardedAdCallback() {

                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                        try {
                            hint123.dismiss();
                        } catch (Exception ignored) {
                        }
                        SolutionRewardedVideoAd = createAndLoadHintRewardedAd();
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.
                        ShowSolution(SolutionImg, "Solution");
                    }

                    @Override
                    public void onRewardedAdFailedToShow(AdError adError) {
                        // Ad failed to display.
                        try {
                            adsNotLoaded.setVisibility(View.VISIBLE);
                        } catch (Exception ignored) {
                        }
                    }
                };

                HintRewardedVideoAd.show(this, adCallback);

            } catch (Exception | Error ignored) {
            }
        } else {
            try {
                adsNotLoaded.setVisibility(View.VISIBLE);
            } catch (Exception ignored) {
            }
        }

    }

    public boolean adAVL() {
        Log.d("TAG", "adAVL:"+sp.getInt("LastDateAdsCount", 1) );
        return sp.getInt("LastDateAdsCount", 1) < 10;
    }

    public void shareimg() {
        try {

            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            questionImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, "com.Math.MathReasoning", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.PlayStoreLink);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    public void LimitAdsPerDay() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new Date());
        Log.d("TAG", "LimitAdsPerDay: " + currentDate);
        if (sp.contains("LastAdsDate")) {
            String lastDate = sp.getString("LastAdsDate", "NoDate");
            assert lastDate != null;
            if (lastDate.equals(currentDate)) {
                editor.putInt("LastDateAdsCount", sp.getInt("LastDateAdsCount", 1) + 1).commit();
            } else {
                editor.putString("LastAdsDate", currentDate);
                editor.putInt("LastDateAdsCount", 1).commit();
            }
        } else {
            editor.putString("LastAdsDate", currentDate);
            editor.putInt("LastDateAdsCount", 1).commit();
        }

    }

    public void ShowPolicyChangedDialog() {
        if (!sp.getBoolean("PolicyChangedDialog", false)) {
                View SolutionView = getLayoutInflater().inflate(R.layout.policy_changed_dialog_iew, null);
                PolicyDialog.setView(SolutionView);
                PolicyDialog.setCancelable(true);

                Button GotIt = SolutionView.findViewById(R.id.gotitButton);
                Button Cancle = SolutionView.findViewById(R.id.cancleButton);

                final AlertDialog Sol = PolicyDialog.create();
                Objects.requireNonNull(Sol.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Sol.show();
                GotIt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putBoolean("PolicyChangedDialog", true).commit();
                        Sol.dismiss();
                    }
                });
                Cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Sol.dismiss();
                    }
                });

            }



    }

}