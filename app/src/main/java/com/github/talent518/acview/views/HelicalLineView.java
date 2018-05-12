package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 螺旋线
 */

public class HelicalLineView extends View implements View.OnClickListener {
    private static final String TAG = HelicalLineView.class.getSimpleName();

    public HelicalLineView(Context context) {
        super(context);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    public HelicalLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    public HelicalLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    public HelicalLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        isPlay = !isPlay;
        mHandler.sendEmptyMessage(0);
    }

    private float mAngle = 0;
    private float mAngleStep = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isPlay) {
                removeMessages(0);
                sendEmptyMessage(0);

                mAngle += mAngleStep;
                if ((mAngleStep > 0 && mAngle >= 720) || (mAngleStep < 0 && mAngle <= 0)) {
                    mAngleStep = -mAngleStep;
                }
            } else {
                mAngle = 0;
            }
            invalidate();
        }
    };

    private boolean isPlay = true;

    private int mWidth = 0, mHeight = 0;
    private Bitmap mBitmap = null;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;

        super.onSizeChanged(w, h, oldw, oldh);

        if (mBitmap != null) {
            mBitmap.recycle();
        }

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(mBitmap);
        final float cx = mWidth / 2.0f, cy = mHeight / 2.0f;
        float radius = Math.min(mWidth, mHeight) / 2.0f - 10.0f;
        double i, r, step = 1;
        float x, y;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawColor(Color.WHITE);

        i = -1;
        for (; ; ) {
            i += step;
            r = 1 + 4.0f * i * Math.PI / 180.0f;
            if (r > radius) {
                break;
            }

            x = cx + (float) (cos(i) * r);
            y = cy - (float) (sin(i) * r);

            if (i > 360.0f) {
                step = 180.0f / (Math.PI * r);
            }

            canvas.drawPoint(x, y, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.rotate(mAngle, mWidth / 2.0f, mHeight / 2.0f);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    protected double cos(double a) {
        return Math.cos(a * Math.PI / 180.0f);
    }

    protected double sin(double a) {
        return Math.sin(a * Math.PI / 180.0f);
    }
}
