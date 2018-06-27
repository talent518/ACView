package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class ProgressView extends View {
    private static final String TAG = ProgressView.class.getSimpleName();
    private static final float frameAngles[] = new float[40];

    static {
        final int frameRates[] = new int[]{4, 4, 4, 6, 8, 14}; // 数组长度表示为N, 每N分之一的角度占用多少帧
        float perAngle = 360.0f / frameRates.length, angle = 0, ppAngle;
        int nRate = 0;
        int n = 0;
        for (int rate : frameRates) {
            nRate += rate;

            ppAngle = perAngle / rate;
            for (int i = 0; i < rate; i++) {
                angle += ppAngle;
                frameAngles[n++] = angle;
            }
        }
        Log.i(TAG, Arrays.toString(frameAngles)); // 已生成的每帧的绘图角度(单位: 度)
        if (nRate != frameAngles.length) {
            throw new RuntimeException("frame number: " + nRate + " != " + frameAngles.length + "");
        }
    }

    private int mDelay = 30;
    private int mFrameIndex = -1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sendEmptyMessageDelayed(0, mDelay);

            mFrameIndex++;
            if (mFrameIndex == frameAngles.length) {
                mFrameIndex = -1;
            }
            invalidate();
        }
    };
    private int mWidth = 0, mHeight = 0;

    public ProgressView(Context context) {
        super(context);

        mHandler.sendEmptyMessageDelayed(0, mDelay);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mHandler.sendEmptyMessageDelayed(0, mDelay);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHandler.sendEmptyMessageDelayed(0, mDelay);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mHandler.sendEmptyMessageDelayed(0, mDelay);
    }

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
        float cx = mWidth / 2.0f;
        float cy = mHeight / 2.0f;
        canvas.drawColor(0xFF2C202C);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setColor(0xFF3E3D45);
        canvas.drawCircle(cx, cy, cx / 2.0f, paint);

        paint.setColor(0xFF38EDFB);
        paint.setStrokeCap(Paint.Cap.ROUND);

        RectF rectF = new RectF(cx / 2.0f, cy / 2.0f, cx * 3.0f / 2.0f, cy * 3.0f / 2.0f);
        paint.setShader(new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.bottom, new int[]{0xFF9C82FF, 0xFF38EDFB}, null, Shader.TileMode.CLAMP));
        canvas.drawArc(rectF, mFrameIndex == -1 ? 0 : frameAngles[mFrameIndex], 90, false, paint);
    }
}
