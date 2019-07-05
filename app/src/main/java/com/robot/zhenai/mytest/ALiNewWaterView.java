package com.robot.zhenai.mytest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class ALiNewWaterView extends View {
    private Context mContext;

    private Paint waterPaint;
    private Paint textPaint;
    private int waterHeight = 0;

    private int waterWidth = 0;
    private Path path;
    private Path circlePath;
    private float TOTAL_NUM = 225f;
    private int num = 23;//22格
    private float radian = (float) Math.toRadians(TOTAL_NUM / (num - 1)); //没一格的弧度
    private float rotateDegress = TOTAL_NUM / (num - 1);
    private int mRadius = 0;
    private int width;
    private int height;

    private int smallHeight;
    private PorterDuffXfermode mXfermode;
    private int currentNum = 18;
    private int score;
    private String level = "信用良好";

    public ALiNewWaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
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
            width = getPaddingLeft() + getPaddingRight() + 2 * mRadius;
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = (int) (getPaddingTop() + getPaddingBottom() + mRadius + Math.cos(Math.toRadians(67.5)) * mRadius) + waterWidth;

        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measueWidth : width,
                heightMode == MeasureSpec.EXACTLY ? measueHeight : height);
    }

    private void init() {
        waterHeight = dp2px(mContext, 11);
        waterWidth = dp2px(mContext, 4);
        smallHeight = dp2px(mContext, 2);
        mRadius = dp2px(mContext, 80);

        waterPaint = new Paint();
        waterPaint.setStyle(Paint.Style.FILL);
        waterPaint.setColor(Color.WHITE);
        waterPaint.setAntiAlias(true);
        waterPaint.setDither(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        path = new Path();
        circlePath = new Path();
//        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
        canvas.translate(getPaddingLeft() + mRadius, getPaddingTop() + mRadius);
        //画水滴
        drawWater(canvas);
        //画分数
        drawScore(canvas);

        //画信用等级
        drawLevel(canvas);
    }

    private void drawLevel(Canvas canvas) {
        textPaint.setTextSize(dp2px(mContext, 20));
        float text_x = -textPaint.measureText(level) / 2;
        canvas.drawText(level, text_x, (float) (mRadius * Math.cos(Math.toRadians(67.5))), textPaint);
    }

    private void drawScore(Canvas canvas) {
        textPaint.setTextSize(dp2px(mContext, 30));
        float text_x = -textPaint.measureText(score + "") / 2;
        float text_y = getTextTopHeight(score + "", textPaint) / 4;
        canvas.drawText(score + "", text_x, text_y, textPaint);
    }

    private void drawWater(Canvas canvas) {
        canvas.save();
        canvas.rotate(67.5f);
        for (int i = 0; i < num; i++) {
            if (i >= currentNum) {
                waterPaint.setAlpha(150);
            } else {
                waterPaint.setAlpha(255);
            }
            path.moveTo(0, mRadius);
            path.cubicTo(waterWidth, mRadius, waterWidth, mRadius - waterHeight + waterWidth, 0, mRadius - waterHeight);
            path.cubicTo(-waterWidth, mRadius - waterHeight + waterWidth, -waterWidth, mRadius, 0, mRadius);
            path.close();
            canvas.drawPath(path, waterPaint);
            canvas.rotate(rotateDegress);
        }
        canvas.restore();
    }

    public void setScore(int score) {
        if (score >= 700) {
            currentNum = (int) ((180 + (score - 700) * 0.18) / rotateDegress);
        } else if (score >= 550) {
            currentNum = (int) ((45 + (score - 550) * 0.9) / rotateDegress);
        } else {
            currentNum = (int) (((score - 350) * 0.225) / rotateDegress);
        }
        this.score = score;
        setCreditLevel(score);
        invalidate();

    }

    public void setCreditLevel(int score) {
        if (score >= 350 && score <= 550) {
            level = "信用较差";
        } else if (score > 550 && score <= 600) {
            level = "信用中等";
        } else if (score > 600 && score <= 650) {
            level = "信用良好";
        } else if (score > 650 && score <= 700) {
            level = "信用优秀";
        } else if (score > 700 && score <= 950) {
            level = "信用极好";
        }
    }

    private int getTextTopHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
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
}
