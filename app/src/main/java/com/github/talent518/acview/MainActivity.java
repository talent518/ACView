package com.github.talent518.acview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.talent518.acview.views.ProgressView;

public class MainActivity extends AppCompatActivity {

    private ProgressView mProgressView;

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.exit(0);
    }

}
