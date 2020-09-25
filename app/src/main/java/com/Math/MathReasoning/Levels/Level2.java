package com.Math.MathReasoning.Levels;

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

import com.Math.MathReasoning.Game_Screen;
import com.Math.MathReasoning.ImpFunctions;
import com.Math.MathReasoning.R;



public class Level2 extends Fragment {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int i;
    ImpFunctions impFun;

    public static Level2 newInstance() {
        return new Level2();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.level_screen2, container, false);

        sp= requireActivity().getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();
        impFun =new ImpFunctions(requireContext());

        int totalEnabled;
        int enabledLevels =sp.getInt("CompletedLevels",0)+1;

        if (enabledLevels >20 && enabledLevels <=40 ){
            totalEnabled=enabledLevels;
        }else if( enabledLevels >40){
            totalEnabled=40;
        }else{
            totalEnabled=0;
        }


        for(i=21;i<=totalEnabled;i++ ){
            final int j = i;
            int id= getResources().getIdentifier("button"+i, "id", requireActivity().getPackageName());
            Button  b=root.findViewById(id);
            b.setEnabled(true);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    impFun.OnclickSound();
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