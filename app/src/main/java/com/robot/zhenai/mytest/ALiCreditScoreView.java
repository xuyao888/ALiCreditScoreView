package com.robot.zhenai.mytest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ALiCreditScoreView extends View {

    private Context mContext;

    private Paint backPaint;
    private Paint outProgressPaint;
    private Paint numPaint;
    private Paint levelPaint;
    private Paint linePaint;
    private int outRadius = 0;
    private int innerRadius = 0;
    private int outPaintWidth = 0;
    private int innerPaintWidth = 0;

    private float TOTAL_ANGLE = 225.f;
    private int height;
    private int width;
    private int score = 690;
    private int colors[] = {0xff7fe6ff, 0xffffffff};

    private int smallCircleRadius = 0;

    private int bigLineWidth = 0;
    private int smallLineWidth = 0;

    private int lineNum = 31;
    private int scores[] = {350, 550, 600, 650, 700, 950};
    private final static String CREDIT_LEVEL[] = {"较差", "中等", "良好", "优秀", "极好"};
    private float numWidth = 0;
    private float numHeight = 0;

    private float numGapWidth = 0;
    private float levelWidth;
    private float levelHeight;

    public ALiCreditScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        initDatas();
        backPaint = new Paint();
        backPaint.setColor(Color.WHITE);
        backPaint.setAntiAlias(true);
        backPaint.setAlpha(100);
        backPaint.setStrokeWidth(outPaintWidth);
        backPaint.setStyle(Paint.Style.STROKE);

        outProgressPaint = new Paint();
        outProgressPaint.setAntiAlias(true);
        outProgressPaint.setStrokeWidth(outPaintWidth);
        outProgressPaint.setStyle(Paint.Style.STROKE);
        outProgressPaint.setShader(new SweepGradient(outRadius, outRadius, colors, null));

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dp2px(mContext, 2));
        linePaint.setColor(Color.WHITE);

        numPaint = new Paint();
        numPaint.setColor(Color.WHITE);
        numPaint.setTextSize(dp2px(mContext, 10));
        numWidth = numPaint.measureText(scores[0] + "");
        numHeight = gettextTopHeight(scores[0] + "", numPaint);


        levelPaint = new Paint();
        levelPaint.setColor(Color.WHITE);
        levelPaint.setTextSize(dp2px(mContext, 10));
        levelWidth = levelPaint.measureText(CREDIT_LEVEL[0]);
        levelHeight = gettextTopHeight(CREDIT_LEVEL[0], levelPaint);
    }

    private void initDatas() {
        smallCircleRadius = dp2px(mContext, 4);
        outRadius = dp2px(mContext, 110);
        innerRadius = dp2px(mContext, 100);
        outPaintWidth = dp2px(mContext, 4);
        innerPaintWidth = dp2px(mContext, 8);
        bigLineWidth = dp2px(mContext, 10);
        smallLineWidth = dp2px(mContext, 8);
        numGapWidth = dp2px(mContext, 4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measueWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measueHeight = MeasureSpec.getSize(heightMeasureSpec);

        //自己测量
        if (widthMode != MeasureSpec.EXACTLY) {
            width = getPaddingLeft() + getPaddingRight() + 2 * outRadius + smallCircleRadius * 2;
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = (int) (getPaddingTop() + getPaddingBottom() + outRadius + Math.sin(Math.toRadians(22.5)) * outRadius) + smallCircleRadius;
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measueWidth : width,
                heightMode == MeasureSpec.EXACTLY ? measueHeight : height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#0f9af7"));
//        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.translate(getPaddingLeft() + outRadius + smallCircleRadius, getPaddingTop() + outRadius + smallCircleRadius);
        canvas.drawCircle(0,0,5,numPaint);
        // 画背景进度
        drawBackProgress(canvas);
        //画外度进度圆弧
        drawOutProgress(canvas);
        //画外进度圆球
        drawOutProgressCircle(canvas);

        //画内部圆弧
        drawInnerProgress(canvas);

        //画内部刻度 画对应文字
        drawInerLine(canvas);

        //画文字和数字
        drawNumAndText(canvas);

    }

    private void drawNumAndText(Canvas canvas) {

        canvas.save();
        canvas.rotate(-112.5f);
        float eachAngle = TOTAL_ANGLE / (lineNum - 1);
        for (int i = 0; i < lineNum; i++) {
            if (i != 0)
                canvas.rotate(eachAngle);
            if (i % 6 == 0) {
                canvas.drawText(scores[i / 6] + "", -numWidth / 2, -(innerRadius - bigLineWidth - numGapWidth - numHeight), numPaint);

            } else {
                if (i % 3 == 0 && i % 6 != 0) {//中文文字
                    canvas.drawText(CREDIT_LEVEL[i / 6], -levelWidth / 2, -(innerRadius - bigLineWidth - numGapWidth - levelHeight), levelPaint);
                }
            }
        }
        canvas.restore();
    }

    int stopX = 0;

    private void drawInerLine(Canvas canvas) {
        linePaint.setAlpha(150);
        canvas.save();

        canvas.rotate(157.5f);
        float eachAngle = TOTAL_ANGLE / (lineNum - 1);
        for (int i = 0; i < lineNum; i++) {
            if (i != 0)
                canvas.rotate(eachAngle);
            if (i % 6 == 0) {//长线
                stopX = innerRadius - bigLineWidth;
//                canvas.drawText(scores[i / 6] + "", innerRadius - bigLineWidth - numGapWidth - numWidth, numHeight / 2, numPaint);

            } else {
//                if (i % 3 == 0 && i % 6 != 0) {//中文文字
//                    canvas.save();
//                    canvas.rotate(90, getWidth() / 2f, getHeight() / 2f);
//                    canvas.drawText(CREDIT_LEVEL[i / 6], innerRadius - bigLineWidth - numGapWidth - levelWidth, levelHeight / 2, numPaint);
//                    canvas.restore();
//                }
                stopX = innerRadius - smallLineWidth;
            }
            canvas.drawLine(innerRadius, 0, stopX, 0, linePaint);
        }
        canvas.restore();
    }

    private void drawInnerProgress(Canvas canvas) {
        backPaint.setStyle(Paint.Style.STROKE);
        backPaint.setStrokeWidth(innerPaintWidth);
        backPaint.setAlpha(50);
        canvas.save();

//        canvas.translate(getPaddingLeft() + outRadius + smallCircleRadius, getPaddingTop() + outRadius + smallCircleRadius);
        float rectRadius = innerRadius - innerPaintWidth / 2;
        canvas.drawArc(new RectF(-rectRadius, -rectRadius, rectRadius, rectRadius), 157.5f, TOTAL_ANGLE, false, backPaint);
        canvas.restore();

    }

    private void drawOutProgressCircle(Canvas canvas) {
//        float sx = (float) (outRadius - Math.sin(Math.toRadians(TOTAL_ANGLE/2 - getAngleToScore(score))) * outRadius);
//        float sy = (float) (outRadius - Math.cos(Math.toRadians(getAngleToScore(score))) * outRadius);
//        canvas.drawCircle(0, 0, smallCircleRadius, backPaint);
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(Color.WHITE);
        canvas.save();

//        canvas.translate(getPaddingLeft() + outRadius + smallCircleRadius, getPaddingTop() + outRadius + smallCircleRadius);
        canvas.rotate(67.5f + getAngleToScore(score));
        canvas.drawCircle(0, outRadius - outPaintWidth / 2, smallCircleRadius, backPaint);
        canvas.restore();
    }

    private void drawOutProgress(Canvas canvas) {
        canvas.save();

//        canvas.translate(getPaddingLeft() + outRadius + smallCircleRadius, getPaddingTop() + outRadius + smallCircleRadius);
        float rectRadius = outRadius - outPaintWidth / 2;
        canvas.drawArc(new RectF(-rectRadius, -rectRadius, rectRadius, rectRadius), 157.5f, getAngleToScore(score), false, outProgressPaint);
        canvas.restore();

    }

    private void drawBackProgress(Canvas canvas) {
        canvas.save();
        backPaint.setAlpha(50);

//        canvas.translate(getPaddingLeft() + outRadius + smallCircleRadius, getPaddingTop() + outRadius + smallCircleRadius);
        float rectRadius = outRadius - outPaintWidth / 2;
        canvas.drawArc(new RectF(-rectRadius, -rectRadius, rectRadius, rectRadius), 157.5f, TOTAL_ANGLE, false, backPaint);
        canvas.restore();
    }

    /**
     * dp转px
     *
     * @param pContext Context
     * @param pDpVal   dp值
     * @return px值
     */
    private static int dp2px(Context pContext, int pDpVal) {
        float _Scale = pContext.getResources().getDisplayMetrics().density;
        return (int) (pDpVal * _Scale + 0.5f * (pDpVal >= 0 ? 1 : -1));
    }

    //根据分数得到需要跨过的角度
    private float getAngleToScore(float score) {
        if (score >= 700) {
            return (float) (180 + (score - 700) * 0.18);
        } else if (score >= 550) {
            return (float) (45 + (score - 550) * 0.9);
        } else {
            return (float) ((score - 350) * 0.225);
        }
    }


    private int gettextTopHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }
}
