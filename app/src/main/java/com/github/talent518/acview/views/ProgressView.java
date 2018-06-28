package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ProgressView extends View {
    private static final String TAG = ProgressView.class.getSimpleName();
    private static final float frameAngles[] = new float[40];
    private static final File savePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), TAG);

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
        if (!savePath.exists()) {
            savePath.mkdir();
        }
    }

    private int mSleepDelay = 150, mDelay = 30;
    private int mFrameIndex = -1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mFrameIndex++;
            if (mFrameIndex == frameAngles.length) {
                mFrameIndex = -1;

                sendEmptyMessageDelayed(0, mSleepDelay);
            } else {
                sendEmptyMessageDelayed(0, mDelay);
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

        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                File file;

                file = new File(savePath, "01.png");
                if (!file.exists()) {
                    draw(canvas, 0);
                    saveImage(bitmap, file);
                }

                int n = 2;
                for (float nframe : frameAngles) {
                    file = new File(savePath, (n < 10 ? "0" : "") + n + ".png");
                    n++;
                    if (!file.exists()) {
                        draw(canvas, nframe);
                        saveImage(bitmap, file);
                    }
                }
            }
        }.start();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void saveImage(Bitmap bitmap, File file) {
        Log.i(TAG, "saveImage: " + file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        draw(canvas, mFrameIndex == -1 ? 0 : frameAngles[mFrameIndex]);
    }

    private void draw(Canvas canvas, float nframe) {
        float cx = mWidth / 2.0f;
        float cy = mHeight / 2.0f;

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(0xFF2C202C);

//        Paint linePaint = new Paint();
//        linePaint.setColor(Color.WHITE);
//        linePaint.setStrokeWidth(1);
//        linePaint.setStyle(Paint.Style.STROKE);
//        canvas.drawLine(0, cy, mWidth, cy, linePaint);
//        canvas.drawLine(cx, 0, cx, mHeight, linePaint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(cx / 8.0f);
        textPaint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        canvas.drawText("正在关机", cx, cy - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top, textPaint);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(cx / 25.0f);
        paint.setColor(0xFF3E3D45);
        paint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, cx / 2.0f, paint);

        paint.setColor(0xFF38EDFB);
        paint.setStrokeCap(Paint.Cap.ROUND);

        RectF rectF = new RectF(cx / 2.0f, cy / 2.0f, cx * 3.0f / 2.0f, cy * 3.0f / 2.0f);
        paint.setShader(new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.bottom, new int[]{0xFF9C82FF, 0xFF38EDFB}, null, Shader.TileMode.CLAMP));
        canvas.drawArc(rectF, nframe, 90, false, paint);
    }
}
