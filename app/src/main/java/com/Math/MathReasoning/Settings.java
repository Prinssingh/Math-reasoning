package com.Math.MathReasoning;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;


public class Settings extends AppCompatActivity {
    ImpFunctions impFun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                Toolbar toolbar =  findViewById(R.id.settingToolbar);
                setSupportActionBar(toolbar);
                impFun=new ImpFunctions(getApplicationContext());


                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
            }
        });




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        Preference NamePreference;
        private SharedPreferences sp;
        private SharedPreferences.Editor editor;
        ImpFunctions impFun;
        private AlertDialog.Builder ContactAlert;
        private LinearLayout instagram;
        private LinearLayout facebook;
        private LinearLayout youtube;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // important Functions
            impFun=new ImpFunctions(requireContext());
            ContactAlert=new AlertDialog.Builder(requireContext());


            //Settings
            NamePreference = findPreference("Name");
            final SwitchPreferenceCompat Sound =findPreference("Sound");
            final Preference SyncNow =findPreference("SyncNow");
            final Preference FollowUs =findPreference("FollowUs");
            final Preference ContactUs =findPreference("ContactUs");
            final Preference PrivacyPolicy =findPreference("PrivacyPolicy");
            final Preference Feedback =findPreference("Feedback");

            sp= requireContext().getSharedPreferences("MathsResoninngData", MODE_PRIVATE);
            editor=sp.edit();



            if (NamePreference != null) {
                NamePreference.setSummary(sp.getString("User_Name","Unknown User"));
                NamePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        impFun.OnclickSound();
                        if(sp.getInt("NameChanged",0)<2){
                            ChangeName(2-sp.getInt("NameChanged",0));
                        }
                        else{
                            Toast.makeText(requireContext(),"You can't change your name!!",Toast.LENGTH_LONG).show();
                        }

                        return false;
                    }
                });
            }

            if (Sound != null) {
              Sound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                  @Override
                  public boolean onPreferenceChange(Preference preference, Object newValue) {

                      if(Sound.isChecked()){
                          editor.putBoolean("Sound",false).apply();
                          Sound.setChecked(false);
                      }
                      else {
                          editor.putBoolean("Sound",true).commit();
                          Sound.setChecked(true);
                          impFun.OnclickSound();
                      }
                      return false;
                  }
              });

            }

            if (SyncNow!=null) {
                SyncNow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        impFun.OnclickSound();
                        if (isSyncNowAVL()) {
                            if (impFun.isConnectedToInternet()) {
                                if (!sp.getBoolean("Login", false)) {
                                    Intent LoginIntent = new Intent(getActivity(), CustomLogin.class);
                                    startActivity(LoginIntent);

                                } else {
                                    String Name =sp.getString("User_Name","NoName");
                                    if(!Name.isEmpty()){
                                    impFun.SyncData();
                                    editor.putLong("LastSyncTime", System.currentTimeMillis()).commit();
                                    }
                                    else{
                                        Toast.makeText(requireContext(),"Please fill your name for sync globally",Toast.LENGTH_LONG).show();
                                    }
                                }

                            } else {
                                impFun.ShowToast(getLayoutInflater(), "No Internet", "Please Connect to internet for Sync your Progress !!");
                            }

                        } else {
                            Toast.makeText(getContext(), "You can sync your data once in a day!!", Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
            }

            if (ContactUs!=null){
                ContactUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        impFun.OnclickSound();
                        Contactus();
                        return true;
                    }
                });
            }

            if(FollowUs!=null){
                FollowUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        impFun.OnclickSound();
                        FollowDilog();
                        return false;
                    }
                });
            }


            if (PrivacyPolicy!=null){
                PrivacyPolicy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        impFun.OnclickSound();
                        Intent intent7 = new Intent(getContext(), com.Math.MathReasoning.PrivacyPolicy.class);
                        startActivity(intent7);
                        return false;
                    }
                });
            }

            if (Feedback!=null){
                Feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        impFun.OnclickSound();
                        rateApp();
                        return true;
                    }
                });
            }
        }


        public void ChangeName(int chance){

            AlertDialog.Builder NamePref;
            NamePref =new AlertDialog.Builder(requireContext());
            View NameView = getLayoutInflater().inflate(R.layout.name_change,null);
            NamePref.setView(NameView);

            TextView message = NameView.findViewById(R.id.message);
            message.setText("Chance left :"+chance);
            final EditText Name =NameView.findViewById(R.id.name);
            Name.setText(sp.getString("User_Name","User"));
            Button Save =NameView.findViewById(R.id.save);
            Button Cancle = NameView.findViewById(R.id.cancle);

            final AlertDialog ShowNameChangeDialog = NamePref.create();
            Objects.requireNonNull(ShowNameChangeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ShowNameChangeDialog.show();
            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    impFun.OnclickSound();
                    if(!Name.getText().toString().isEmpty()) {
                        editor.putString("User_Name",Name.getText().toString()).commit();
                        editor.putInt("NameChanged",sp.getInt("NameChanged",0)+1).commit();
                        NamePreference.setSummary(Name.getText().toString());
                    }
                    ShowNameChangeDialog.dismiss();
                }
            });
            Cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    impFun.OnclickSound();
                    ShowNameChangeDialog.dismiss();
                }
            });


        }

        public boolean isSyncNowAVL(){
            if(sp.contains("LastSyncTime"))
            {
                long LastSynced =sp.getLong("LastSyncTime",0);
                long currentTime =System.currentTimeMillis();

                return currentTime - LastSynced >= 86400000;
            }
            else{
                return true;
            }
        }

        public void rateApp() {
            try
            {
                Intent rateIntent = rateIntentForUrl("market://details");
                startActivity(rateIntent);
            }
            catch (ActivityNotFoundException e)
            {
                Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
                startActivity(rateIntent);
            }
        }

        private Intent rateIntentForUrl(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("%s?id=%s", url,
                            requireContext().getPackageName())));
            int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
            if (Build.VERSION.SDK_INT >= 21)
            {
                flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
            }
            else
            {
                //noinspection deprecation
                flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
            }
            intent.addFlags(flags);
            return intent;
        }

        private void Contactus(){

            View ContactView= getLayoutInflater().inflate(R.layout.contact_dialog,null);
            ContactAlert.setView(ContactView);
            ContactAlert.setCancelable(false);

            final EditText Title=ContactView.findViewById(R.id.title);
            final EditText message=ContactView.findViewById(R.id.message);
            CardView Send=ContactView.findViewById(R.id.sendMail);
            CardView Cancle=ContactView.findViewById(R.id.cancle_dialog);
            ImageButton close =ContactView.findViewById(R.id.close_dialog);


            final AlertDialog ContactDilog=ContactAlert.create();
            Objects.requireNonNull(ContactDilog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ContactDilog.show();
            Cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    impFun.OnclickSound();ContactDilog.dismiss();
                }
            });
            close.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    impFun.OnclickSound();

                    ContactDilog.dismiss();
                }
            });

            Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    impFun.OnclickSound();
                    SendMail(Title.getText().toString(),message.getText().toString());
                    ContactDilog.dismiss();
                }
            });


        }

        private void SendMail(String Title, String Body) {
            String mbody ="Regarding Math-Reasoning Application.\n\n"+Body+"\n\n\nRespectfully \n"
                              +sp.getString("User_Name","User");
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"contact.thefutureprogrammers@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, Title);
            i.putExtra(Intent.EXTRA_TEXT   , mbody);
            try {
                startActivity(Intent.createChooser(i, "Send Mail bia"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        private void FollowDilog(){

            AlertDialog.Builder Follow;
            Follow =new AlertDialog.Builder(requireContext());
            View FollowView = getLayoutInflater().inflate(R.layout.followus_page,null);
            Follow.setView(FollowView);
            instagram = FollowView.findViewById(R.id.instagramSection);
            facebook = FollowView.findViewById(R.id.facebookSection);
            youtube = FollowView.findViewById(R.id.youtubeSection);

            final AlertDialog ShowFollowDilog = Follow.create();
            Objects.requireNonNull(ShowFollowDilog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ShowFollowDilog.show();
            instagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    desableFollow();
                    impFun.OnclickSound();
                    OpenInsta();
                }
            });
            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    desableFollow();
                    impFun.OnclickSound();
                    OpenFaceook();
                }
            });

            youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    desableFollow();
                    impFun.OnclickSound();
                    OpenYoutube();
                }
            });

        }

        private void OpenInsta(){
            String scheme = "http://instagram.com/_u/the_future_programmers/";
            String path = "https://instagram.com/the_future_programmers/";

            String nomPackageInfo ="com.instagram.android";
            Intent intentAiguilleur;
            try {
                requireActivity().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                intentAiguilleur = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));

            } catch (Exception e) {
                intentAiguilleur = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            }
            requireActivity().startActivity(intentAiguilleur);

            enableFollow();
        }

        private  void OpenFaceook() {
            try {
                requireActivity(). getPackageManager().getPackageInfo("com.facebook.katana", 0);
                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/101278845077494")));
            } catch (Exception e) {
                requireActivity().startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/thefutureprogrammers/")));
            }
            enableFollow();
        }

        private void OpenYoutube(){
            String id="";
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:channel/UCqQNtHFnmMu-W8kQbVPx6zA?view_as=subscriber"));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UCqQNtHFnmMu-W8kQbVPx6zA?view_as=subscriber"));
            try {
                requireActivity().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                requireActivity().startActivity(webIntent);
            }
            enableFollow();
        }

        private void enableFollow(){
            try{
                instagram.setEnabled(true);
                facebook.setEnabled(true);
                youtube.setEnabled(true);
            }
            catch(Exception ignored){ }
        }

        private void desableFollow(){
            try{
                instagram.setEnabled(true);
                facebook.setEnabled(true);
                youtube.setEnabled(true);
            }
            catch(Exception ignored){ }
        }
    }



}