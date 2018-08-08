package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SweepGradientRotateView extends View {
	private static final String TAG = SweepGradientRotateView.class.getSimpleName();
	private static final long DELAY = 200;
	private int[] mColors = new int[]{
		0XFFAB2CF7, 0XFF6B7BF6, 0XFF2CC8F7, 0XFF000000
	};
	private float[] mPositions = new float[]{
		0, 0.5f, 0.8f, 1
	};
	private int mAngle = 0;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			sendEmptyMessageDelayed(0, DELAY);

			mAngle = (mAngle + 36) % 360;

			invalidate();
		}
	};
	private float mWidth, mHeight, mMin;

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

		mHandler.sendEmptyMessageDelayed(0, DELAY);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = w;
		mHeight = h;
		mMin = Math.min(mWidth, mHeight);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

		canvas.drawColor(Color.TRANSPARENT);

		Paint paint = new Paint();
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
	}

}
