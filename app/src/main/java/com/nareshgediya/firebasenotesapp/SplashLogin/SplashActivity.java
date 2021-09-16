package com.nareshgediya.firebasenotesapp.SplashLogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nareshgediya.firebasenotesapp.MainActivity;
import com.nareshgediya.firebasenotesapp.R;

public class SplashActivity extends AppCompatActivity {
    View logo, logoName, image;
    FirebaseAuth auth;
    FirebaseUser user;
    View lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Util.blackIconStatusBar(SplashActivity.this, R.color.dark_background);

        logo = findViewById(R.id.imageView);
        logoName = findViewById(R.id.textView);
        lottieAnimationView = findViewById(R.id.lottie);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user != null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, Pair.create(logo, "logo"),
                            Pair.create(logoName, "textView"));
                    startActivity(intent, activityOptions.toBundle());
                    finish();
                }

            }
        }, 3500);
    }
}