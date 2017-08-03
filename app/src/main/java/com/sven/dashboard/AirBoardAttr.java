package com.sven.dashboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;


/**
 * 文件描述：空调面板属性管理
 * <p>
 * 作者：   Created by Sven on 2017/7/21 0021.
 */
public class AirBoardAttr {

    //中心圆
    private Float centerCircleRadius;
    private int centerCircleBackground;

    //进度条
    private int progressWidth;
    private int progressStartColor;
    private int progressCenterColor;
    private int progressEndColor;
    private int progressBackgroundColor;

    //外圆
    private Float outsideCircleRadius;
    private int outsideCircleBackgroundColor;

    //温度文字
    private int tempTextColor;
    private float tempTextSize;

    //刻度文字
    private int scaleTextColor;
    private float scaleTextSize;

    private CharSequence[] tmepStrArray;



    public AirBoardAttr(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AirBoardView, defStyleAttr, 0);
        centerCircleRadius = ta.getDimension(R.styleable.AirBoardView_centerCircleRadius, DensityUtils.dip2px(60));
        centerCircleBackground = ta.getColor(R.styleable.AirBoardView_centerCircleBackground, ContextCompat.getColor(context, R.color.white));

        progressWidth = (int) ta.getDimension(R.styleable.AirBoardView_progressWidth, DensityUtils.dip2px(25));
        progressStartColor = ta.getColor(R.styleable.AirBoardView_progressStartColor, ContextCompat.getColor(context, R.color.startColor));
        progressCenterColor = ta.getColor(R.styleable.AirBoardView_progressCenterColor, ContextCompat.getColor(context, R.color.centerColor));
        progressEndColor = ta.getColor(R.styleable.AirBoardView_progressEndColor, ContextCompat.getColor(context, R.color.endColor));
        progressBackgroundColor = ta.getColor(R.styleable.AirBoardView_progressBackgroundColor, ContextCompat.getColor(context, R.color.progressBackgroundColor));

        outsideCircleRadius = ta.getDimension(R.styleable.AirBoardView_outsideCircleRadius, DensityUtils.dip2px(145));
        outsideCircleBackgroundColor = ta.getColor(R.styleable.AirBoardView_outsideCircleBackgroundColor, ContextCompat.getColor(context, R.color.outCircleColor));

        tempTextColor = ta.getColor(R.styleable.AirBoardView_tempTextColor, ContextCompat.getColor(context, R.color.tempTextColor));
        tempTextSize = ta.getDimension(R.styleable.AirBoardView_tempTextSize, 24);

        scaleTextColor = ta.getColor(R.styleable.AirBoardView_scaleTextColor, ContextCompat.getColor(context, R.color.tempTextColor));
        scaleTextSize = ta.getDimension(R.styleable.AirBoardView_scaleTextSize, 10);

        tmepStrArray = ta.getTextArray(R.styleable.AirBoardView_tempStrArray);
        ta.recycle();
    }

    public Float getCenterCircleRadius() {
        return centerCircleRadius;
    }

    public void setCenterCircleRadius(Float centerCircleRadius) {
        this.centerCircleRadius = centerCircleRadius;
    }

    public int getCenterCircleBackground() {
        return centerCircleBackground;
    }

    public void setCenterCircleBackground(int centerCircleBackground) {
        this.centerCircleBackground = centerCircleBackground;
    }

    public int getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    public int getProgressStartColor() {
        return progressStartColor;
    }

    public void setProgressStartColor(int progressStartColor) {
        this.progressStartColor = progressStartColor;
    }

    public int getProgressCenterColor() {
        return progressCenterColor;
    }

    public void setProgressCenterColor(int progressCenterColor) {
        this.progressCenterColor = progressCenterColor;
    }

    public int getProgressEndColor() {
        return progressEndColor;
    }

    public void setProgressEndColor(int progressEndColor) {
        this.progressEndColor = progressEndColor;
    }

    public int getProgressBackgroundColor() {
        return progressBackgroundColor;
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
    }

    public Float getOutsideCircleRadius() {
        return outsideCircleRadius;
    }

    public void setOutsideCircleRadius(Float outsideCircleRadius) {
        this.outsideCircleRadius = outsideCircleRadius;
    }

    public int getOutsideCircleBackgroundColor() {
        return outsideCircleBackgroundColor;
    }

    public void setOutsideCircleBackgroundColor(int outsideCircleBackgroundColor) {
        this.outsideCircleBackgroundColor = outsideCircleBackgroundColor;
    }

    public int getTempTextColor() {
        return tempTextColor;
    }

    public void setTempTextColor(int tempTextColor) {
        this.tempTextColor = tempTextColor;
    }

    public float getTempTextSize() {
        return tempTextSize;
    }

    public void setTempTextSize(float tempTextSize) {
        this.tempTextSize = tempTextSize;
    }

    public int getScaleTextColor() {
        return scaleTextColor;
    }

    public void setScaleTextColor(int scaleTextColor) {
        this.scaleTextColor = scaleTextColor;
    }

    public float getScaleTextSize() {
        return scaleTextSize;
    }

    public void setScaleTextSize(float scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
    }


    public CharSequence[] getTmepStrArray() {
        return tmepStrArray;
    }

    public void setTmepStrArray(CharSequence[] tmepStrArray) {
        this.tmepStrArray = tmepStrArray;
    }
}
