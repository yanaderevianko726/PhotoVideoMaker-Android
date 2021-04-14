package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

class PinView extends View {
    private static final float DEFAULT_THUMB_RADIUS_DP = 14.0f;
    private static final float MINIMUM_TARGET_RADIUS_DP = 24.0f;
    private IRangeBarFormatter formatter;
    private Rect mBounds = new Rect();
    private Paint mCirclePaint;
    private float mCircleRadiusPx;
    private float mDensity;
    private boolean mHasBeenPressed = false;
    private boolean mIsPressed = false;
    private float mMaxPinFont = 24.0f;
    private float mMinPinFont = 8.0f;
    private Drawable mPin;
    private ColorFilter mPinFilter;
    private float mPinPadding;
    private int mPinRadiusPx;
    private boolean mPinsAreTemporary;
    private Resources mRes;
    private float mTargetRadiusPx;
    private Paint mTextPaint;
    private float mTextYPadding;
    private String mValue;
    private float mX;
    private float mY;

    public PinView(Context context) {
        super(context);
    }

    public void setFormatter(IRangeBarFormatter mFormatter) {
        this.formatter = mFormatter;
    }

    public void init(Context ctx, float y, float pinRadiusDP, int pinColor, int textColor, float circleRadius, int circleColor, float minFont, float maxFont, boolean pinsAreTemporary) {
        this.mRes = ctx.getResources();
        this.mPin = ContextCompat.getDrawable(ctx, R.drawable.rotate);
        this.mDensity = getResources().getDisplayMetrics().density;
        this.mMinPinFont = minFont / this.mDensity;
        this.mMaxPinFont = maxFont / this.mDensity;
        this.mPinsAreTemporary = pinsAreTemporary;
        this.mPinPadding = (float) ((int) TypedValue.applyDimension(1, 15.0f, this.mRes.getDisplayMetrics()));
        this.mCircleRadiusPx = circleRadius;
        this.mTextYPadding = (float) ((int) TypedValue.applyDimension(1, 3.5f, this.mRes.getDisplayMetrics()));
        if (pinRadiusDP == -1.0f) {
            this.mPinRadiusPx = (int) TypedValue.applyDimension(1, DEFAULT_THUMB_RADIUS_DP, this.mRes.getDisplayMetrics());
        } else {
            this.mPinRadiusPx = (int) TypedValue.applyDimension(1, pinRadiusDP, this.mRes.getDisplayMetrics());
        }
        int textSize = (int) TypedValue.applyDimension(2, 15.0f, this.mRes.getDisplayMetrics());
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(textColor);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize((float) textSize);
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setColor(circleColor);
        this.mCirclePaint.setAntiAlias(true);
        this.mPinFilter = new LightingColorFilter(pinColor, pinColor);
        this.mTargetRadiusPx = TypedValue.applyDimension(1, (float) ((int) Math.max(24.0f, (float) this.mPinRadiusPx)), this.mRes.getDisplayMetrics());
        this.mY = y;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public float getX() {
        return this.mX;
    }

    public void setXValue(String x) {
        this.mValue = x;
    }

    public boolean isPressed() {
        return this.mIsPressed;
    }

    public void press() {
        this.mIsPressed = true;
        this.mHasBeenPressed = true;
    }

    public void setSize(float size, float padding) {
        this.mPinPadding = (float) ((int) padding);
        this.mPinRadiusPx = (int) size;
        invalidate();
    }

    public void release() {
        this.mIsPressed = false;
    }

    public boolean isInTargetZone(float x, float y) {
        return Math.abs(x - this.mX) <= this.mTargetRadiusPx && Math.abs((y - this.mY) + this.mPinPadding) <= this.mTargetRadiusPx;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(this.mX, this.mY, this.mCircleRadiusPx, this.mCirclePaint);
        if (this.mPinRadiusPx > 0 && (this.mHasBeenPressed || !this.mPinsAreTemporary)) {
            this.mBounds.set(((int) this.mX) - this.mPinRadiusPx, (((int) this.mY) - (this.mPinRadiusPx * 2)) - ((int) this.mPinPadding), ((int) this.mX) + this.mPinRadiusPx, ((int) this.mY) - ((int) this.mPinPadding));
            this.mPin.setBounds(this.mBounds);
            String text = this.mValue;
            if (this.formatter != null) {
                text = this.formatter.format(text);
            }
            calibrateTextSize(this.mTextPaint, text, (float) this.mBounds.width());
            this.mTextPaint.getTextBounds(text, 0, text.length(), this.mBounds);
            this.mTextPaint.setTextAlign(Align.CENTER);
            this.mPin.setColorFilter(this.mPinFilter);
            this.mPin.draw(canvas);
            canvas.drawText(text, this.mX, ((this.mY - ((float) this.mPinRadiusPx)) - this.mPinPadding) + this.mTextYPadding, this.mTextPaint);
        }
        super.draw(canvas);
    }

    private void calibrateTextSize(Paint paint, String text, float boxWidth) {
        paint.setTextSize(10.0f);
        float estimatedFontSize = ((8.0f * boxWidth) / paint.measureText(text)) / this.mDensity;
        if (estimatedFontSize < this.mMinPinFont) {
            estimatedFontSize = this.mMinPinFont;
        } else if (estimatedFontSize > this.mMaxPinFont) {
            estimatedFontSize = this.mMaxPinFont;
        }
        paint.setTextSize(this.mDensity * estimatedFontSize);
    }
}
