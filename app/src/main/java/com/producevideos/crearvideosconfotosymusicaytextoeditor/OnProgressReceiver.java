package com.producevideos.crearvideosconfotosymusicaytextoeditor;

public interface OnProgressReceiver {
    void onImageProgressFrameUpdate(float f);

    void onProgressFinish(String str);

    void onVideoProgressFrameUpdate(float f);
}
