package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
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

    @Override
    protected void onDraw(Canvas canvas) {
        double angle;
        final float RADIUS = Math.min(canvas.getWidth(), canvas.getHeight()) / 2.0f;
        final float DIAL = RADIUS - 10.0f;
        final float DIAL2 = DIAL - RADIUS * 0.05f;
        final float TEXTSIZE = DIAL2 * 0.125f;
        final float TEXT = DIAL2 - TEXTSIZE / 2.0f - 5.0f;
        float radius = DIAL2 - TEXTSIZE - 10.0f;
        float x, y, x2, y2;
        final float cx = canvas.getWidth() / 2.0f, cy = canvas.getHeight() / 2.0f;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        Log.i(TAG, canvas.getWidth() + "," + canvas.getHeight());

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.STROKE);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);

        textPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawColor(Color.WHITE);

        canvas.drawCircle(cx, cy, DIAL, paint);
        canvas.drawCircle(cx, cy, DIAL2, paint);

        textPaint.setTextSize(TEXTSIZE);
        int n = 1;
        for (int i = 60; i > -300; i -= 6) {
            angle = i * Math.PI / 180.0;
            x = cx + (float) (Math.cos(angle) * DIAL);
            y = cy - (float) (Math.sin(angle) * DIAL);
            x2 = cx + (float) (Math.cos(angle) * DIAL2);
            y2 = cy - (float) (Math.sin(angle) * DIAL2);

            if (i % 90 == 0) {
                paint.setStrokeWidth(10.0f);
            } else if (i % 30 == 0) {
                paint.setStrokeWidth(6.0f);
            } else {
                paint.setStrokeWidth(2.0f);
            }
            canvas.drawLine(x, y, x2, y2, paint);
            if (i % 30 == 0) {
                x = cx + (float) (Math.cos(angle) * TEXT);
                y = cy - (float) (Math.sin(angle) * TEXT) + TEXTSIZE/3.0f;
                canvas.drawText(Integer.toString(n++), x, y, textPaint);
            }
        }

        String[] dfs = dateFormat.format(new Date()).split("\n");
        textPaint.setAlpha(64);
        textPaint.setTextSize(radius*0.2f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(dfs[0], cx, cy - radius*0.5f, textPaint);
        textPaint.setTextSize(radius*0.2f);
        canvas.drawText(dfs[1], cx, cy + radius*0.5f, textPaint);

        paint.setStyle(Paint.Style.FILL);

        angle = (90 - 30 * hour - 30.0f / 60.0f * minute) * Math.PI / 180.0;
        x = cx + (float) (Math.cos(angle) * radius * 0.5f);
        y = cy - (float) (Math.sin(angle) * radius * 0.5f);
        angle = (90 - 30 * hour - 30.0f / 60.0f * minute + 180) * Math.PI / 180.0;
        x2 = cx + (float) (Math.cos(angle) * 24);
        y2 = cy - (float) (Math.sin(angle) * 24);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(radius*0.035f);
        canvas.drawLine(x2, y2, x, y, paint);
        canvas.drawCircle(cx, cy, 12, paint);

        angle = (90 - 6 * minute - 6.0f / 60.0f * second) * Math.PI / 180.0;
        x = cx + (float) (Math.cos(angle) * radius * 0.75f);
        y = cy - (float) (Math.sin(angle) * radius * 0.75f);
        angle = (90 - 6 * minute - 6.0f / 60.0f * second + 180) * Math.PI / 180.0;
        x2 = cx + (float) (Math.cos(angle) * 24);
        y2 = cy - (float) (Math.sin(angle) * 24);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(radius*0.02f);
        canvas.drawLine(x2, y2, x, y, paint);
        canvas.drawCircle(cx, cy, 8, paint);

        angle = (90 - 6 * second) * Math.PI / 180.0;
        x = cx + (float) (Math.cos(angle) * radius);
        y = cy - (float) (Math.sin(angle) * radius);
        angle = (90 - 6 * second + 180) * Math.PI / 180.0;
        x2 = cx + (float) (Math.cos(angle) * 24);
        y2 = cy - (float) (Math.sin(angle) * 24);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(radius*0.01f);
        canvas.drawLine(x2, y2, x, y, paint);
        canvas.drawCircle(cx, cy, 4, paint);
    }

}
