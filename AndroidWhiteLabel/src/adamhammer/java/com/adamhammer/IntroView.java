package com.adamhammer;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2/27/2016.
 */
public class IntroView extends ImageView implements Animator.AnimatorListener{


    public IntroView(Context context) {
        super(context);

    }

    public IntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    ValueAnimator valueAnimator;
    @Override
    public void setVisibility(int visibility) {

        if (visibility == View.GONE && getVisibility() == View.VISIBLE) {
            valueAnimator = ValueAnimator.ofFloat(getHeight(),0);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewGroup.LayoutParams lp = getLayoutParams();
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    setLayoutParams(lp);
                }

            });
            valueAnimator.addListener((Animator.AnimatorListener) IntroView.this);


            valueAnimator.start();

        } else {
            super.setVisibility(visibility);
        }

    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        super.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
