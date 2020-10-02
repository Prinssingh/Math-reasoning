package com.Math.MathReasoning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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

        sp= getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        if(!sp.getBoolean("Login",false)){
            Intent LoginIntent = new Intent(GlobalRanking.this, CustomLogin.class);
            startActivity(LoginIntent);
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Global Ranking");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        list =findViewById(R.id.list);
        progressBar=findViewById(R.id.progressBar);

        getFireBaseData();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getFireBaseData() {
        
        //FirebaseApp.initializeApp(getApplicationContext());

        DatabaseReference mdbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allData.clear();
                 for (DataSnapshot snap: dataSnapshot.getChildren()) {
                     if(String.valueOf(snap.getKey()).equals(sp.getString("User_UID", ""))) {
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
                    if(user.getName().isEmpty()){
                        continue;
                    }
                    rank.add(i);
                    name.add(user.getName());
                    level.add(user.getLevel());
                    i++;
                }

                PopulateList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                final Intent homeintent = new Intent(GlobalRanking.this, DashBoard.class);
                Toast.makeText(GlobalRanking.this,"Exception:"+ databaseError,Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                View UnderConstractionView= getLayoutInflater().inflate(R.layout.under_construction,null);
                AlertDialog.Builder UnderConstDialog = new AlertDialog.Builder(GlobalRanking.this,android.R.style.Theme_Translucent_NoTitleBar);
                UnderConstDialog.setView(UnderConstractionView);
                UnderConstDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                Button mainmenu = UnderConstractionView.findViewById(R.id.mainmenu);
                final AlertDialog Constraction=UnderConstDialog.create();
                Constraction.show();
                mainmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(homeintent);
                        Constraction.dismiss();
                        finish();
                    }
                });


            }
        };
        try {
            mdbRef.addValueEventListener(postListener);
        }catch(Exception e){
            Toast.makeText(this, "Exception:-"+e, Toast.LENGTH_SHORT).show();
        }
        catch(Error er){
            Toast.makeText(this, "Error:-"+er, Toast.LENGTH_SHORT).show();
        }
    }

    public void SortAllUsers(){
        if (allData !=null){
            try{
                  Collections.sort(allData,Collections.reverseOrder());
            }
            catch(Exception E){
                Log.d("EXCEPTION","AT Sorting Global Data");
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void PopulateList(){
        try{
            Integer[] Rank = rank.toArray(new Integer[0]);
            String[] Name = name.toArray(new String[0]);
            Integer[] Level = level.toArray(new Integer[0]);
            ListAdapter adapter=new ListAdapter(this,Rank,Name,Level);
            list.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        }catch(Exception ignored){}
    }

}