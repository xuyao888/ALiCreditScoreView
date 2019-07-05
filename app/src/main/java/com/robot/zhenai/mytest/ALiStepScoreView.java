package com.robot.zhenai.mytest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ALiStepScoreView extends View {
    private Context mContext;
    private Paint mRoundPaint;
    private Paint mBackPaint;
    private Paint mWhiteLinePaint;
    private Paint mTopTextPaint;
    private Paint mBottomTextPaint;
    private int colors[] = {0xffedf8fa, 0xff52cfbf};
    private String topTexts[] = {"较差", "中等", "良好", "优秀", "极好"};
    private int bottomTexts[] = {350, 550, 600, 650, 700, 950};
    private int roundRectWidth = 0;
    private int roundRectHeight = 0;
    private int width;
    private int height;
    private int gapHeight = 0;
    private int textBottomWidth = 0;
    private int textTopHeight = 0;
    private int paddingGap = 0;
    private int triangleHeight = 0;
    private int stepNum = 6;
    private float stepWidth;
    private int score = 600;
    private int topHeight;
    private int textBottomHeight;
    private int textTopWidth;
    private Path path;

    public ALiStepScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        init();
    }

    private void init() {
        roundRectWidth = dp2px(mContext, 300);
        roundRectHeight = dp2px(mContext, 6);
        gapHeight = dp2px(mContext, 5);
        paddingGap = dp2px(mContext, 2);
        triangleHeight = dp2px(mContext, 2);
        mRoundPaint = new Paint();
        mRoundPaint.setStyle(Paint.Style.FILL);
        mRoundPaint.setAntiAlias(true);
        Shader shader = new LinearGradient(0, 0, roundRectWidth, 0, colors, null, Shader.TileMode.CLAMP);
        mRoundPaint.setShader(shader);
        mWhiteLinePaint = new Paint();
        mWhiteLinePaint.setStyle(Paint.Style.FILL);
        mWhiteLinePaint.setColor(Color.WHITE);
        mTopTextPaint = new Paint();
        mTopTextPaint.setTextSize(28);
        mTopTextPaint.setColor(Color.parseColor("#333333"));

        mBottomTextPaint = new Paint();
        mBottomTextPaint.setTextSize(30);
        mBottomTextPaint.setColor(Color.parseColor("#b7b7b7"));
        mBackPaint = new Paint();
        mBackPaint.setColor(Color.parseColor("#52cfbf"));
        mBackPaint.setAntiAlias(true);

        path = new Path();
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

            textBottomWidth = (int) mBottomTextPaint.measureText(bottomTexts[0] + "");
            textTopWidth = (int) mTopTextPaint.measureText(topTexts[0] + "");
            width = getPaddingLeft() + getPaddingRight() + roundRectWidth + textBottomWidth;
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            Paint.FontMetrics fontMetrics = mTopTextPaint.getFontMetrics();
            textTopHeight = gettextTopHeight(topTexts[0] + "", mTopTextPaint);
//            textTopHeight = (int) Math.abs(fontMetrics.ascent - fontMetrics.descent);
//            Paint.FontMetrics fontMetrics1 = mBottomTextPaint.getFontMetrics();
            textBottomHeight = gettextTopHeight(bottomTexts[0] + "", mBottomTextPaint);
            height = getPaddingTop() + getPaddingBottom() + roundRectHeight + gapHeight * 2 + textTopHeight + textBottomHeight + paddingGap * 2 + triangleHeight;
            Log.e("TAG", "----textTopHeight--" + textTopHeight + "=textBottomHeight==" + textBottomHeight);
        }


        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measueWidth : width,
                heightMode == MeasureSpec.EXACTLY ? measueHeight : height);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.RED);
        // 1.画圆角矩形 并且渐变
        drawGradientRoundRect(canvas);
        //画白色线条
        drawWhiteLine(canvas);
        //画底部文字
        drawBottomText(canvas);
        //难点  根据当前得分 确定当前位置 并画出指向图
