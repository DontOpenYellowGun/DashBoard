package com.sven.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * 文件描述：空调控制面板View
 * <p>
 * 作者：   Created by Sven on 2017/8/3 0003.
 */

public class AirBoardView extends View {

    private AirBoardAttr attr;
    private Context mContext;

    //中心圆
    Float centerCircleRadius;
    int centerCircleBackground;

    //进度条
    int progressWidth;
    int progressStartColor;
    int progressCenterColor;
    int progressEndColor;
    int progressBackgroundColor;

    //外圆
    Float outsideCircleRadius;
    int outsideCircleBackgroundColor;

    //温度文字
    int tempTextColor;
    float tempTextSize;

    //刻度文字
    int scaleTextColor;
    float scaleTextSize;

    //画笔
    private Paint paintCenterCircle;
    private Paint paintProgress;
    private Paint paintProgressBackground;
    private Paint paintOutCircle;
    private Paint paintTempText;
    private Paint paintScaleText;
    private RectF rectF;

    private int mWidth, mHeight;
    private float percent = 1.0f;
    private float oldPercent = 0f;
    private int START_ARC = 150;
    private int DURING_ARC = 240;
    private int OFFSET = 30;
    private CharSequence[] tempStrArray = null;
    private int scaleCount;//刻度的个数
    private ValueAnimator valueAnimator;
    private long animatorDuration;
    TimeInterpolator interpolator = new SpringInterpolator();//动画插值器
    private int mCenterCircleX;
    private int mCenterCircleY;
    private String currentTemp;


    public AirBoardView(Context context) {
        this(context, null);
        init(context);
    }

    public AirBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public AirBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attr = new AirBoardAttr(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initAttr();
        initPaint();
        initData();
    }

    private void initData() {


    }


    private void initAttr() {
        //中心圆
        centerCircleRadius = attr.getCenterCircleRadius();
        centerCircleBackground = attr.getCenterCircleBackground();

        //进度条
        progressWidth = attr.getProgressWidth();
        progressStartColor = attr.getProgressStartColor();
        progressCenterColor = attr.getProgressCenterColor();
        progressEndColor = attr.getProgressEndColor();
        progressBackgroundColor = attr.getProgressBackgroundColor();

        //外圆
        outsideCircleRadius = attr.getOutsideCircleRadius();
        outsideCircleBackgroundColor = attr.getOutsideCircleBackgroundColor();

        //温度文字
        tempTextColor = attr.getTempTextColor();
        tempTextSize = attr.getTempTextSize();

        //刻度文字
        scaleTextColor = attr.getScaleTextColor();
        scaleTextSize = attr.getScaleTextSize();

        //温度
        tempStrArray = attr.getTmepStrArray();
        if (tempStrArray != null && tempStrArray.length != 0) {
            //根据需要绘制的刻度数组大小计算刻度总数
            scaleCount = tempStrArray.length - 1;
        } else {
            tempStrArray = new String[0];
            scaleCount = 7;
        }
    }


    private void initPaint() {
        paintOutCircle = new Paint();
        paintOutCircle.setAntiAlias(true);
        paintOutCircle.setStyle(Paint.Style.FILL);
        paintOutCircle.setStrokeWidth(2);
        paintOutCircle.setColor(outsideCircleBackgroundColor);
        paintOutCircle.setDither(true);

        paintCenterCircle = new Paint();
        paintCenterCircle.setAntiAlias(true);
        paintCenterCircle.setStyle(Paint.Style.FILL);
        paintCenterCircle.setStrokeWidth(2);
        paintCenterCircle.setColor(centerCircleBackground);
        paintCenterCircle.setDither(true);

        paintProgressBackground = new Paint();
        paintProgressBackground.setAntiAlias(true);
        paintProgressBackground.setStrokeWidth(progressWidth);
        paintProgressBackground.setStyle(Paint.Style.STROKE);
        paintProgressBackground.setStrokeCap(Paint.Cap.ROUND);
        paintProgressBackground.setColor(progressBackgroundColor);
        paintProgressBackground.setDither(true);

        paintProgress = new Paint();
        paintProgress = new Paint();
        paintProgress.setAntiAlias(true);
        paintProgress.setStrokeWidth(progressWidth);
        paintProgress.setStyle(Paint.Style.STROKE);
        paintProgress.setStrokeCap(Paint.Cap.ROUND);
        paintProgress.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        paintProgress.setDither(true);


        paintScaleText = new Paint();
        paintScaleText.setAntiAlias(true);
        paintScaleText.setColor(scaleTextColor);
        paintScaleText.setStrokeWidth(4);
        paintScaleText.setStyle(Paint.Style.FILL);
        paintScaleText.setDither(true);

        paintTempText = new Paint();
        paintTempText.setAntiAlias(true);
        paintTempText.setColor(tempTextColor);
        paintTempText.setStrokeWidth(1);
        paintTempText.setStyle(Paint.Style.FILL);//实心画笔
        paintTempText.setDither(true);
        paintTempText.setTextSize(tempTextSize * 2.5f);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int realWidth = startMeasure(widthMeasureSpec);
        int realHeight = startMeasure(heightMeasureSpec);
        setMeasuredDimension(realWidth, realHeight);
    }

