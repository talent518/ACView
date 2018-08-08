package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RoundRectLinearGradientRotateView extends View implements View.OnClickListener {
	private static final String TAG = RoundRectLinearGradientRotateView.class.getSimpleName();
	private static final long DELAY = 50;
	private boolean isPerimeter = true;
	private float mAngle = 0, mStep = 1;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			sendEmptyMessageDelayed(0, DELAY);

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

		mHandler.sendEmptyMessageDelayed(0, DELAY);

		setOnClickListener(this);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = w;
		mHeight = h;
		mMin = Math.min(mWidth, mHeight);

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
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawColor(0xFFFFFFFF);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(mMin / 20.0f);
		paint.setAntiAlias(true);
		paint.setColor(0xFF38EDFB);

		final float lw = paint.getStrokeWidth() / 2.0f;
		RectF rectF = new RectF(lw, lw, mWidth - lw, mHeight - lw);
		PointF p1 = getPointF(mAngle), p2 = getPointF(isPerimeter ? mAngle + mWidth + mHeight : mAngle + 180);

		Log.i(TAG, "mAngle = " + mAngle + ", p1 = " + p1 + ", p2 = " + p2);

		paint.setShader(new LinearGradient(p1.x, p1.y, p2.x, p2.y, new int[]{0xFF9C82FF, 0xFF38EDFB}, null, Shader.TileMode.CLAMP));
		canvas.drawRoundRect(rectF, mMin / 2.0f, mMin / 2.0f, paint);

		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
		paint.setColor(0xFF000000);
		canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
	}

	protected PointF getPointF(float angle) {
		PointF pointF = new PointF();
		if (isPerimeter) {
			float n = angle;
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
		} else {
			float radius = 0;
			if ((angle > mProtectedAngle && angle < 180 - mProtectedAngle * 2) || (angle > 180 + mProtectedAngle && angle < 360 - mProtectedAngle)) {
				radius = (float) (mHeight / 2.0f / Math.sin(angle * Math.PI / 180.0f));
			} else {
				radius = (float) (mWidth / 2.0f / Math.cos(angle * Math.PI / 180.0f));
			}

			if (radius < 0) {
				radius = -radius;
			}

			pointF.x = (float) (mWidth / 2.0f + radius * Math.cos(angle * Math.PI / 180.0f));
			pointF.y = (float) (mHeight / 2.0f - radius * Math.sin(angle * Math.PI / 180.0f));
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
