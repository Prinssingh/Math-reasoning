package com.Math.MathReasoning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.Math.MathReasoning.Levels.Level1;
import com.Math.MathReasoning.Levels.Level2;
import com.Math.MathReasoning.Levels.Level3;
import com.Math.MathReasoning.Levels.Level4;
import com.Math.MathReasoning.Levels.Level5;

public class Levels_Screen extends AppCompatActivity {

    FragmentPagerAdapter PageAdaptor;
    LinearLayout Dotspanel;
    private int dotscount;
    private ImageView[] dots;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_levels);
        Intent intent = getIntent();
        int pageNo=intent.getIntExtra("Page",0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Levels");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Data base and Game data

        ViewPager Pager= findViewById(R.id.Pager);
        PageAdaptor =new MyPagerAdapter(getSupportFragmentManager());
        Pager.setAdapter(PageAdaptor);

        //Pager.setPageTransformer(true, new ZoomOutPageTransformer());


        Dotspanel=(LinearLayout) findViewById(R.id.SliderDots);
        dotscount = PageAdaptor.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(5, 0, 5, 0);

            Dotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        Pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // defult settings
        Pager.setCurrentItem(pageNo);
        dots[pageNo ].setImageDrawable(ContextCompat
                    .getDrawable(getApplicationContext(), R.drawable.active_dot));

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages.
        @Override
        public int getCount() {
            return 5;
        }

        // Returns the fragment to display for a particular page.
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 1:
                    return Level2.newInstance();
                case 2:
                    return Level3.newInstance();
                case 3:
                    return Level4.newInstance();
                case 4:
                    return Level5.newInstance();
                default:
                    return Level1.newInstance();
            }

        }




    }



}


