package com.example.keep_exploring.animations;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.keep_exploring.R;

public class Anim_Bottom_Navigation implements View.OnTouchListener {
    private GestureDetector gestureDetector;

    public Anim_Bottom_Navigation(Context context, View viewAnim) {
        gestureDetector = new GestureDetector(context, new SimpleGestureDetector(viewAnim));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class SimpleGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private View viewAnim;
        private boolean isFinishAnim = true;

        public SimpleGestureDetector(View viewAnim) {
            this.viewAnim = viewAnim;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY > 0) {
                hiddenView();
            } else {
                showView();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }


        private void hiddenView() {
            if (viewAnim == null || viewAnim.getVisibility() == View.GONE) {
                return;
            }
            Animation animDown = AnimationUtils.loadAnimation(viewAnim.getContext(), R.anim.bottom_navigation_down);
            animDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewAnim.setVisibility(View.VISIBLE);
                    isFinishAnim = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    viewAnim.setVisibility(View.GONE);
                    isFinishAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (isFinishAnim) {
                viewAnim.startAnimation(animDown);
            }
        }

        private void showView() {
            if (viewAnim == null || viewAnim.getVisibility() == View.VISIBLE) {
                return;
            }
            Animation animUp = AnimationUtils.loadAnimation(viewAnim.getContext(), R.anim.bottom_navigation_up);
            animUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewAnim.setVisibility(View.VISIBLE);
                    isFinishAnim = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isFinishAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (isFinishAnim) {
                viewAnim.startAnimation(animUp);
            }
        }

    }
}
