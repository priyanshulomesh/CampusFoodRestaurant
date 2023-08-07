package com.tiet.campusfoodadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    private ImageView logo;
    private TextView title,tagline;
    private Animation top,bottom;

    private static final int SPLASH_SCREEN_TIME_OUT=1500;
    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //to hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logo=findViewById(R.id.splash_logo);
        title=findViewById(R.id.splash_title);
        tagline=findViewById(R.id.splash_tagline);
        top= AnimationUtils.loadAnimation(this,R.anim.top);
        bottom=AnimationUtils.loadAnimation(this,R.anim.bottom);

        animate();
        postSplash();
    }
    void animate(){
        title.setAnimation(bottom);
        tagline.setAnimation(bottom);
        logo.setAnimation(top);
    }
    void postSplash(){


        new Handler().postDelayed(()->{
            Intent i = new Intent(Splash.this, LoginPage.class);
            //For continuity of transition from splash screen to login page
            Pair[] pair=new Pair[2];
            pair[0]=new Pair<View,String>(logo,"logo_image");
            pair[1]=new Pair<View,String>(title,"logo_text");

            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Splash.this,pair);
            //For continuity of transition from splash screen to login page

            startActivity(i,options.toBundle());
            finish();
        },SPLASH_SCREEN_TIME_OUT);

    }
}