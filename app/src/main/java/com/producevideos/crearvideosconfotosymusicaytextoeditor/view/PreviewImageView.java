package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;

@SuppressLint({"AppCompatCustomView"})
public class PreviewImageView extends ImageView {
    public static int mAspectRatioHeight = 360;
    public static int mAspectRatioWidth = 640;

    public PreviewImageView(Context context) {
        super(context);
    }

    public PreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);
    }

    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    @SuppressLint({"NewApi"})
    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, attrs);
    }

    private void Init(Context context, AttributeSet attrs) {
        mAspectRatioWidth = MyApplication.VIDEO_WIDTH;
        mAspectRatioHeight = MyApplication.VIDEO_HEIGHT;
    }

    @SuppressLint({"WrongConstant"})
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth;
        int finalHeight;
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = (int) (((float) (mAspectRatioHeight * originalWidth)) / ((float) mAspectRatioWidth));
        if (calculatedHeight > originalHeight) {
            finalWidth = (int) (((float) (mAspectRatioWidth * originalHeight)) / ((float) mAspectRatioHeight));
            finalHeight = originalHeight;
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(finalWidth, 1073741824), MeasureSpec.makeMeasureSpec(finalHeight, 1073741824));
    }
}
