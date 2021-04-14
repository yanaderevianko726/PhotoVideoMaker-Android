package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MyVideoView extends VideoView {
    PlayPauseListner listner;

    public interface PlayPauseListner {
        void onVideoPause();

        void onVideoPlay();
    }

    public void setOnPlayPauseListner(PlayPauseListner listner) {
        this.listner = listner;
    }

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void start() {
        super.start();
        if (this.listner != null) {
            this.listner.onVideoPlay();
        }
    }

    public void pause() {
        super.pause();
        if (this.listner != null) {
            this.listner.onVideoPause();
        }
    }
}
