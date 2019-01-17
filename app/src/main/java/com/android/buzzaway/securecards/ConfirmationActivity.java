package com.android.buzzaway.securecards;

import android.animation.Animator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView animatedLogo;
    private TextView status;
    private static int animationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        status = findViewById(R.id.status);
        animatedLogo = findViewById(R.id.logo);
        VectorDrawable progress = (VectorDrawable) getResources().getDrawable(R.drawable.time);
        animatedLogo.setImageDrawable(progress);
        ViewPropertyAnimator animator = animatedLogo.animate();
        animator.rotation(360.0f).setDuration(2000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animationCount < 2) {
                    float rotation = animatedLogo.getRotation();
                    animatedLogo.animate().rotation(rotation == 360.0f ? 0.0f : 360.0f).setDuration(2000).start();
                } else {
                    AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.finaldone);
                    animatedLogo.setImageDrawable(animatedVectorDrawable);
                    animatedVectorDrawable.start();
                    animatedLogo.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            status.setVisibility(View.VISIBLE);
                        }
                    }, 1500);
                }
                animationCount++;
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        animator.start();
    }
}
