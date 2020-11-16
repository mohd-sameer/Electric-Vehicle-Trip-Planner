package com.example.seccharge.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.seccharge.R;
import com.example.seccharge.navigation_drawer;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager pager;
    IntroViewPagerAdapter introPagerAdapter ;
    TabLayout tabInd;
    Button nextButton;
    int position = 0 ;
    Button startButton;
    Animation animButton ;
    TextView skip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_intro);
        nextButton = findViewById(R.id.btn_next);
        startButton = findViewById(R.id.btn_get_started);
        tabInd = findViewById(R.id.tab_indicator);
        animButton = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        skip = findViewById(R.id.skip);
        
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Easy Booking","Under Development",R.drawable.img1));
        mList.add(new ScreenItem("Station Finder","Under Development",R.drawable.img2));
        mList.add(new ScreenItem("Easy Payment","Under Development",R.drawable.img3));
        
        pager =findViewById(R.id.screen_viewpager);
        introPagerAdapter = new IntroViewPagerAdapter(this,mList);
        pager.setAdapter(introPagerAdapter);


        tabInd.setupWithViewPager(pager);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = pager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    pager.setCurrentItem(position);
                }
                if (position == mList.size()-1) {
                    lastScreen();
                }
            }
        });


        tabInd.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    lastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navDrawer = new Intent(getApplicationContext(), navigation_drawer.class);
                startActivity(navDrawer);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(mList.size());
            }
        });
    }
    
    private void lastScreen() {
        nextButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        skip.setVisibility(View.INVISIBLE);
        tabInd.setVisibility(View.INVISIBLE);
        startButton.setAnimation(animButton);
    }
}
