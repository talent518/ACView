package com.github.talent518.acview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.talent518.acview.R;

/**
 * 模拟时钟
 */
public class PinBallView extends View implements View.OnClickListener {
    private static final String TAG = PinBallView.class.getSimpleName();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postOnAnimationDelayed(this, 20);

            invalidate();
        }
    };
    protected String name;
    protected float width = 0, height = 0;
    protected PointF p1 = new PointF(), p2 = new PointF(), p3 = new PointF(), p4 = new PointF();
    protected double angle = 0, angle2 = 0, radius = 0, maxRadius = 0;
    protected final int circleRadius = 20;

    public PinBallView(Context context) {
        this(context, null);
    }

    public PinBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinBallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinBallView);

        name = a.getString(R.styleable.PinBallView_name);

        a.recycle();

        postOnAnimation(mRunnable);

        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w - circleRadius * 2;
        height = h - circleRadius * 2;

        Log.d(TAG, name + ": width = " + width + ", height = " + height);

        init();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec == 0) {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(height, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    protected void init() {
        p1.x = width * (float) Math.random();
        p2.y = height * (float) Math.random();

        do {
            angle = 360 * (float) Math.random();
        } while ((int) angle % 90 < 15);

        p3.x = p1.x;
        p3.y = p1.y;
        p4.x = p1.x;
        p4.y = p1.y;
        calc();
    }

    protected void calc() {
        if (angle < 90.0f) {
            angle2 = atan(p1.y / (width - p1.x));
            if (angle > angle2) {
                maxRadius = p1.y / cos(90.0f - angle);
                p2.x = (float) (p1.x + maxRadius * cos(angle));
                p2.y = 0.0f;
            } else {
                maxRadius = (width - p1.x) / cos(angle);
                p2.x = width;
                p2.y = (float) (p1.y - maxRadius * sin(angle));
            }
        } else if (angle < 180.0f) {
            angle2 = 180.0f - atan(p1.y / p1.x);
            if (angle > angle2) {
                maxRadius = p1.x / cos(180.0f - angle);
                p2.x = 0.0f;
                p2.y = (float) (p1.y - maxRadius * sin(angle));
            } else {
                maxRadius = p1.y / cos(angle - 90.0f);
                p2.x = (float) (p1.x + maxRadius * cos(angle));
                p2.y = 0.0f;
            }
        } else if (angle < 270.0f) {
            angle2 = 270.0f - atan(p1.x / (height - p1.y));
            if (angle > angle2) {
                maxRadius = (height - p1.y) / cos(270.0f - angle);
                p2.x = (float) (p1.x + maxRadius * cos(angle));
                p2.y = height;
            } else {
                maxRadius = p1.x / cos(angle - 180.0f);
                p2.x = 0.0f;
                p2.y = (float) (p1.y - maxRadius * sin(angle));
            }
        } else {
            angle2 = 360.0f - atan((height - p1.y) / (width - p1.x));
            if (angle > angle2) {
                maxRadius = (width - p1.x) / cos(360.0f - angle);
                p2.x = width;
                p2.y = (float) (p1.y - maxRadius * sin(angle));
            } else {
                maxRadius = (height - p1.y) / cos(angle - 270.0f);
                p2.x = (float) (p1.x + maxRadius * cos(angle));
                p2.y = height;
            }
        }

        Log.d(TAG, "width = " + width + ", height = " + height + ", angle = " + angle + ", angle2 = " + angle2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x, y;
        radius += 10;
        if(radius >= maxRadius) {
            radius = 0;
            p4.x = p3.x;
            p4.y = p3.y;
            p3.x = p1.x;
            p3.y = p1.y;
            p1.x = p2.x;
            p1.y = p2.y;
            if(angle < 90) {
                if(angle > angle2) {
                    angle = 360 - (90 - (90 - angle));
                } else {
                    angle = (90 - angle) + 90;
                }
            } else if(angle < 180) {
                if(angle > angle2) {
                    angle = 90 - (90 - (180 - angle));
                } else {
                    angle = 90 - (angle - 90) + 180;
                }
            } else if(angle < 270) {
                if(angle > angle2) {
                    angle = 180 - (90 - (270 - angle));
                } else {
                    angle = 270 + (90 - (angle - 180));
                }
            } else {
                if(angle > angle2) {
                    angle = 270 - (90 - (360 - angle));
                } else {
                    angle = 90 - (angle - 270);
                }
            }
            calc();

            x = p1.x;
            y = p1.y;
        } else {
			x = (float) (p1.x + radius * cos(angle));
			y = (float) (p1.y - radius * sin(angle));
        }

        Paint paint = new Paint();
        paint.setColor(Color.argb(0x33, 0xff, 0x22, 0x00));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(p4.x + circleRadius, p4.y + circleRadius, circleRadius, paint);
        canvas.drawCircle(p3.x + circleRadius, p3.y + circleRadius, circleRadius, paint);
        paint.setColor(Color.argb(0x7f, 0xff, 0x22, 0x00));
        canvas.drawCircle(p1.x + circleRadius, p1.y + circleRadius, circleRadius, paint);
        paint.setColor(Color.argb(0xcc, 0xff, 0x22, 0x00));
        canvas.drawCircle(p2.x + circleRadius, p2.y + circleRadius, circleRadius, paint);

        paint.setColor(Color.argb(0xff, 0xff, 0x22, 0x00));

        Path path = new Path();
        path.moveTo(p4.x + circleRadius, p4.y + circleRadius);
        path.lineTo(p3.x + circleRadius, p3.y + circleRadius);
        path.lineTo(p1.x + circleRadius, p1.y + circleRadius);
        path.lineTo(p2.x + circleRadius, p2.y + circleRadius);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x + circleRadius, y + circleRadius, circleRadius, paint);
    }

    protected double atan(double a) {
        return Math.atan(a) * 180.0f / Math.PI;
    }

    protected double cos(double a) {
        return Math.cos(a * Math.PI / 180.0f);
    }

    protected double sin(double a) {
        return Math.sin(a * Math.PI / 180.0f);
    }

    @Override
    public void onClick(View view) {
        init();
        invalidate();
    }
}
