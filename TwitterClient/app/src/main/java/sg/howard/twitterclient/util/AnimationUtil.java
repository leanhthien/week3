package sg.howard.twitterclient.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;
import sg.howard.twitterclient.R;


public class AnimationUtil {

    final static AccelerateInterpolator ACCELERATE = new AccelerateInterpolator();
    final static AccelerateDecelerateInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    final static DecelerateInterpolator DECELERATE = new DecelerateInterpolator();
    View view;
    float startBtnX;
    float startBtnY;
    int endBtnX;
    int endBtnY;
    int startCoverBottom;
    @Nullable
    FloatingActionButton button;
    @Nullable
    FrameLayout cover;

    public AnimationUtil(View view) {

        this.view = view;
        button = view.findViewById(R.id.fab);
        cover = view.findViewById(R.id.cover);

    }

    public void moveLeft() {

        startBtnX = centerX(button);
        startBtnY = centerY(button);
        endBtnX = centerX(view);
        endBtnY = centerY(view);

        ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(button, endBtnX,
                endBtnY, 90, Side.LEFT)
                .setDuration(500);
        arcAnimator.addListener(new SimpleListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                button.setVisibility(View.INVISIBLE);
                appear();
                }
        });
        arcAnimator.start();

    }

    private void appear() {

        cover.setVisibility(View.VISIBLE);

        float finalRadius = Math.max(cover.getWidth(), cover.getHeight()) * 1.5f;

        Animator circularAnimator = ViewAnimationUtils.createCircularReveal(cover, endBtnX, endBtnY, button.getWidth() / 2f,
                finalRadius);
        circularAnimator.setDuration(500);
        circularAnimator.setInterpolator(ACCELERATE);
        circularAnimator.addListener(new SimpleListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                raise();
            }
        });
        circularAnimator.start();


    }

    private void raise() {

        startCoverBottom = cover.getBottom();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(cover, "bottom", cover.getBottom(), cover.getTop());
        objectAnimator.setInterpolator(ACCELERATE_DECELERATE);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new SimpleListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                rearrange();
            }
        });
        objectAnimator.start();
    }

    private void rearrange() {

        ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(button, startBtnX,
                startBtnY, 90, Side.LEFT);
                arcAnimator.start();
    }

    private static int centerX(View view) {
        return Math.round(view.getX() + view.getWidth() / 2);
    }

    private static int centerY(View view) {
        return Math.round(view.getY() + view.getHeight() / 2);
    }


    private static class SimpleListener implements Animator.AnimatorListener,
            com.nineoldandroids.animation.Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

        }

        @Override
        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {

        }

        @Override
        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

        }

        @Override
        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

        }
    }
}
