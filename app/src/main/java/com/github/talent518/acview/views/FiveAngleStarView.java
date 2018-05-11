package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 五角星
 */
public class FiveAngleStarView extends View {
    private static final String TAG = FiveAngleStarView.class.getSimpleName();

    public FiveAngleStarView(Context context) {
        super(context);
    }

    public FiveAngleStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FiveAngleStarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double angle;
        float radius = Math.min(canvas.getWidth(), canvas.getHeight()) / 2.0f - 10.0f;
        float x, y, x2, y2;
        final float cx = canvas.getWidth() / 2.0f, cy = canvas.getHeight() / 2.0f;

        Log.i(TAG, canvas.getWidth() + "," + canvas.getHeight());

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);
        paint.setAntiAlias(false);

        canvas.drawColor(Color.WHITE);
        for (int i = 0; i <= 720; i += 144) {
            angle = (90 + i) * Math.PI / 180.0;
            x = cx + (float) (Math.cos(angle) * radius);
            y = cy - (float) (Math.sin(angle) * radius);
            angle = (90 + i + 144) * Math.PI / 180.0;
            x2 = cx + (float) (Math.cos(angle) * radius);
            y2 = cy - (float) (Math.sin(angle) * radius);
            canvas.drawLine(x, y, x2, y2, paint);
        }
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, paint);
    }

}
