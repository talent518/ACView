package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SweepGradientRotateView extends View {
    private static final String TAG = SweepGradientRotateView.class.getSimpleName();
    private static final long DELAY = 50;
    private int[] mColors = new int[]{0XFFAB2CF7, 0XFF6B7BF6, 0XFF2CC8F7, 0XFF000000};
    private float[] mPositions = new float[]{0, 0.5f, 0.8f, 1};
    private int mAngle = 0;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postOnAnimationDelayed(this, DELAY);

            mAngle = (mAngle + 9) % 360;

            invalidate();
        }
    };
    private float mWidth, mHeight, mMin;
    private Bitmap mBitmap = null, mMaskBitmap = null;
    private Canvas mCanvas = null;

    public SweepGradientRotateView(Context context) {
        this(context, null);
    }

    public SweepGradientRotateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepGradientRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SweepGradientRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        postOnAnimationDelayed(mRunnable, DELAY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mMin = Math.min(mWidth, mHeight);

        if (mMaskBitmap != null) {
            mMaskBitmap.recycle();
        }

        if (mBitmap != null) {
            mBitmap.recycle();
        }

        mMaskBitmap = getMaskBitmap();
        mBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);


        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(mCanvas);

        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawBitmap(Canvas canvas) {
        // 清空位图
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        canvas.drawColor(Color.TRANSPARENT);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mMin / 20.0f);
        paint.setAntiAlias(true);
        paint.setColor(0xFF38EDFB);

        final float lw = paint.getStrokeWidth() / 2.0f;
        RectF rectF = new RectF(lw, lw, mWidth - lw, mHeight - lw);
        SweepGradient sweepGradient = new SweepGradient(mWidth / 2.0f, mHeight / 2.0f, mColors, mPositions);
        Matrix matrix = new Matrix();
        matrix.setRotate(mAngle, mWidth / 2.0f, mHeight / 2.0f);
        sweepGradient.setLocalMatrix(matrix);

        paint.setShader(sweepGradient);
        canvas.drawRoundRect(rectF, mMin / 2.0f, mMin / 2.0f, paint);

        float radius = (float) Math.sqrt(mWidth * mWidth + mHeight * mHeight) / 2.0f;
        float cx = mWidth / 2.0f, cy = mHeight / 2.0f;
        double angle = ((360 - mAngle) * Math.PI / 180.0f);

        PointF pointF = new PointF();
        pointF.x = (float) (cx + radius * Math.cos(angle));
        pointF.y = (float) (cy - radius * Math.sin(angle));

        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawLine(cx, cy, pointF.x, pointF.y, paint);

        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(getMaskBitmap(), null, new RectF(0, 0, mWidth, mHeight), paint);
    }

    private Bitmap getMaskBitmap() {
        Bitmap bitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawRoundRect(0, 0, mWidth, mHeight, mMin / 2.0f, mMin / 2.0f, paint);
        canvas.save();

        return bitmap;
    }

}
