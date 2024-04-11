package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.pages.LoginPage;
import com.google.firebase.FirebaseApp;
//goat

public class MainActivity extends AppCompatActivity {
    private static int  SPLASH_SCREEN = 5000;
    Animation bottomAnim, topAnim;
    ImageView splashScreenImage;
    TextView logo,motto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        splashScreenImage = findViewById(R.id.splashScreenImage);
        logo = findViewById(R.id.logoText);
        motto = findViewById(R.id.motto);

        splashScreenImage.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        motto.setAnimation(bottomAnim);

        // wow wow
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                Pair[] pairs = new Pair[2];
                pairs[0] =  new Pair<View,String>(splashScreenImage,"splashScreenImaageAnim");
                pairs[1]  = new Pair<View,String>(logo,"logoText_Animation");

                ActivityOptions  opt =  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent, opt.toBundle());
                finish();

            }
        },SPLASH_SCREEN);

    }


}