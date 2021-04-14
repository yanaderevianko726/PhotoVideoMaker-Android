package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

import java.util.HashMap;

public class RangeBar extends View {
    private static final int DEFAULT_BAR_COLOR = -3355444;
    private static final float DEFAULT_BAR_PADDING_BOTTOM_DP = 24.0f;
    private static final float DEFAULT_BAR_WEIGHT_PX = 2.0f;
    private static final float DEFAULT_CIRCLE_SIZE_DP = 5.0f;
    private static final int DEFAULT_CONNECTING_LINE_COLOR = -12627531;
    private static final float DEFAULT_CONNECTING_LINE_WEIGHT_PX = 4.0f;
    private static final float DEFAULT_EXPANDED_PIN_RADIUS_DP = 12.0f;
    public static final float DEFAULT_MAX_PIN_FONT_SP = 24.0f;
    public static final float DEFAULT_MIN_PIN_FONT_SP = 8.0f;
    private static final int DEFAULT_PIN_COLOR = -12627531;
    private static final float DEFAULT_PIN_PADDING_DP = 16.0f;
    private static final int DEFAULT_TEXT_COLOR = -1;
    private static final int DEFAULT_TICK_COLOR = -16777216;
    private static final float DEFAULT_TICK_END = 5.0f;
    private static final float DEFAULT_TICK_HEIGHT_DP = 1.0f;
    private static final float DEFAULT_TICK_INTERVAL = 1.0f;
    private static final float DEFAULT_TICK_START = 0.0f;
    private static final String TAG = "RangeBar";
    private boolean drawTicks = true;
    private int mActiveBarColor;
    private int mActiveCircleColor;
    private int mActiveConnectingLineColor;
    private int mActiveTickColor;
    private boolean mArePinsTemporary = true;
    private Bar mBar;
    private int mBarColor = DEFAULT_BAR_COLOR;
    private float mBarPaddingBottom = 24.0f;
    private float mBarWeight = DEFAULT_BAR_WEIGHT_PX;
    private int mCircleColor = -12627531;
    private float mCircleSize = 5.0f;
    private ConnectingLine mConnectingLine;
    private int mConnectingLineColor = -12627531;
    private float mConnectingLineWeight = DEFAULT_CONNECTING_LINE_WEIGHT_PX;
    private int mDefaultHeight = 150;
    private int mDefaultWidth = 500;
    private int mDiffX;
    private int mDiffY;
    private float mExpandedPinRadius = DEFAULT_EXPANDED_PIN_RADIUS_DP;
    private boolean mFirstSetTickCount = true;
    private IRangeBarFormatter mFormatter;
    private boolean mIsRangeBar = true;
    private float mLastX;
    private float mLastY;
    private int mLeftIndex;
    private PinView mLeftThumb;
    private OnRangeBarChangeListener mListener;
    private float mMaxPinFont = 24.0f;
    private float mMinPinFont = 8.0f;
    private int mPinColor = -12627531;
    private float mPinPadding = DEFAULT_PIN_PADDING_DP;
    private PinTextFormatter mPinTextFormatter = new C10391();
    private OnRangeBarTextListener mPinTextListener;
    private int mRightIndex;
    private PinView mRightThumb;
    private int mTextColor = -1;
    private float mThumbRadiusDP = DEFAULT_EXPANDED_PIN_RADIUS_DP;
    private int mTickColor = -16777216;
    private int mTickCount = (((int) ((this.mTickEnd - this.mTickStart) / this.mTickInterval)) + 1);
    private float mTickEnd = 5.0f;
    private float mTickHeightDP = 1.0f;
    private float mTickInterval = 1.0f;
    private HashMap<Float, String> mTickMap;
    private float mTickStart = 0.0f;

    public interface OnRangeBarChangeListener {
        void onRangeChangeListener(RangeBar rangeBar, int i, int i2, String str, String str2);
    }

    public interface OnRangeBarTextListener {
        String getPinValue(RangeBar rangeBar, int i);
    }

    public interface PinTextFormatter {
        String getText(String str);
    }

    class C10391 implements PinTextFormatter {
        C10391() {
        }

