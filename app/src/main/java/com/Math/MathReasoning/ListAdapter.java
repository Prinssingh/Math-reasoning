package com.Math.MathReasoning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final Integer[] Rank;
    private final String[] Name;
    private final Integer[] Level;

    public ListAdapter(Activity context, Integer[] Rank, String[] Name, Integer[] Level) {
        super(context, R.layout.list_item, Name);

        this.context=context;
        this.Level = Level;
        this.Rank = Rank;
        this.Name =Name;


    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView rank = (TextView) rowView.findViewById(R.id.rank);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView level = (TextView) rowView.findViewById(R.id.level);

        if (position==0)
        {
            rank.setText("Rank");
            rank.setTextColor(Color.parseColor("#ffffff"));
            name.setText("Name");
            name.setTextColor(Color.parseColor("#ffffff"));
            level.setText("Level");
            level.setTextColor(Color.parseColor("#ffffff"));
            rowView.setBackgroundColor(R.color.nextbgcolor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rowView.setBackgroundColor(Color.parseColor("#00c2cb"));
            }


        }
        else {
        rank.setText(String.valueOf(Rank[position]));
        name.setText(Name[position]);
        level.setText(String.valueOf(Level[position]));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(position==1){ rowView.setBackgroundColor(Color.parseColor("#E94A09")); }
//            else if(position == 2){ rowView.setBackgroundColor(Color.parseColor("#7609E9")); }
//            else if(position == 3){ rowView.setBackgroundColor(Color.parseColor("#09E9D8")); }
//        }

        return rowView;

    }
}