//        drawScorePoint(canvas);
        //画顶部文字
        drawTopText(canvas);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawScorePoint(Canvas canvas) {

        canvas.save();
        canvas.translate(textBottomWidth / 2, 0);
        float startX = stepWidth / 2 - triangleHeight;
        float startY = paddingGap * 2 + textTopHeight;
        if (bottomTexts[0] <= score && score < bottomTexts[1]) { //较差
            float left = stepWidth / 2 - textTopWidth / 2 - paddingGap * 2;
            canvas.drawRoundRect(left, 0, left + paddingGap * 4 + textTopWidth, textTopHeight + paddingGap * 2, 5, 5, mBackPaint);
            path.moveTo(startX, startY);
            path.lineTo(startX + triangleHeight, startY + triangleHeight);
            path.lineTo(startX + 2 * triangleHeight, startY);
            path.close();
            canvas.drawPath(path, mBackPaint);

        } else if (bottomTexts[1] <= score && score < bottomTexts[2]) { //中等

        } else if (bottomTexts[2] <= score && score < bottomTexts[3]) { //良好

        } else if (bottomTexts[3] <= score && score < bottomTexts[4]) { //优秀

        } else if (bottomTexts[4] <= score && score <= bottomTexts[5]) { //极好

        }
        canvas.restore();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawTopText(Canvas canvas) {
        int currentIndex = 0;
        canvas.save();
        canvas.translate(textBottomWidth / 2, 0);
        float startX = stepWidth / 2 - triangleHeight;
        float startY = paddingGap * 2 + textTopHeight;
        if (bottomTexts[0] <= score && score < bottomTexts[1]) { //较差
            currentIndex = 0;
        } else if (bottomTexts[1] <= score && score < bottomTexts[2]) { //中等
            currentIndex = 1;
        } else if (bottomTexts[2] <= score && score < bottomTexts[3]) { //良好
            currentIndex = 2;
        } else if (bottomTexts[3] <= score && score < bottomTexts[4]) { //优秀
            currentIndex = 3;
        } else if (bottomTexts[4] <= score && score <= bottomTexts[5]) { //极好
            currentIndex = 4;
        }
        for (int i = 0; i < topTexts.length; i++) {
            if (i == currentIndex) {
                float left = stepWidth / 2 - textTopWidth / 2 - paddingGap * 2 + i * stepWidth;
                canvas.drawRoundRect(left, 0, left + paddingGap * 4 + textTopWidth, textTopHeight + paddingGap * 2, 8, 8, mBackPaint);
                path.moveTo(startX + i * stepWidth, startY);
                path.lineTo(startX + i * stepWidth + triangleHeight, startY + triangleHeight);
                path.lineTo(startX + i * stepWidth + 2 * triangleHeight, startY);
                path.close();
                canvas.drawPath(path, mBackPaint);
                mTopTextPaint.setColor(Color.WHITE);
            } else {
                mTopTextPaint.setColor(Color.parseColor("#333333"));
            }
            canvas.drawText(topTexts[i], (2 * i + 1) * stepWidth / 2 - textTopWidth / 2, textTopHeight + 2, mTopTextPaint);
        }

        canvas.restore();
    }


    private void drawBottomText(Canvas canvas) {
        for (int i = 0; i < bottomTexts.length; i++) {
//            canvas.drawRect(i * stepWidth, topHeight + roundRectHeight + gapHeight, i * stepWidth + textBottomWidth, topHeight +
//                    roundRectHeight + gapHeight + textBottomHeight, mWhiteLinePaint);
            canvas.drawText(bottomTexts[i] + "", i * stepWidth, topHeight + roundRectHeight + gapHeight + textBottomHeight, mBottomTextPaint);

        }
    }


    private void drawWhiteLine(Canvas canvas) {
        canvas.save();
        canvas.translate(textBottomWidth / 2, topHeight);
        stepWidth = roundRectWidth * 1.0f / (stepNum - 1);
        for (int i = 0; i < stepNum; i++) {
            if (i == 0 || i == 5) continue;
            canvas.drawLine(stepWidth * i, 0, stepWidth * i, roundRectHeight, mWhiteLinePaint);
        }
        canvas.restore();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawGradientRoundRect(Canvas canvas) {
        topHeight = textTopHeight + paddingGap * 2 + triangleHeight + gapHeight;
        canvas.drawRoundRect(textBottomWidth / 2, topHeight, roundRectWidth + textBottomWidth / 2, roundRectHeight + topHeight, dp2px(mContext, 5), dp2px(mContext, 5), mRoundPaint);
    }

    public void setScore(int score) {
        this.score = score;
        invalidate();
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

    private int gettextTopHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

}
