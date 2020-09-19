package com.Maths.mathematicalreasoning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalRanking extends AppCompatActivity {
    ListView list;
    ProgressBar progressBar;
    SharedPreferences sp;
    UserData Mydata;

    List<UserData> allData =new ArrayList<>();
    List<Integer> rank =new ArrayList<>();
    List<String>  name =new ArrayList<>();
    List<Integer> level =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_ranking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Global Ranking");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        list =findViewById(R.id.list);
        progressBar=findViewById(R.id.progressBar);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getFireBaseData();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getFireBaseData() {

        DatabaseReference mdbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allData.clear();
                 for (DataSnapshot snap: dataSnapshot.getChildren()) {
                     if(String.valueOf(snap.getKey()).equals(sp.getString("User_UID", "no"))) {
                         Mydata=snap.getValue(UserData.class); }
                     UserData p=snap.getValue(UserData.class);
                     if(p!=null){
                         allData.add(p);
                     }
                 }

                 // Sorting data
                SortAllUsers();

               //Clear and Reset the List
                rank.clear();
                name.clear();
                level.clear();

                rank.add(0);
                name.add("Name");
                level.add(0);

                int i=1;
                for (UserData user: allData){
                    rank.add(i);
                    name.add(user.getName());
                    level.add(user.getLevel());
                    i++;
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                   PopulateList();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "................................loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mdbRef.addValueEventListener(postListener);

    }

    public void SortAllUsers(){
        if (allData !=null){
            Collections.sort(allData,Collections.reverseOrder());
        }
    }

    @SuppressLint("ResourceAsColor")
    public void PopulateList(){
        Integer[] Rank = rank.toArray(new Integer[0]);
        String[] Name = name.toArray(new String[0]);
        Integer[] Level = level.toArray(new Integer[0]);
        ListAdapter adapter=new ListAdapter(this,Rank,Name,Level);
        list.setAdapter(adapter);


        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        if(!sp.getBoolean("Login",false)){
            Intent LoginIntent = new Intent(GlobalRanking.this, CustomLogin.class);
            startActivity(LoginIntent);
            finish();
        }
        super.onStart();
    }
}