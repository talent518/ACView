package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 模拟时钟
 */
public class ClockView extends View {
    private static final String TAG = ClockView.class.getSimpleName();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sendEmptyMessageDelayed(0, 1000);
            invalidate();
        }
    };

    public ClockView(Context context) {
        super(context);

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");

    private int mWidth = 0, mHeight = 0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double angle;
        final float RADIUS = Math.min(mWidth, mHeight) / 2.0f;
        final float DIAL = RADIUS - 10.0f;
        final float DIAL2 = DIAL - RADIUS * 0.05f;
        final float TEXTSIZE = DIAL2 * 0.125f;
        final float TEXT = DIAL2 - TEXTSIZE / 2.0f - 5.0f;
        final float POINTER = DIAL2 - TEXTSIZE - 10.0f;
        final float DATETIME = POINTER / 2.0f;
        final float DTTextSize = POINTER * 0.2f;
        final float HOUR = POINTER * 0.5f;
        final float HOURWEIGTH = POINTER * 0.025f;
        final float MINUTE = POINTER * 0.75f;
        final float MINUTEWEIGHT = POINTER * 0.02f;
        final float SECOND = POINTER;
        final float SECONDWEIGHT = POINTER * 0.0125f;
        float x, y, x2, y2;
        final float cx = mWidth / 2.0f, cy = mHeight / 2.0f;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(Color.WHITE);

        canvas.drawCircle(cx, cy, DIAL, paint);
        canvas.drawCircle(cx, cy, DIAL2, paint);

        textPaint.setTextSize(TEXTSIZE);
        int n = 1;
        for (angle = 60; angle > -300; angle -= 6) {
            x = cx + (float) (cos(angle) * DIAL);
            y = cy - (float) (sin(angle) * DIAL);
            x2 = cx + (float) (cos(angle) * DIAL2);
            y2 = cy - (float) (sin(angle) * DIAL2);

            if (angle % 90 == 0) {
                paint.setStrokeWidth(10.0f);
            } else if (angle % 30 == 0) {
                paint.setStrokeWidth(6.0f);
            } else {
                paint.setStrokeWidth(2.0f);
            }
            canvas.drawLine(x, y, x2, y2, paint);
            if (angle % 30 == 0) {
                x = cx + (float) (cos(angle) * TEXT);
                y = cy - (float) (sin(angle) * TEXT) + TEXTSIZE / 3.0f;
                canvas.drawText(Integer.toString(n++), x, y, textPaint);
            }
        }

        String[] dfs = dateFormat.format(new Date()).split("\n");
        textPaint.setAlpha(64);
        textPaint.setTextSize(DTTextSize);
        canvas.drawText(dfs[0], cx, cy - DATETIME, textPaint);
        canvas.drawText(dfs[1], cx, cy + DATETIME, textPaint);

        paint.setStyle(Paint.Style.FILL);

        // 时针
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(HOURWEIGTH);
        drawPointer(canvas, cx, cy, HOUR * 1.0f / 2.0f, 1.0f, 90 - 30 * hour - 30.0f / 60.0f * minute, paint);

        // 分针
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(MINUTEWEIGHT);
        drawPointer(canvas, cx, cy, MINUTE * 2.0f / 3.0f, 1.0f / 2.0f, 90 - 6 * minute - 6.0f / 60.0f * second, paint);

        // 秒针
        paint.setColor(Color.RED);
        paint.setStrokeWidth(SECONDWEIGHT);
        drawPointer(canvas, cx, cy, SECOND * 2.0f / 3.0f, 1.0f / 2.0f, 90 - 6 * second, paint);
    }

    /**
     * 绘制指针
     *
     * @param canvas
     * @param cx
     * @param cy
     * @param radius
     * @param angle
     * @param paint
     */
    protected void drawPointer(Canvas canvas, float cx, float cy, float radius, float scale, double angle, Paint paint) {
        float x = cx + (float) (cos(angle) * radius);
        float y = cy - (float) (sin(angle) * radius);
        float x2 = cx + (float) (cos(angle + 180) * 24.0f);
        float y2 = cy - (float) (sin(angle + 180) * 24.0f);
        float r = paint.getStrokeWidth() * 1.5f;
        float R = radius + radius * scale;
        double agl = (float) (Math.acos(r / radius * scale) * 180.0f / Math.PI);

        canvas.drawLine(x2, y2, x, y, paint);

        paint.setStrokeWidth(1.0f);
        canvas.drawCircle(cx, cy, r, paint);
        canvas.drawCircle(x, y, r, paint);

        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x + (float) (cos(angle + agl) * r), y - (float) (sin(angle + agl) * r));
        path.lineTo(cx + (float) (cos(angle) * R), cy - (float) (sin(angle) * R));
        path.lineTo(x + (float) (cos(angle - agl) * r), y - (float) (sin(angle - agl) * r));
        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, paint);
    }

    protected double cos(double a) {
        return Math.cos(a * Math.PI / 180.0f);
    }

    protected double sin(double a) {
        return Math.sin(a * Math.PI / 180.0f);
    }

}
