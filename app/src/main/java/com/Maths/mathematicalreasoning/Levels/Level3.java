package com.Maths.mathematicalreasoning.Levels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.Maths.mathematicalreasoning.Game_Screen;
import com.Maths.mathematicalreasoning.R;

public class Level3 extends Fragment {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int i;


    public static Level3 newInstance() {
        return new Level3();
    }
    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.level_screen3, container, false);

        sp=requireActivity().getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        int totalEnabled;
        int enabledLevels =sp.getInt("CompletedLevels",0)+1;

        if (enabledLevels >40 && enabledLevels <=60 ){
            totalEnabled=enabledLevels;
        }else if( enabledLevels >60){
            totalEnabled=60;
        }else{
            totalEnabled=0;
        }


        for(i=41;i<=totalEnabled;i++ ){
            final int j = i;
            int id= getResources().getIdentifier("button"+i, "id", requireActivity().getPackageName());
            Button  b=new Button(getContext());
            b  =root.findViewById(id);
            b.setEnabled(true);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(getContext(), Game_Screen.class);
                    intent1.putExtra("Level",j);
                    intent1.putExtra("LevelScreen",true);
                    startActivity(intent1);
                    requireActivity().finish();
                }
            });

        }



        return root;

    }
}