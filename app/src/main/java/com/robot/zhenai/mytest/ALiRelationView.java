package com.robot.zhenai.mytest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ALiRelationView extends View {

    private Context mContext;

    private Paint outPaint;
    private Paint textPaint;

    private Paint innerPaint;
    private int outRadius = 0;
    private int innerRadius = 0;
    private int outPaintWidth = 0;
    private int innerPaintWidth = 0;

    private int height;
    private int width;
    private float numWidth = 0;
    private float numHeight = 0;

    private float numGapWidth = 0;
    private float levelWidth;
    private float levelHeight;
    private Path path;
    private PointF pointA;
    private PointF pointB;
    private PointF pointC;
    private PointF pointD;
    private PointF pointE;
    private PointF pointA1;
    private PointF pointB1;
    private PointF pointC1;
    private PointF pointD1;
    private PointF pointE1;
    private String texts[] = {"历史", "行为", "履约能力", "人脉", "身份"};
    private int icons[] = {R.mipmap.lishi, R.mipmap.xingwei, R.mipmap.lvyue, R.mipmap.renmai, R.mipmap.huangguan};
    private int initWidth;
    private int initHeight;

    public ALiRelationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        initDatas();
        outPaint = new Paint();
        outPaint.setColor(Color.WHITE);
        outPaint.setAntiAlias(true);
        outPaint.setAlpha(100);
        outPaint.setStrokeWidth(outPaintWidth);
        outPaint.setStyle(Paint.Style.STROKE);

        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setColor(Color.WHITE);
        innerPaint.setAlpha(50);
        innerPaint.setStrokeWidth(innerPaintWidth);
        innerPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(32);
        textPaint.setColor(Color.WHITE);
        textPaint.setAlpha(200);

        path = new Path();
        pointA = new PointF();
        pointB = new PointF();
        pointC = new PointF();
        pointD = new PointF();
        pointE = new PointF();
        pointA1 = new PointF();
        pointB1 = new PointF();
        pointC1 = new PointF();
        pointD1 = new PointF();
        pointE1 = new PointF();
    }

    private void initDatas() {
        outRadius = dp2px(mContext, 60);
        innerRadius = dp2px(mContext, 40);
        outPaintWidth = dp2px(mContext, 1);
        innerPaintWidth = dp2px(mContext, 1);
        numGapWidth = dp2px(mContext, 10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measueWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measueHeight = MeasureSpec.getSize(heightMeasureSpec);
        Drawable d = getResources().getDrawable(icons[0]);
        initWidth = d.getIntrinsicWidth();
        initHeight = d.getIntrinsicHeight();
        //自己测量
        if (widthMode != MeasureSpec.EXACTLY) {
            width = (int) (getPaddingLeft() + getPaddingRight() + 2 * outRadius * Math.cos(Math.toRadians(18))
                    + (numGapWidth + initWidth) * 2);
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = (int) (getPaddingTop() + getPaddingBottom() + outRadius + Math.cos(Math.toRadians(36)) * outRadius
                    + (numGapWidth + gettextTopHeight(texts[0], textPaint) + initHeight) * 2);
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measueWidth : width,
                heightMode == MeasureSpec.EXACTLY ? measueHeight : height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#0f9af7"));
        canvas.translate((float) (getPaddingLeft() + outRadius * Math.cos(Math.toRadians(18)) + numGapWidth + initWidth),
                getPaddingTop() + outRadius + numGapWidth + gettextTopHeight(texts[0], textPaint) + initHeight);
        //画外部五边形
        drawFiveRect(canvas);
        //画内实心部五边形
        drawInnerFiveRect(canvas);
        //画连接线
        drawLine(canvas);
        //画搜索图标
        drawSearchBitmap(canvas);

        //画顶部图标和文字
        drawTopIconAndText(canvas);
        //画剩下的图标和文字
        drawLeaveIconAndText(canvas);
    }

    private void drawLeaveIconAndText(Canvas canvas) {
        outPaint.setAlpha(150);
        textPaint.setAlpha(200);
        canvas.save();
        float x = 0f;
        float iconY = 0f;
        float iconX = 0f;
        float y = 0f;
        for (int i = 0; i < texts.length; i++) {
            Drawable d = getResources().getDrawable(icons[i]);
            int iconHeight = d.getIntrinsicHeight();
            int iconWidth = d.getIntrinsicWidth();
            int textWidth = (int) textPaint.measureText(texts[i]);
            int textHeight = gettextTopHeight(texts[i], textPaint);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), icons[i]);
            //左右两边
            if (i == 1) {
                x = (float) (Math.cos(Math.toRadians(18)) * outRadius + numGapWidth);
                y = textHeight * 2 / 3;
                iconY = -iconHeight - dp2px(mContext, 5);
                canvas.drawText(texts[i], x, y, textPaint);
                canvas.drawBitmap(bm, x, iconY, outPaint);
            }
            if (i == 4) {
                x = -(float) (Math.cos(Math.toRadians(18)) * outRadius + numGapWidth + textWidth);
                y = textHeight * 2 / 3;
                ;
                iconY = -iconHeight - dp2px(mContext, 5);
                canvas.drawText(texts[i], x, y, textPaint);
                canvas.drawBitmap(bm, x, iconY, outPaint);
            }

            if (i == 2 || i == 3) {
                canvas.save();
                canvas.translate(0, (float) ((outRadius + numGapWidth) * Math.cos(Math.toRadians(36)) - 3));
                if (i == 2) {
                    iconX = (float) (Math.cos(Math.toRadians(54)) * (outRadius + numGapWidth));
                    iconY = 0;
                    x = iconX - (textWidth - iconWidth) / 2;
                    y = iconHeight + textHeight;
                }
                if (i == 3) {
                    iconX = -(float) (Math.cos(Math.toRadians(54)) * (outRadius + numGapWidth) + iconWidth * 2 / 3);
                    x = iconX;
                    y = iconHeight + textHeight;
                    iconY = 0;
                }
                canvas.drawText(texts[i], x, y, textPaint);
                canvas.drawBitmap(bm, iconX, iconY, outPaint);
                canvas.restore();
            }

        }
        canvas.restore();

    }

    private void drawTopIconAndText(Canvas canvas) {
        outPaint.setAlpha(255);
        canvas.save();
        canvas.translate(0, -(outRadius + numGapWidth));

        canvas.drawText(texts[0], -textPaint.measureText(texts[0]) / 2, 0, textPaint);

        drawIconBitmap(canvas, 0, -gettextTopHeight(texts[0], textPaint));
        canvas.restore();
    }

    private void drawIconBitmap(Canvas canvas, int posotion, float translate) {
        Drawable d = getResources().getDrawable(icons[posotion]);
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), icons[posotion]);

        canvas.drawBitmap(bm, -width / 2, -height + translate, outPaint);


    }

    private void drawSearchBitmap(Canvas canvas) {
        outPaint.setAlpha(255);
        Drawable d = getResources().getDrawable(R.mipmap.search);
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.search);

        canvas.drawBitmap(bm, -width / 2, -height / 2, outPaint);


    }

    private void drawLine(Canvas canvas) {
        outPaint.setAlpha(50);
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(pointA.x, pointA.y);
        path.moveTo(0, 0);
        path.lineTo(pointB.x, pointB.y);
        path.moveTo(0, 0);
        path.lineTo(pointC.x, pointC.y);
        path.moveTo(0, 0);
        path.lineTo(pointD.x, pointD.y);
        path.moveTo(0, 0);
        path.lineTo(pointE.x, pointE.y);
        canvas.drawPath(path, outPaint);
    }

    private void drawInnerFiveRect(Canvas canvas) {
        path.reset();
        initInnerCoordinate();
        path.moveTo(pointA1.x, pointA1.y);
        path.lineTo(pointB1.x, pointB1.y);
        path.lineTo(pointC1.x, pointC1.y);
        path.lineTo(pointD1.x, pointD1.y);
        path.lineTo(pointE1.x, pointE1.y);
        path.close();
        canvas.drawPath(path, innerPaint);
    }

    private void initInnerCoordinate() {

        pointA1.x = 0;
        pointA1.y = -innerRadius;
        pointB1.x = (float) Math.cos(Math.toRadians(18)) * innerRadius;
        pointB1.y = -(float) Math.sin(Math.toRadians(18)) * innerRadius;
        pointC1.x = (float) Math.cos(Math.toRadians(54)) * innerRadius;
        pointC1.y = (float) Math.sin(Math.toRadians(54)) * innerRadius;
        pointD1.x = -(float) Math.cos(Math.toRadians(54)) * innerRadius;
        pointD1.y = (float) Math.sin(Math.toRadians(54)) * innerRadius;
        pointE1.x = -(float) Math.cos(Math.toRadians(18)) * innerRadius;
        pointE1.y = -(float) Math.sin(Math.toRadians(18)) * innerRadius;

    }

    private void drawFiveRect(Canvas canvas) {
        outPaint.setAlpha(255);
        initOutCoordinate();
        path.moveTo(pointA.x, pointA.y);
        path.lineTo(pointB.x, pointB.y);
        path.lineTo(pointC.x, pointC.y);
        path.lineTo(pointD.x, pointD.y);
        path.lineTo(pointE.x, pointE.y);
        path.close();
        outPaint.setShader(new SweepGradient(0,0,new int[]{0x30ffffff,0x4dffffff,0x80ffffff,0xd9ffffff,0xe8ffffff},null));
        canvas.drawPath(path, outPaint);

    }

    private void initOutCoordinate() {
        pointA.x = 0;
        pointA.y = -outRadius;
        pointB.x = (float) Math.cos(Math.toRadians(18)) * outRadius;
        pointB.y = -(float) Math.sin(Math.toRadians(18)) * outRadius;
        pointC.x = (float) Math.cos(Math.toRadians(54)) * outRadius;
        pointC.y = (float) Math.sin(Math.toRadians(54)) * outRadius;
        pointD.x = -(float) Math.cos(Math.toRadians(54)) * outRadius;
        pointD.y = (float) Math.sin(Math.toRadians(54)) * outRadius;
        pointE.x = -(float) Math.cos(Math.toRadians(18)) * outRadius;
        pointE.y = -(float) Math.sin(Math.toRadians(18)) * outRadius;
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
