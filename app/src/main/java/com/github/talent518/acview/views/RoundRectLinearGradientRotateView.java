package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RoundRectLinearGradientRotateView extends View implements View.OnClickListener {
    private static final String TAG = RoundRectLinearGradientRotateView.class.getSimpleName();
    private static final long DELAY = 50;
    private boolean isPerimeter = true;
    private float mAngle = 0, mStep = 1;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postOnAnimationDelayed(this, DELAY);
            mAngle += mStep;

            if (isPerimeter) {
                if (mAngle >= mPerimeter)
                    mAngle -= mPerimeter;
            } else {
                if (mAngle >= 360)
                    mAngle -= 360;
            }

            invalidate();
        }
    };
    private float mWidth, mHeight, mMin, mPerimeter, mProtectedAngle;
    private Bitmap mBitmap = null, mMaskBitmap = null;
    private Canvas mCanvas = null;

    public RoundRectLinearGradientRotateView(Context context) {
        this(context, null);
    }

    public RoundRectLinearGradientRotateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectLinearGradientRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RoundRectLinearGradientRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        postOnAnimationDelayed(mRunnable, DELAY);

        setOnClickListener(this);
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

        initProperties();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initProperties() {
        if (isPerimeter) {
            mPerimeter = mWidth * 2 + mHeight * 2;
            mStep = mPerimeter / (5000 / DELAY);
            Log.i(TAG, "mWidth = " + mWidth + ", mHeight = " + mHeight + ", mMin = " + mMin + ", mStep = " + mStep + ", mPerimeter = " + mPerimeter);
        } else {
            mStep = 360 / (2000 / DELAY);
            mProtectedAngle = (float) (Math.atan(mHeight / mWidth) * 180.0f / Math.PI) / 2.0f;
            Log.i(TAG, "mWidth = " + mWidth + ", mHeight = " + mHeight + ", mMin = " + mMin + ", mStep = " + mStep + ", mProtectedAngle = " + mProtectedAngle);
        }
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
        canvas.drawColor(0xFFFFFFFF);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mMin / 20.0f);
        paint.setAntiAlias(true);
        paint.setColor(0xFF38EDFB);

        final float lw = paint.getStrokeWidth() / 2.0f;
        RectF rectF = new RectF(lw, lw, mWidth - lw, mHeight - lw);
        PointF p1, p2;

        if (isPerimeter) {
            p1 = getPointF(mAngle);
            p2 = getPointF(mAngle + mWidth + mHeight);

            Log.i(TAG, "mAngle = " + mAngle + ", p1 = " + p1 + ", p2 = " + p2);

            paint.setShader(new LinearGradient(p1.x, p1.y, p2.x, p2.y, new int[]{0xFF9C82FF, 0xFF38EDFB}, null, Shader.TileMode.CLAMP));

            canvas.drawRoundRect(rectF, mMin / 2.0f, mMin / 2.0f, paint);
        } else {
            LinearGradient linearGradient = new LinearGradient(0, 0, mWidth, mHeight, new int[]{0xFF9C82FF, 0xFF38EDFB}, null, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();

            matrix.setRotate(mAngle, mWidth / 2.0f, mHeight / 2.0f);
            linearGradient.setLocalMatrix(matrix);
            paint.setShader(linearGradient);

            canvas.drawRoundRect(rectF, mMin / 2.0f, mMin / 2.0f, paint);

            float radius = (float) Math.sqrt(mWidth * mWidth + mHeight * mHeight) / 2.0f;
            float cx = mWidth / 2.0f, cy = mHeight / 2.0f;
            double angle = ((360 - mAngle) * Math.PI / 180.0f);

            p1 = new PointF();
            p1.x = (float) (cx + radius * Math.cos(angle));
            p1.y = (float) (cy - radius * Math.sin(angle));

            p2 = new PointF();
            p2.x = (float) (cx + radius * Math.cos(angle + Math.PI));
            p2.y = (float) (cy - radius * Math.sin(angle + Math.PI));
        }

        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);

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

    protected PointF getPointF(float n) {
        PointF pointF = new PointF();

        while (n >= mPerimeter) {
            n -= mPerimeter;
        }

        if (n <= mWidth) {
            pointF.x = n;
            pointF.y = 0;
        } else if (n <= mWidth + mHeight) {
            pointF.x = mWidth;
            pointF.y = n - mWidth;
        } else if (n <= mWidth * 2 + mHeight) {
            pointF.x = mWidth - (n - mWidth - mHeight);
            pointF.y = mHeight;
        } else {
            pointF.x = 0;
            pointF.y = mHeight - (n - mWidth * 2 - mHeight);
        }

        return pointF;
    }

    @Override
    public void onClick(View v) {
        isPerimeter = !isPerimeter;
        mAngle = 0;
        initProperties();
        invalidate();
    }
}
