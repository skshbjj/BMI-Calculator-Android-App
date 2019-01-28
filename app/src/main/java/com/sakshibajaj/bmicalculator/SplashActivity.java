package com.sakshibajaj.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView tvText;
    ImageView ivBG;
    Animation animation1, animation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvText = findViewById(R.id.tvText);
        ivBG = findViewById(R.id.ivBG);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.a1);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.a2);
        ivBG.startAnimation(animation1);
        tvText.startAnimation(animation2);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent a = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(a);
                finish();

            }
        }).start();




    }
}
