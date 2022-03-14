package com.example.khitta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {

    Animation Topanimation,bottomanimation;
    TextView tv;
    ImageView logo;

    private static int SPLASH_SCREEN=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams
                .FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Topanimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        logo=findViewById(R.id.logo);
        logo.setAnimation(Topanimation);

        bottomanimation= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        tv=findViewById(R.id.tag);
        tv.setAnimation(bottomanimation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}