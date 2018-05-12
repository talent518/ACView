package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Date;

/**
 * 五角星
 */
public class FiveAngleStarView extends View implements View.OnClickListener {
    private static final String TAG = FiveAngleStarView.class.getSimpleName();

    public FiveAngleStarView(Context context) {
        super(context);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    public FiveAngleStarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    public FiveAngleStarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        isPlay = !isPlay;
        if (isPlay) {
            bTime = new Date().getTime();
        }
        mHandler.sendEmptyMessage(0);
    }

    private boolean isPlay = true;

    private long runTime = -1;
    private long bTime = new Date().getTime();
    private double mAngle = 0;
    private double mAngleStep = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isPlay) {
                removeMessages(0);
                sendEmptyMessage(0);

                mAngle += mAngleStep;
                if ((mAngleStep > 0 && mAngle >= 360) || (mAngleStep < 0 && mAngle <= 0)) {
                    mAngleStep = -mAngleStep;
                    long eTime = new Date().getTime();
                    runTime = eTime - bTime;
                    bTime = eTime;
                }
            } else {
                mAngle = 0;
            }
            invalidate();
        }
    };

    private int mWidth = 0, mHeight = 0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double angle;
        float radius = Math.min(mWidth, mHeight) / 2.0f - 10.0f;
        float x, y, x2, y2;
        final float cx = mWidth / 2.0f, cy = mHeight / 2.0f;

        mAngleStep = (mAngleStep > 0 ? 180.0f : -180.0f) / (Math.PI * radius);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(Color.WHITE);
        for (int i = 0; i <= 720; i += 144) {
            angle = 90 + i + mAngle;
            x = cx + (float) (cos(angle) * radius);
            y = cy - (float) (sin(angle) * radius);
            x2 = cx + (float) (cos(angle + 144) * radius);
            y2 = cy - (float) (sin(angle + 144) * radius);
            canvas.drawLine(x, y, x2, y2, paint);

            if (i == 0) {
                canvas.drawCircle(x, y, 10, paint);
            }
        }

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, paint);

        if (runTime >= 0) {
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setAlpha(64);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            paint.setTextSize((float) (sin(18.0f) * radius * 0.35f));
            canvas.drawText(runTime + " ms", cx, cy, paint);
        }
    }

    protected double cos(double a) {
        return Math.cos(a * Math.PI / 180.0f);
    }

    protected double sin(double a) {
        return Math.sin(a * Math.PI / 180.0f);
    }

}
