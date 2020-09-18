package com.Maths.mathematicalreasoning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private static final int RC_SIGN_IN = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp=getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();
        try{
            startEmailLogin();
        }catch (Exception ignored) {}


    }

    public void startEmailLogin(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.d("Responce data",""+response);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;


                editor.putBoolean("Login",true).commit();
                editor.putString("User_Name",user.getDisplayName()).commit();
                editor.putString("User_Email",user.getEmail()).commit();
                editor.putString("User_UID",user.getUid()).commit();
                editor.putBoolean("Sync_Periodically",true).commit();
                Toast.makeText(this,"Login Success!!",Toast.LENGTH_LONG).show();

                //Get Data If User Already Registered!!
                try {
                    GetMyProgress();

                }catch (Exception ignored){}


            } else {
                Toast.makeText(this,"Failed to Login !!",Toast.LENGTH_LONG).show();

            }
        }
        else{
            Toast.makeText(this,"Failed to Create Login Request !!",Toast.LENGTH_LONG).show();
        }
        Intent homeintent = new Intent(LoginActivity.this, DashBoard.class);
        startActivity(homeintent);
        finish();
    }



    public void GetMyProgress(){
        DatabaseReference mdbRef;
        final ProgressDialog progressDialog1 =new ProgressDialog(this);
        progressDialog1.setMessage("Getting Your Progress.. !!");
        progressDialog1.show();

        String key =sp.getString("User_UID","");
        mdbRef = FirebaseDatabase.getInstance().getReference().child("Users/"+key);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserData user=dataSnapshot.getValue(UserData.class);

                try {
                    assert user != null;
                    editor.putString("User_Name", user.getName()).commit();
                    editor.putString("User_Email", user.getEmail()).commit();
                    editor.putInt("CompletedLevels", user.getLevel()).commit();
                    progressDialog1.setMessage("Done Getting Progress.. !!");
                    progressDialog1.hide();
                }catch (Exception ignored){}

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog1.setMessage("No Progress Found");
                progressDialog1.hide();


                 }
        };
        mdbRef.addValueEventListener(postListener);


    }





}