        public String getText(String value) {
            if (value.length() > 4) {
                return value.substring(0, 4);
            }
            return value;
        }
    }

    public RangeBar(Context context) {
        super(context);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        rangeBarInit(context, attrs);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        rangeBarInit(context, attrs);
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("TICK_COUNT", this.mTickCount);
        bundle.putFloat("TICK_START", this.mTickStart);
        bundle.putFloat("TICK_END", this.mTickEnd);
        bundle.putFloat("TICK_INTERVAL", this.mTickInterval);
        bundle.putInt("TICK_COLOR", this.mTickColor);
        bundle.putFloat("TICK_HEIGHT_DP", this.mTickHeightDP);
        bundle.putFloat("BAR_WEIGHT", this.mBarWeight);
        bundle.putInt("BAR_COLOR", this.mBarColor);
        bundle.putFloat("CONNECTING_LINE_WEIGHT", this.mConnectingLineWeight);
        bundle.putInt("CONNECTING_LINE_COLOR", this.mConnectingLineColor);
        bundle.putFloat("CIRCLE_SIZE", this.mCircleSize);
        bundle.putInt("CIRCLE_COLOR", this.mCircleColor);
        bundle.putFloat("THUMB_RADIUS_DP", this.mThumbRadiusDP);
        bundle.putFloat("EXPANDED_PIN_RADIUS_DP", this.mExpandedPinRadius);
        bundle.putFloat("PIN_PADDING", this.mPinPadding);
        bundle.putFloat("BAR_PADDING_BOTTOM", this.mBarPaddingBottom);
        bundle.putBoolean("IS_RANGE_BAR", this.mIsRangeBar);
        bundle.putBoolean("ARE_PINS_TEMPORARY", this.mArePinsTemporary);
        bundle.putInt("LEFT_INDEX", this.mLeftIndex);
        bundle.putInt("RIGHT_INDEX", this.mRightIndex);
        bundle.putBoolean("FIRST_SET_TICK_COUNT", this.mFirstSetTickCount);
        bundle.putFloat("MIN_PIN_FONT", this.mMinPinFont);
        bundle.putFloat("MAX_PIN_FONT", this.mMaxPinFont);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mTickCount = bundle.getInt("TICK_COUNT");
            this.mTickStart = bundle.getFloat("TICK_START");
            this.mTickEnd = bundle.getFloat("TICK_END");
            this.mTickInterval = bundle.getFloat("TICK_INTERVAL");
            this.mTickColor = bundle.getInt("TICK_COLOR");
            this.mTickHeightDP = bundle.getFloat("TICK_HEIGHT_DP");
            this.mBarWeight = bundle.getFloat("BAR_WEIGHT");
            this.mBarColor = bundle.getInt("BAR_COLOR");
            this.mCircleSize = bundle.getFloat("CIRCLE_SIZE");
            this.mCircleColor = bundle.getInt("CIRCLE_COLOR");
            this.mConnectingLineWeight = bundle.getFloat("CONNECTING_LINE_WEIGHT");
            this.mConnectingLineColor = bundle.getInt("CONNECTING_LINE_COLOR");
            this.mThumbRadiusDP = bundle.getFloat("THUMB_RADIUS_DP");
            this.mExpandedPinRadius = bundle.getFloat("EXPANDED_PIN_RADIUS_DP");
            this.mPinPadding = bundle.getFloat("PIN_PADDING");
            this.mBarPaddingBottom = bundle.getFloat("BAR_PADDING_BOTTOM");
            this.mIsRangeBar = bundle.getBoolean("IS_RANGE_BAR");
            this.mArePinsTemporary = bundle.getBoolean("ARE_PINS_TEMPORARY");
            this.mLeftIndex = bundle.getInt("LEFT_INDEX");
            this.mRightIndex = bundle.getInt("RIGHT_INDEX");
            this.mFirstSetTickCount = bundle.getBoolean("FIRST_SET_TICK_COUNT");
            this.mMinPinFont = bundle.getFloat("MIN_PIN_FONT");
            this.mMaxPinFont = bundle.getFloat("MAX_PIN_FONT");
            setRangePinsByIndices(this.mLeftIndex, this.mRightIndex);
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (measureWidthMode == Integer.MIN_VALUE) {
            width = measureWidth;
        } else if (measureWidthMode == 1073741824) {
            width = measureWidth;
        } else {
            width = this.mDefaultWidth;
        }
        if (measureHeightMode == Integer.MIN_VALUE) {
            height = Math.min(this.mDefaultHeight, measureHeight);
        } else if (measureHeightMode == 1073741824) {
            height = measureHeight;
        } else {
            height = this.mDefaultHeight;
        }
        setMeasuredDimension(width, height);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Context ctx = getContext();
        float expandedPinRadius = this.mExpandedPinRadius / getResources().getDisplayMetrics().density;
        float yPos = ((float) h) - this.mBarPaddingBottom;
        if (this.mIsRangeBar) {
            this.mLeftThumb = new PinView(ctx);
            this.mLeftThumb.setFormatter(this.mFormatter);
            this.mLeftThumb.init(ctx, yPos, expandedPinRadius, this.mPinColor, this.mTextColor, this.mCircleSize, this.mCircleColor, this.mMinPinFont, this.mMaxPinFont, this.mArePinsTemporary);
        }
        this.mRightThumb = new PinView(ctx);
        this.mRightThumb.setFormatter(this.mFormatter);
        this.mRightThumb.init(ctx, yPos, expandedPinRadius, this.mPinColor, this.mTextColor, this.mCircleSize, this.mCircleColor, this.mMinPinFont, this.mMaxPinFont, this.mArePinsTemporary);
        float marginLeft = Math.max(this.mExpandedPinRadius, this.mCircleSize);
        float barLength = ((float) w) - (DEFAULT_BAR_WEIGHT_PX * marginLeft);
        this.mBar = new Bar(ctx, marginLeft, yPos, barLength, this.mTickCount, this.mTickHeightDP, this.mTickColor, this.mBarWeight, this.mBarColor);
        if (this.mIsRangeBar) {
            this.mLeftThumb.setX(((((float) this.mLeftIndex) / ((float) (this.mTickCount - 1))) * barLength) + marginLeft);
            this.mLeftThumb.setXValue(getPinValue(this.mLeftIndex));
        }
        this.mRightThumb.setX(((((float) this.mRightIndex) / ((float) (this.mTickCount - 1))) * barLength) + marginLeft);
        this.mRightThumb.setXValue(getPinValue(this.mRightIndex));
        int newLeftIndex = this.mIsRangeBar ? this.mBar.getNearestTickIndex(this.mLeftThumb) : 0;
        int newRightIndex = this.mBar.getNearestTickIndex(this.mRightThumb);
        if (!((newLeftIndex == this.mLeftIndex && newRightIndex == this.mRightIndex) || this.mListener == null)) {
            this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
        }
        this.mConnectingLine = new ConnectingLine(ctx, yPos, this.mConnectingLineWeight, this.mConnectingLineColor);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mBar.draw(canvas);
        if (this.mIsRangeBar) {
            this.mConnectingLine.draw(canvas, this.mLeftThumb, this.mRightThumb);
            if (this.drawTicks) {
                this.mBar.drawTicks(canvas);
            }
            this.mLeftThumb.draw(canvas);
        } else {
            this.mConnectingLine.draw(canvas, getMarginLeft(), this.mRightThumb);
            if (this.drawTicks) {
                this.mBar.drawTicks(canvas);
            }
        }
        this.mRightThumb.draw(canvas);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                this.mDiffX = 0;
                this.mDiffY = 0;
                this.mLastX = event.getX();
                this.mLastY = event.getY();
                onActionDown(event.getX(), event.getY());
                return true;
            case 1:
                getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp(event.getX(), event.getY());
                return true;
            case 2:
                onActionMove(event.getX());
                getParent().requestDisallowInterceptTouchEvent(true);
                float curX = event.getX();
                float curY = event.getY();
                this.mDiffX = (int) (((float) this.mDiffX) + Math.abs(curX - this.mLastX));
                this.mDiffY = (int) (((float) this.mDiffY) + Math.abs(curY - this.mLastY));
                this.mLastX = curX;
                this.mLastY = curY;
                if (this.mDiffX >= this.mDiffY) {
                    return true;
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            case 3:
                getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp(event.getX(), event.getY());
                return true;
            default:
                return false;
        }
    }

