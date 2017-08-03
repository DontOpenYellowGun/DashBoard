package com.sven.dashboard;


import android.view.animation.Interpolator;

/**
 * 文件描述：空调面板动画插值器
 * <p>
 * 作者：   Created by Sven on 2017/7/21 0021.
 */
public class SpringInterpolator implements Interpolator {
    private final float mTension;

    public SpringInterpolator() {
        mTension = 0.4f;

    }

    public SpringInterpolator(float tension) {
        mTension = tension;
    }

    @Override
    public float getInterpolation(float input) {
        float result = (float) (Math.pow(2, -10 * input) *
                Math.sin((input - mTension / 4) * (2 * Math.PI) / mTension) + 1);
        return result;
    }
}
