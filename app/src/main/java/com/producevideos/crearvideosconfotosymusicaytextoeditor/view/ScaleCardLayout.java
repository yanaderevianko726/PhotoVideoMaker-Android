package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

public class ScaleCardLayout extends CardView {
    public int mAspectRatioHeight = 360;
    public int mAspectRatioWidth = 640;

    public ScaleCardLayout(Context context) {
        super(context);
    }

    public ScaleCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);
    }

    public ScaleCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    @SuppressLint({"ResourceType"})
    private void Init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleCardLayout);
        this.mAspectRatioWidth = a.getInt(0, MyApplication.VIDEO_WIDTH);
        this.mAspectRatioHeight = a.getInt(1, MyApplication.VIDEO_HEIGHT);
        a.recycle();
    }

    @SuppressLint({"WrongConstant"})
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth;
        int finalHeight;
        if (this.mAspectRatioHeight == this.mAspectRatioWidth) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = (int) (((float) (this.mAspectRatioHeight * originalWidth)) / ((float) this.mAspectRatioWidth));
        if (calculatedHeight > originalHeight) {
            finalWidth = (int) (((float) (this.mAspectRatioWidth * originalHeight)) / ((float) this.mAspectRatioHeight));
            finalHeight = originalHeight;
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(finalWidth, 1073741824), MeasureSpec.makeMeasureSpec(finalHeight, 1073741824));
    }
}
