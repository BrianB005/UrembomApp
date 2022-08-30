package com.brianbett.urembom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CardView topView=findViewById(R.id.top_part);
        CardView bottomView=findViewById(R.id.bottom_part);

        topView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_ttb));
        bottomView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_btt));

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        },3000);
    }
}