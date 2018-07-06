package com.github.talent518.acview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ProgressBarView extends View {
	private int mProgress = 50;
	private Bitmap mDstBitmap, mDstTextBitmap, mSrcBitmap;
	private Canvas mDstCanvas, mDstTextCanvas, mSrcCanvas;

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

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		if (progress < 0) {
			if (mProgress == 0)
				return;
			else
				progress = 0;
		} else if (progress > 100) {
			if (mProgress == 100) {
				return;
			} else
				progress = 100;
		} else if (mProgress == progress) {
			return;
		}

		Log.i(getClass().getSimpleName(), "setProgress: " + progress);

		mProgress = progress;
		invalidate();
	}

	private void makeProgressBitmap(boolean isXor) {
		int w = mSrcBitmap.getWidth();
		int h = mSrcBitmap.getHeight();
		Canvas canvas = mSrcCanvas;

		float progress = h * mProgress / 100.0f;

		// 清空位图
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		canvas.drawPaint(paint);

		paint = new Paint();
		paint.setColor(isXor ? Color.WHITE : 0x88000000);
		paint.setStyle(Paint.Style.FILL);

		canvas.drawRect(new RectF(0, 0, w, h - progress), paint);

		paint.setColor(isXor ? 0x88000000 : Color.WHITE);
		canvas.drawRect(new RectF(0, h - progress, w, h), paint);

		canvas.save();
	}

	private void makeRoundBitmap() {
		int w = mDstBitmap.getWidth();
		int h = mDstBitmap.getHeight();
		Canvas canvas = mDstCanvas;
		float corners = Math.min(w / 2.0f, h / 2.0f);
		Rect rect = new Rect(0, 0, w, h);
		RectF rectF = new RectF(rect);

		// 清空位图
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		canvas.drawPaint(paint);

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		canvas.drawRoundRect(rectF, corners, corners, paint);

		makeProgressBitmap(false);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(mSrcBitmap, null, rect, paint);

		canvas.save();
	}

	private void makeTextBitmap() {
		int w = mDstTextBitmap.getWidth();
		int h = mDstTextBitmap.getHeight();
		Canvas canvas = mDstTextCanvas;
		Rect rect = new Rect(0, 0, w, h);

		// 清空位图
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		canvas.drawPaint(paint);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(w/2.2f);
		paint.setTextAlign(Paint.Align.CENTER);

		canvas.drawText(Integer.toString(mProgress), w / 2.0f, h / 2.0f + paint.getTextSize() / 3.0f, paint);

		makeProgressBitmap(true);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(mSrcBitmap, null, rect, paint);

		canvas.save();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mDstBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mDstCanvas = new Canvas(mDstBitmap);
		mDstTextBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mDstTextCanvas = new Canvas(mDstTextBitmap);
		mSrcBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mSrcCanvas = new Canvas(mSrcBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);

		makeRoundBitmap();
		canvas.drawBitmap(mDstBitmap, 0, 0, null);

		makeTextBitmap();
		canvas.drawBitmap(mDstTextBitmap, 0, 0, null);
	}

}
