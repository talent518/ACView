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
        final float SIDE = 10.0f;
        float radius = Math.min(canvas.getWidth(), canvas.getHeight()) / 2.0f - 2 * SIDE;
        float x, y;
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

        canvas.drawColor(Color.WHITE);

        String[] dfs = dateFormat.format(new Date()).split("\n");
        paint.setTextSize(radius*0.1f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(dfs[0], cx, cy - radius*0.6f, paint);
        canvas.drawText(dfs[1], cx, cy - radius*0.6f + paint.getTextSize(), paint);

        canvas.drawCircle(cx, cy, radius, paint);
        canvas.drawCircle(cx, cy, radius + SIDE, paint);

        int sw = 0;
        for (int i = 0; i < 360; i += 6) {
            angle = i * Math.PI / 180.0;
            x = cx + (float) (Math.cos(angle) * radius);
            y = cy - (float) (Math.sin(angle) * radius);
            float x2 = cx + (float) (Math.cos(angle) * (radius + SIDE));
            float y2 = cy - (float) (Math.sin(angle) * (radius + SIDE));

            if (i % 90 == 0) {
                paint.setStrokeWidth(10.0f);
            } else if (i % 30 == 0) {
                paint.setStrokeWidth(6.0f);
            } else {
                paint.setStrokeWidth(2.0f);
            }
            canvas.drawLine(x, y, x2, y2, paint);
        }

        radius -= SIDE;

        angle = (90 - 30 * hour - 30.0f / 60.0f * minute) * Math.PI / 180.0;
        x = cx + (float) (Math.cos(angle) * radius * 0.5f);
        y = cy - (float) (Math.sin(angle) * radius * 0.5f);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(6.0f);
        canvas.drawLine(cx, cy, x, y, paint);

        angle = (90 - 6 * minute - 6.0f / 60.0f * second) * Math.PI / 180.0;
        x = cx + (float) (Math.cos(angle) * radius * 0.75f);
        y = cy - (float) (Math.sin(angle) * radius * 0.75f);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4.0f);
        canvas.drawLine(cx, cy, x, y, paint);

        angle = (90 - 6 * second) * Math.PI / 180.0;
        x = cx + (float) (Math.cos(angle) * radius);
        y = cy - (float) (Math.sin(angle) * radius);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2.0f);
        canvas.drawLine(cx, cy, x, y, paint);
    }

}
