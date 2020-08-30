package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import iss.workshop.adprojectmobile.R;

public class SplashScreen extends AppCompatActivity {

    TextView tag, team8;
    ImageView logo;

    //animation variables
    Animation tagAnim, team8Anim, splashScreenAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //animations
        tagAnim = AnimationUtils.loadAnimation(this, R.anim.slid_in_right);
        team8Anim = AnimationUtils.loadAnimation(this, R.anim.drop_from_top);
        splashScreenAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        //animation hooks
        tag = findViewById(R.id.tag);
        team8 = findViewById(R.id.team8);
        logo = findViewById(R.id.logo);

        tag.setAnimation(tagAnim);
        team8.setAnimation(team8Anim);
        logo.setAnimation(splashScreenAnim);

        //splash screen
        //time-interval for the welcome screen
        int SPLASH_TIME_OUT = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}