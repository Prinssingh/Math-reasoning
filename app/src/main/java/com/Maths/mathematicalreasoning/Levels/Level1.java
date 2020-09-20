package com.Maths.mathematicalreasoning.Levels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.Maths.mathematicalreasoning.Game_Screen;
import com.Maths.mathematicalreasoning.R;

import java.util.Objects;


public class Level1 extends Fragment {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int i;

    public static Level1 newInstance() {
        return new Level1();
    }


    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.level_screen1, container, false);

        sp= requireActivity().getSharedPreferences("MathsResoninngData", Context.MODE_PRIVATE);
        editor=sp.edit();

        int totalEnabled;
        int enabledLevels =sp.getInt("CompletedLevels",0)+1;

        totalEnabled = Math.min(enabledLevels, 20);

        for(i=1;i<=totalEnabled;i++ ){
            final int j = i;
            int id= getResources().getIdentifier("button"+i, "id", getActivity().getPackageName());
            Log.d("Button id",""+id);
            Button b=new Button(getContext());
            b = root.findViewById(id);
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