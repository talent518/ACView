package com.github.talent518.acview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.github.talent518.acview.views.ProgressBarView;
import com.github.talent518.acview.views.ProgressView;

public class MainActivity extends AppCompatActivity {

	private ProgressView mProgressView;
	private ProgressBarView mProgressBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressView = findViewById(R.id.pv_progress);
		mProgressView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mProgressView.getProgress() == -1) {
					float progress = (float) Math.random();
					mProgressView.setmProgressLabel(Float.toString((int) (progress * 1000) / 10.0f) + "%");
					mProgressView.setProgress(progress);
				} else {
					mProgressView.play();
				}
			}
		});

		mProgressBarView = findViewById(R.id.pbv_progress);
		mProgressBarView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mProgressBarView.setProgress(100-(int) ((double) (event.getY() * 100.0f) / (double) mProgressBarView.getMeasuredHeight()));
				return true;
			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			mProgressBarView.setProgress(mProgressBarView.getProgress() + 1);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			mProgressBarView.setProgress(mProgressBarView.getProgress() - 1);
		}

		return super.onKeyUp(keyCode, event);
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();

		System.exit(0);
	}

}