    public void setOnRangeBarChangeListener(OnRangeBarChangeListener listener) {
        this.mListener = listener;
    }

    public void setPinTextListener(OnRangeBarTextListener mPinTextListener) {
        this.mPinTextListener = mPinTextListener;
    }

    public void setFormatter(IRangeBarFormatter formatter) {
        if (this.mLeftThumb != null) {
            this.mLeftThumb.setFormatter(formatter);
        }
        if (this.mRightThumb != null) {
            this.mRightThumb.setFormatter(formatter);
        }
        this.mFormatter = formatter;
    }

    public void setDrawTicks(boolean drawTicks) {
        this.drawTicks = drawTicks;
    }

    public void setTickStart(float tickStart) {
        int tickCount = ((int) ((this.mTickEnd - tickStart) / this.mTickInterval)) + 1;
        if (isValidTickCount(tickCount)) {
            this.mTickCount = tickCount;
            this.mTickStart = tickStart;
            if (this.mFirstSetTickCount) {
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
            if (indexOutOfRange(this.mLeftIndex, this.mRightIndex)) {
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
            createBar();
            createPins();
            return;
        }
        Log.e(TAG, "tickCount less than 2; invalid tickCount.");
        throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
    }

    public void setTickInterval(float tickInterval) {
        int tickCount = ((int) ((this.mTickEnd - this.mTickStart) / tickInterval)) + 1;
        if (isValidTickCount(tickCount)) {
            this.mTickCount = tickCount;
            this.mTickInterval = tickInterval;
            if (this.mFirstSetTickCount) {
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
            if (indexOutOfRange(this.mLeftIndex, this.mRightIndex)) {
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
            createBar();
            createPins();
            return;
        }
        Log.e(TAG, "tickCount less than 2; invalid tickCount.");
        throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
    }

    public void setTickEnd(float tickEnd) {
        int tickCount = ((int) ((tickEnd - this.mTickStart) / this.mTickInterval)) + 1;
        if (isValidTickCount(tickCount)) {
            this.mTickCount = tickCount;
            this.mTickEnd = tickEnd;
            if (this.mFirstSetTickCount) {
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
            if (indexOutOfRange(this.mLeftIndex, this.mRightIndex)) {
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
            createBar();
            createPins();
            return;
        }
        Log.e(TAG, "tickCount less than 2; invalid tickCount.");
        throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
    }

    public void setTickHeight(float tickHeight) {
        this.mTickHeightDP = tickHeight;
        createBar();
    }

    public void setBarWeight(float barWeight) {
        this.mBarWeight = barWeight;
        createBar();
    }

    public void setBarColor(int barColor) {
        this.mBarColor = barColor;
        createBar();
    }

    public void setPinColor(int pinColor) {
        this.mPinColor = pinColor;
        createPins();
    }

    public void setPinTextColor(int textColor) {
        this.mTextColor = textColor;
        createPins();
    }

    public void setRangeBarEnabled(boolean isRangeBar) {
        this.mIsRangeBar = isRangeBar;
        invalidate();
    }

    public void setTemporaryPins(boolean arePinsTemporary) {
        this.mArePinsTemporary = arePinsTemporary;
        invalidate();
    }

    public void setTickColor(int tickColor) {
        this.mTickColor = tickColor;
        createBar();
    }

    public void setSelectorColor(int selectorColor) {
        this.mCircleColor = selectorColor;
        createPins();
    }

    public void setConnectingLineWeight(float connectingLineWeight) {
        this.mConnectingLineWeight = connectingLineWeight;
        createConnectingLine();
    }

    public void setConnectingLineColor(int connectingLineColor) {
        this.mConnectingLineColor = connectingLineColor;
        createConnectingLine();
    }

    public void setPinRadius(float pinRadius) {
        this.mExpandedPinRadius = pinRadius;
        createPins();
    }

    public float getTickStart() {
        return this.mTickStart;
    }

    public float getTickEnd() {
        return this.mTickEnd;
    }

    public int getTickCount() {
        return this.mTickCount;
    }

    public void setRangePinsByIndices(int leftPinIndex, int rightPinIndex) {
        if (indexOutOfRange(leftPinIndex, rightPinIndex)) {
            Log.e(TAG, "Pin index left " + leftPinIndex + ", or right " + rightPinIndex + " is out of bounds. Check that it is greater than the minimum (" + this.mTickStart + ") and less than the maximum value (" + this.mTickEnd + ")");
            throw new IllegalArgumentException("Pin index left " + leftPinIndex + ", or right " + rightPinIndex + " is out of bounds. Check that it is greater than the minimum (" + this.mTickStart + ") and less than the maximum value (" + this.mTickEnd + ")");
        }
        if (this.mFirstSetTickCount) {
            this.mFirstSetTickCount = false;
        }
        this.mLeftIndex = leftPinIndex;
        this.mRightIndex = rightPinIndex;
        createPins();
        if (this.mListener != null) {
            this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
        }
        invalidate();
        requestLayout();
    }

    public void setSeekPinByIndex(int pinIndex) {
        if (pinIndex < 0 || pinIndex > this.mTickCount) {
            Log.e(TAG, "Pin index " + pinIndex + " is out of bounds. Check that it is greater than the minimum (" + 0 + ") and less than the maximum value (" + this.mTickCount + ")");
            throw new IllegalArgumentException("Pin index " + pinIndex + " is out of bounds. Check that it is greater than the minimum (" + 0 + ") and less than the maximum value (" + this.mTickCount + ")");
        }
        if (this.mFirstSetTickCount) {
            this.mFirstSetTickCount = false;
        }
        this.mRightIndex = pinIndex;
        createPins();
        if (this.mListener != null) {
            this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
        }
        invalidate();
        requestLayout();
    }

    public void setRangePinsByValue(float leftPinValue, float rightPinValue) {
        if (valueOutOfRange(leftPinValue, rightPinValue)) {
            Log.e(TAG, "Pin value left " + leftPinValue + ", or right " + rightPinValue + " is out of bounds. Check that it is greater than the minimum (" + this.mTickStart + ") and less than the maximum value (" + this.mTickEnd + ")");
            throw new IllegalArgumentException("Pin value left " + leftPinValue + ", or right " + rightPinValue + " is out of bounds. Check that it is greater than the minimum (" + this.mTickStart + ") and less than the maximum value (" + this.mTickEnd + ")");
        }
        if (this.mFirstSetTickCount) {
            this.mFirstSetTickCount = false;
        }
        this.mLeftIndex = (int) ((leftPinValue - this.mTickStart) / this.mTickInterval);
        this.mRightIndex = (int) ((rightPinValue - this.mTickStart) / this.mTickInterval);
        createPins();
        if (this.mListener != null) {
            this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
        }
        invalidate();
        requestLayout();
    }

    public void setSeekPinByValue(float pinValue) {
        if (pinValue > this.mTickEnd || pinValue < this.mTickStart) {
            Log.e(TAG, "Pin value " + pinValue + " is out of bounds. Check that it is greater than the minimum (" + this.mTickStart + ") and less than the maximum value (" + this.mTickEnd + ")");
            throw new IllegalArgumentException("Pin value " + pinValue + " is out of bounds. Check that it is greater than the minimum (" + this.mTickStart + ") and less than the maximum value (" + this.mTickEnd + ")");
        }
        if (this.mFirstSetTickCount) {
            this.mFirstSetTickCount = false;
        }
        this.mRightIndex = (int) ((pinValue - this.mTickStart) / this.mTickInterval);
        createPins();
        if (this.mListener != null) {
            this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
        }
        invalidate();
        requestLayout();
    }

    public boolean isRangeBar() {
        return this.mIsRangeBar;
    }

    public String getLeftPinValue() {
        return getPinValue(this.mLeftIndex);
    }

    public String getRightPinValue() {
        return getPinValue(this.mRightIndex);
    }

    public int getLeftIndex() {
        return this.mLeftIndex;
    }

    public int getRightIndex() {
        return this.mRightIndex;
    }

    public double getTickInterval() {
        return (double) this.mTickInterval;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.mBarColor = this.mActiveBarColor;
            this.mConnectingLineColor = this.mActiveConnectingLineColor;
            this.mCircleColor = this.mActiveCircleColor;
            this.mTickColor = this.mActiveTickColor;
        } else {
            this.mBarColor = DEFAULT_BAR_COLOR;
            this.mConnectingLineColor = DEFAULT_BAR_COLOR;
            this.mCircleColor = DEFAULT_BAR_COLOR;
            this.mTickColor = DEFAULT_BAR_COLOR;
        }
        createBar();
        createPins();
        createConnectingLine();
        super.setEnabled(enabled);
    }

    public void setPinTextFormatter(PinTextFormatter pinTextFormatter) {
        this.mPinTextFormatter = pinTextFormatter;
    }

    @SuppressLint({"ResourceType"})
    private void rangeBarInit(Context context, AttributeSet attrs) {
        if (this.mTickMap == null) {
            this.mTickMap = new HashMap();
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RangeBar, 0, 0);
        try {
            float tickStart = ta.getFloat(0, 0.0f);
            float tickEnd = ta.getFloat(1, 5.0f);
            float tickInterval = ta.getFloat(2, 1.0f);
            int tickCount = ((int) ((tickEnd - tickStart) / tickInterval)) + 1;
            if (isValidTickCount(tickCount)) {
                this.mTickCount = tickCount;
                this.mTickStart = tickStart;
                this.mTickEnd = tickEnd;
                this.mTickInterval = tickInterval;
                this.mLeftIndex = 0;
                this.mRightIndex = this.mTickCount - 1;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            } else {
                Log.e(TAG, "tickCount less than 2; invalid tickCount. XML input ignored.");
            }
            this.mTickHeightDP = ta.getDimension(3, 1.0f);
            this.mBarWeight = ta.getDimension(5, DEFAULT_BAR_WEIGHT_PX);
            this.mBarColor = ta.getColor(6, DEFAULT_BAR_COLOR);
            this.mTextColor = ta.getColor(7, -1);
            this.mPinColor = ta.getColor(8, -12627531);
            this.mActiveBarColor = this.mBarColor;
            this.mCircleSize = ta.getDimension(9, TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
            this.mCircleColor = ta.getColor(14, -12627531);
            this.mActiveCircleColor = this.mCircleColor;
            this.mTickColor = ta.getColor(4, -16777216);
            this.mActiveTickColor = this.mTickColor;
            this.mConnectingLineWeight = ta.getDimension(17, DEFAULT_CONNECTING_LINE_WEIGHT_PX);
            this.mConnectingLineColor = ta.getColor(18, -12627531);
            this.mActiveConnectingLineColor = this.mConnectingLineColor;
            this.mExpandedPinRadius = ta.getDimension(19, TypedValue.applyDimension(1, DEFAULT_EXPANDED_PIN_RADIUS_DP, getResources().getDisplayMetrics()));
            this.mPinPadding = ta.getDimension(10, TypedValue.applyDimension(1, DEFAULT_PIN_PADDING_DP, getResources().getDisplayMetrics()));
            this.mBarPaddingBottom = ta.getDimension(13, TypedValue.applyDimension(1, 24.0f, getResources().getDisplayMetrics()));
            this.mIsRangeBar = ta.getBoolean(15, true);
            this.mArePinsTemporary = ta.getBoolean(16, true);
            float density = getResources().getDisplayMetrics().density;
            this.mMinPinFont = ta.getDimension(11, 8.0f * density);
            this.mMaxPinFont = ta.getDimension(12, 24.0f * density);
            this.mIsRangeBar = ta.getBoolean(15, true);
        } finally {
            ta.recycle();
        }
    }

    private void createBar() {
        this.mBar = new Bar(getContext(), getMarginLeft(), getYPos(), getBarLength(), this.mTickCount, this.mTickHeightDP, this.mTickColor, this.mBarWeight, this.mBarColor);
        invalidate();
    }

    private void createConnectingLine() {
        this.mConnectingLine = new ConnectingLine(getContext(), getYPos(), this.mConnectingLineWeight, this.mConnectingLineColor);
        invalidate();
    }

    private void createPins() {
        Context ctx = getContext();
        float yPos = getYPos();
        if (this.mIsRangeBar) {
            this.mLeftThumb = new PinView(ctx);
            this.mLeftThumb.init(ctx, yPos, 0.0f, this.mPinColor, this.mTextColor, this.mCircleSize, this.mCircleColor, this.mMinPinFont, this.mMaxPinFont, false);
        }
        this.mRightThumb = new PinView(ctx);
        this.mRightThumb.init(ctx, yPos, 0.0f, this.mPinColor, this.mTextColor, this.mCircleSize, this.mCircleColor, this.mMinPinFont, this.mMaxPinFont, false);
        float marginLeft = getMarginLeft();
        float barLength = getBarLength();
        if (this.mIsRangeBar) {
            this.mLeftThumb.setX(((((float) this.mLeftIndex) / ((float) (this.mTickCount - 1))) * barLength) + marginLeft);
            this.mLeftThumb.setXValue(getPinValue(this.mLeftIndex));
        }
        this.mRightThumb.setX(((((float) this.mRightIndex) / ((float) (this.mTickCount - 1))) * barLength) + marginLeft);
        this.mRightThumb.setXValue(getPinValue(this.mRightIndex));
        invalidate();
    }

    private float getMarginLeft() {
        return Math.max(this.mExpandedPinRadius, this.mCircleSize);
    }

    private float getYPos() {
        return ((float) getHeight()) - this.mBarPaddingBottom;
    }

    private float getBarLength() {
        return ((float) getWidth()) - (DEFAULT_BAR_WEIGHT_PX * getMarginLeft());
    }

    private boolean indexOutOfRange(int leftThumbIndex, int rightThumbIndex) {
        return leftThumbIndex < 0 || leftThumbIndex >= this.mTickCount || rightThumbIndex < 0 || rightThumbIndex >= this.mTickCount;
    }

    private boolean valueOutOfRange(float leftThumbValue, float rightThumbValue) {
        return leftThumbValue < this.mTickStart || leftThumbValue > this.mTickEnd || rightThumbValue < this.mTickStart || rightThumbValue > this.mTickEnd;
    }

    private boolean isValidTickCount(int tickCount) {
        return tickCount > 1;
    }

    private void onActionDown(float x, float y) {
        if (this.mIsRangeBar) {
            if (!this.mRightThumb.isPressed() && this.mLeftThumb.isInTargetZone(x, y)) {
                pressPin(this.mLeftThumb);
            } else if (!this.mLeftThumb.isPressed() && this.mRightThumb.isInTargetZone(x, y)) {
                pressPin(this.mRightThumb);
            }
        } else if (this.mRightThumb.isInTargetZone(x, y)) {
            pressPin(this.mRightThumb);
        }
    }

    private void onActionUp(float x, float y) {
        if (this.mIsRangeBar && this.mLeftThumb.isPressed()) {
            releasePin(this.mLeftThumb);
        } else if (this.mRightThumb.isPressed()) {
            releasePin(this.mRightThumb);
        } else {
            if ((this.mIsRangeBar ? Math.abs(this.mLeftThumb.getX() - x) : 0.0f) >= Math.abs(this.mRightThumb.getX() - x)) {
                this.mRightThumb.setX(x);
                releasePin(this.mRightThumb);
            } else if (this.mIsRangeBar) {
                this.mLeftThumb.setX(x);
                releasePin(this.mLeftThumb);
            }
            int newLeftIndex = this.mIsRangeBar ? this.mBar.getNearestTickIndex(this.mLeftThumb) : 0;
            int newRightIndex = this.mBar.getNearestTickIndex(this.mRightThumb);
            if (newLeftIndex != this.mLeftIndex || newRightIndex != this.mRightIndex) {
                this.mLeftIndex = newLeftIndex;
                this.mRightIndex = newRightIndex;
                if (this.mListener != null) {
                    this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
                }
            }
        }
    }

    private void onActionMove(float x) {
        if (this.mIsRangeBar && this.mLeftThumb.isPressed()) {
            movePin(this.mLeftThumb, x);
        } else if (this.mRightThumb.isPressed()) {
            movePin(this.mRightThumb, x);
        }
        if (this.mIsRangeBar && this.mLeftThumb.getX() > this.mRightThumb.getX()) {
            PinView temp = this.mLeftThumb;
            this.mLeftThumb = this.mRightThumb;
            this.mRightThumb = temp;
        }
        int newLeftIndex = this.mIsRangeBar ? this.mBar.getNearestTickIndex(this.mLeftThumb) : 0;
        int newRightIndex = this.mBar.getNearestTickIndex(this.mRightThumb);
        int componentLeft = getLeft() + getPaddingLeft();
        int componentRight = (getRight() - getPaddingRight()) - componentLeft;
        if (x <= ((float) componentLeft)) {
            newLeftIndex = 0;
            movePin(this.mLeftThumb, this.mBar.getLeftX());
        } else if (x >= ((float) componentRight)) {
            newRightIndex = getTickCount() - 1;
            movePin(this.mRightThumb, this.mBar.getRightX());
        }
        if (newLeftIndex != this.mLeftIndex || newRightIndex != this.mRightIndex) {
            this.mLeftIndex = newLeftIndex;
            this.mRightIndex = newRightIndex;
            if (this.mIsRangeBar) {
                this.mLeftThumb.setXValue(getPinValue(this.mLeftIndex));
            }
            this.mRightThumb.setXValue(getPinValue(this.mRightIndex));
            if (this.mListener != null) {
                this.mListener.onRangeChangeListener(this, this.mLeftIndex, this.mRightIndex, getPinValue(this.mLeftIndex), getPinValue(this.mRightIndex));
            }
        }
    }

    private void pressPin(final PinView thumb) {
        if (this.mFirstSetTickCount) {
            this.mFirstSetTickCount = false;
        }
        if (this.mArePinsTemporary) {
            ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, this.mExpandedPinRadius});
            animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    RangeBar.this.mThumbRadiusDP = ((Float) animation.getAnimatedValue()).floatValue();
                    thumb.setSize(RangeBar.this.mThumbRadiusDP, RangeBar.this.mPinPadding * animation.getAnimatedFraction());
                    RangeBar.this.invalidate();
                }
            });
            animator.start();
        }
        thumb.press();
    }

    private void releasePin(final PinView thumb) {
        thumb.setX(this.mBar.getNearestTickCoordinate(thumb));
        thumb.setXValue(getPinValue(this.mBar.getNearestTickIndex(thumb)));
        if (this.mArePinsTemporary) {
            ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mExpandedPinRadius, 0.0f});
            animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    RangeBar.this.mThumbRadiusDP = ((Float) animation.getAnimatedValue()).floatValue();
                    thumb.setSize(RangeBar.this.mThumbRadiusDP, RangeBar.this.mPinPadding - (RangeBar.this.mPinPadding * animation.getAnimatedFraction()));
                    RangeBar.this.invalidate();
                }
            });
            animator.start();
        } else {
            invalidate();
        }
        thumb.release();
    }

    private String getPinValue(int tickIndex) {
        if (this.mPinTextListener != null) {
            return this.mPinTextListener.getPinValue(this, tickIndex);
        }
        float tickValue;
        if (tickIndex == this.mTickCount - 1) {
            tickValue = this.mTickEnd;
        } else {
            tickValue = (((float) tickIndex) * this.mTickInterval) + this.mTickStart;
        }
        String xValue = (String) this.mTickMap.get(Float.valueOf(tickValue));
        if (xValue == null) {
            if (((double) tickValue) == Math.ceil((double) tickValue)) {
                xValue = String.valueOf((int) tickValue);
            } else {
                xValue = String.valueOf(tickValue);
            }
        }
        return this.mPinTextFormatter.getText(xValue);
    }

    private void movePin(PinView thumb, float x) {
        if (x >= this.mBar.getLeftX() && x <= this.mBar.getRightX() && thumb != null) {
            thumb.setX(x);
            invalidate();
        }
    }
}