    private int startMeasure(int msSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(msSpec);
        int size = MeasureSpec.getSize(msSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (outsideCircleRadius * 2);
        }
        return result;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        percent = oldPercent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("onDraw", "");

        mCenterCircleX = (getWidth() + getPaddingLeft() - getPaddingRight()) / 2;
        mCenterCircleY = (getHeight() + getPaddingTop() - getPaddingBottom()) / 2;

        this.percent = percent / 100f;
        canvas.translate(mWidth / 2, mHeight / 2);//移动坐标原点到中心

        drawOutCircle(canvas);//画外圆
        drawCenterCircle(canvas);//画外圆
        drawProgressBackground(canvas);//画进度条背景
        drawProgress(canvas, percent);//画进度条
        drawScale(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX() - (mWidth / 2);
            float y = event.getY() - (mHeight / 2);
            float touchAngle = 0;
            if (x < 0 && y < 0) {
                touchAngle += 180;
            } else if (y < 0 && x > 0) {
                touchAngle += 360;
            } else if (y > 0 && x < 0) {
                touchAngle += 180;
            }
            Log.e("onTouchEvent", "touchAngle___1: " + touchAngle);
            touchAngle += Math.toDegrees(Math.atan(y / x));
            Log.e("onTouchEvent", "Math.atan(y/x: " + Math.atan(y / x));
            Log.e("onTouchEvent", "touchAngle___2: " + touchAngle);
            touchAngle = touchAngle - START_ARC;
            Log.e("onTouchEvent", "touchAngle___3: " + touchAngle);
            if (touchAngle < 0) {
                touchAngle = touchAngle + 360;
            }
            float touchRadius = (float) Math.sqrt(y * y + x * x);//点击点距离圆心得距离
            if (touchRadius < outsideCircleRadius) {
                getCurrentTemp(touchAngle);
            }
            Log.e("onTouchEvent", "原始坐标__: " /*+ b*/ + " x: " + event.getX() + "  y: " + event.getX());
            Log.e("onTouchEvent", "________: " /*+ b*/ + " x: " + x + "  y: " + y);
            Log.e("onTouchEvent", "touchAngle___4: " /*+ b*/ + " touchAngle___: " + touchAngle);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (onAirClickListener != null) {
                onAirClickListener.onAirClick(currentTemp);
            }
        }
        return true;
    }

    private void getCurrentTemp(float touchAngle) {

        int SPACE_COUNT = tempStrArray.length + 1;//一共多少个区间
        int SPACE_ARC = DURING_ARC / (SPACE_COUNT);//每个区间的的角度

        float[] spaces = new float[SPACE_COUNT + 1];//所有的区间角度
        float f = 0;
        spaces[0] = 0;
        for (int i = 1; i < SPACE_COUNT + 1; i++) {
            f += SPACE_ARC;
            spaces[i] = f;
        }
        for (int i = 0; i < SPACE_COUNT; i++) {
            if (touchAngle > spaces[i] && touchAngle < spaces[i + 1]) {
                if (i < 14) {
                    currentTemp = String.valueOf(tempStrArray[i]);
                    percent = (i / (tempStrArray.length - 1 + 1f)) * 100;
                    invalidate();
                } else if (i == 14) {
                    currentTemp = String.valueOf(tempStrArray[14]);
                    percent = 100;
                    invalidate();
                }
                Log.e("currentTemp", currentTemp);
            }
        }
    }

