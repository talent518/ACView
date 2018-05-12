package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 五角星
 */
public class FiveAngleStarView extends View implements View.OnClickListener {
    private static final String TAG = FiveAngleStarView.class.getSimpleName();

    public FiveAngleStarView(Context context) {
        super(context);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessageDelayed(0, 20);
    }

    public FiveAngleStarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessageDelayed(0, 20);
    }

    public FiveAngleStarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClickable(true);
        setOnClickListener(this);
        mHandler.sendEmptyMessageDelayed(0, 20);
    }

    @Override
    public void onClick(View v) {
        isPlay = !isPlay;
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 20);
    }

    private boolean isPlay = true;

    private int mAngle = 0;
    private int mAngleStep = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(isPlay) {
                sendEmptyMessageDelayed(0, 50);

                mAngle += mAngleStep;
                if((mAngleStep>0 && mAngle >= 360) || (mAngleStep<0 && mAngle <= 0) ) {
                    mAngleStep = -mAngleStep;
                }
            } else {
                mAngle = 0;
            }
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        double angle;
        float radius = Math.min(canvas.getWidth(), canvas.getHeight()) / 2.0f - 10.0f;
        float x, y, x2, y2;
        final float cx = canvas.getWidth() / 2.0f, cy = canvas.getHeight() / 2.0f;

        // Log.i(TAG, canvas.getWidth() + "," + canvas.getHeight());

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(Color.WHITE);
        for (int i = 0; i <= 720; i += 144) {
            angle = (90 + i + mAngle) * Math.PI / 180.0;
            x = cx + (float) (Math.cos(angle) * radius);
            y = cy - (float) (Math.sin(angle) * radius);
            angle = (90 + i + 144 + mAngle) * Math.PI / 180.0;
            x2 = cx + (float) (Math.cos(angle) * radius);
            y2 = cy - (float) (Math.sin(angle) * radius);
            canvas.drawLine(x, y, x2, y2, paint);

            if(i == 0) {
                canvas.drawCircle(x, y, 10, paint);
            }
        }
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, paint);
    }

}
