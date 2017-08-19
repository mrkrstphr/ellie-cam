package com.grcodingcompany.elliecam;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class CameraFeedImageView extends ImageView {

    Camera mCamera;
    Timer refreshTimer;

    public CameraFeedImageView(Context context) {
        super(context);
        startTimer();
    }

    public CameraFeedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        startTimer();
    }

    public CameraFeedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startTimer();
    }

    public CameraFeedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        startTimer();
    }

    public void setCamera(Camera camera)
    {
        mCamera = camera;
    }

    private void startTimer()
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fetchNewSnapshot();
            }
        };

        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(task, 250, 250);
    }

    private void fetchNewSnapshot() {
        String url = mCamera.url +
                "/snapshot.cgi?user=" + mCamera.username +
                "&pwd=" + mCamera.password;
        new ImageFetcher(CameraFeedImageView.this).execute(url);
    }
}
