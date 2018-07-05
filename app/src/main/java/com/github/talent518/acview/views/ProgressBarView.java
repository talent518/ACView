package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ProgressBarView extends View {
	private float mProgress = 0.5f;

	public ProgressBarView(Context context) {
		super(context);
	}

	public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public float getProgress() {
		return mProgress;
	}

	public void setProgress(float progress) {
		if (progress <= 0 || progress > 1) {
			return;
		}

		mProgress = progress;
		invalidate();
	}

	protected Bitmap makeProgressBitmap(int w, int h) {
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		float progress = canvas.getHeight() * mProgress;
		canvas.drawColor(Color.TRANSPARENT);

		Paint paint = new Paint();
		paint.setColor(0x88000000);
		paint.setStyle(Paint.Style.FILL);

		canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight() - progress), paint);

		paint.setColor(Color.WHITE);
		canvas.drawRect(new RectF(0, canvas.getHeight() - progress, canvas.getWidth(), canvas.getHeight()), paint);

		return bitmap;
	}

	protected Bitmap makeRoundBitmap(int w, int h) {
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		float corners = Math.min(w / 2.0f, h / 2.0f);
		Rect rect = new Rect(0, 0, w, h);
		RectF rectF = new RectF(rect);

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawRoundRect(rectF, corners, corners, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(makeProgressBitmap(w, h), null, rect, paint);

		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();

		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawBitmap(makeRoundBitmap(w, h), 0, 0, null);
	}

}
