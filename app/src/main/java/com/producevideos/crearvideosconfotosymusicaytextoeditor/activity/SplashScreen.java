package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;



public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, StartActivity.class);
                startActivity(i);
            }
        }, SPLASH_TIME_OUT);
    }
}
