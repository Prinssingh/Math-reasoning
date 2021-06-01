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


public class Level4 extends Fragment {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int i;
    ImpFunctions impFun;


    public static Level4 newInstance() {
        return new Level4();
    }
    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.level_screen4, container, false);

        sp=requireActivity().getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();
        impFun =new ImpFunctions(requireContext());

        int totalEnabled;
        int enabledLevels =sp.getInt("CompletedLevels",0)+1;

        if (enabledLevels >60 && enabledLevels <=80 ){
            totalEnabled=enabledLevels;
        }else if( enabledLevels >80){
            totalEnabled=80;
        }else{
            totalEnabled=0;
        }


        for(i=61;i<=totalEnabled;i++ ){
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