    private void setAnimator(final float percent) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }

        animatorDuration = (long) Math.abs(percent - oldPercent) * 20;

        valueAnimator = ValueAnimator.ofFloat(oldPercent, percent).setDuration(animatorDuration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                AirBoardView.this.percent = (float) animation.getAnimatedValue();
                invalidate();
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                oldPercent = percent;

                //防止因为插值器误差产生越界
                if (AirBoardView.this.percent < 0.0) {
                    AirBoardView.this.percent = 0.0f;
                    invalidate();
                }
                if (AirBoardView.this.percent > 100.0) {
                    AirBoardView.this.percent = 100.0f;
                    invalidate();
                }
            }
        });
        valueAnimator.start();
    }

    private void drawScale(Canvas canvas) {
        canvas.save(); //记录画布状态
        canvas.rotate(-(180 - START_ARC + 90), 0, 0);
        int numY = -((mHeight / 2)) + DensityUtils.dip2px(22);
        float rAngle = DURING_ARC / tempStrArray.length + 1; //每个区间需要转动的角度
        for (int i = 0; i < tempStrArray.length; i++) {
            canvas.save(); //记录画布状态
            canvas.rotate(rAngle * i, 0, 0);
            if (i % 2 == 0) {
                if (i != 0 || i != tempStrArray.length + 1) {
                    //画温度文字
                    String text = tempStrArray[i].toString();
                    Paint.FontMetricsInt fontMetrics = paintTempText.getFontMetricsInt();
                    int baseline = ((numY) + (fontMetrics.bottom - fontMetrics.top) / 2);
                    canvas.drawText(text, -getTextViewLength(paintTempText, text) / 2, baseline, paintTempText);
                }
            } else {
                canvas.drawLine(0, numY + 5, 0, numY + 40, paintScaleText);//画短刻度线
            }
            canvas.restore();
        }
        canvas.restore();
    }

    private float getTextViewLength(Paint paint, String text) {
        if (TextUtils.isEmpty(text)) return 0;
        float textLength = paint.measureText(text);
        return textLength;
    }

    private void drawProgress(Canvas canvas, float percent) {
        if (percent > 1.0f) {
            percent = 1.0f; //限制进度条在弹性的作用下不会超出
        }
        if (!(percent <= 0.0f)) {
            canvas.drawArc(rectF, START_ARC, percent * DURING_ARC, false, paintProgress);
        }
    }


    private void updateOval() {
        float v = centerCircleRadius + progressWidth / 2f;
        rectF = new RectF((-v) + getPaddingLeft(), getPaddingTop() - (v),
                (v) - getPaddingRight(),
                (v) - getPaddingBottom());
    }

    private void initShader() {
        updateOval();
        if (progressStartColor != 0 && progressCenterColor != 0 && progressEndColor != 0) {
            SweepGradient shader = new SweepGradient(0, 0, new int[]{progressStartColor, progressCenterColor, progressEndColor}, null);
            float rotate = 90f;
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(rotate, 0, 0);
            shader.setLocalMatrix(gradientMatrix);
            paintProgress.setShader(shader);
        }
    }

    private void drawProgressBackground(Canvas canvas) {
        float v = centerCircleRadius + progressWidth / 2f;
        Log.e("drawProgressBackground", String.valueOf(v));
        canvas.drawCircle(0, 0, v, paintProgressBackground);
    }


    private void drawCenterCircle(Canvas canvas) {
        Log.e("drawCenterCircle", String.valueOf(centerCircleRadius));
        canvas.drawCircle(0, 0, centerCircleRadius, paintCenterCircle);
        canvas.save();
    }

    private void drawOutCircle(Canvas canvas) {
        Log.e("drawOutCircle", String.valueOf(outsideCircleRadius));
        canvas.drawCircle(0, 0, outsideCircleRadius, paintOutCircle);
        canvas.save();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged", String.valueOf(w) + String.valueOf(h) + String.valueOf(oldw) + String.valueOf(oldh));
        mWidth = w;
        mHeight = h;
        initShader();
    }


    public void setPercent(int percent) {
        setAnimator(percent);
    }

    private OnAirClickListener onAirClickListener;

    public interface OnAirClickListener {
        void onAirClick(String temp);
    }

    public OnAirClickListener getOnAirClickListener() {
        return onAirClickListener;
    }

    public void setOnAirClickListener(OnAirClickListener onAirClickListener) {
        this.onAirClickListener = onAirClickListener;
    }
}
