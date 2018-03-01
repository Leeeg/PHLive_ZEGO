package com.i5i58.live.widget.animation;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by lee on 2014/7/31.
 */
public class FlipV extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", -90, 0).setDuration(mDuration)

        );
    }
}
