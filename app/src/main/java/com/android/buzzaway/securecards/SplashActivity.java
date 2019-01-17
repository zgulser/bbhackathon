package com.android.buzzaway.securecards;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private ImageView animatedLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideSystemUI();

        animatedLogo = findViewById(R.id.logo);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.splashlogo3);
        animatedLogo.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.start();
        animatedLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
               animatedLogo.animate().scaleX(1/100.0f).scaleY(1/100.0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                   @Override
                   public void onAnimationStart(Animator animator) {
                   }

                   @Override
                   public void onAnimationEnd(Animator animator) {
                       Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                       startActivity(intent);
                   }

                   @Override
                   public void onAnimationCancel(Animator animator) {

                   }

                   @Override
                   public void onAnimationRepeat(Animator animator) {

                   }
               }).start();
            }
        }, 3200);
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}
