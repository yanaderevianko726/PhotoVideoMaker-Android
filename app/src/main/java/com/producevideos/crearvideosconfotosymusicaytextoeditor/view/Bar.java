package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

public class Bar {
    private final Paint mBarPaint = new Paint();
    private final float mLeftX;
    private int mNumSegments;
    private final float mRightX;
    private float mTickDistance;
    private final float mTickHeight;
    private final Paint mTickPaint;
    private final float mY;

    public Bar(Context ctx, float x, float y, float length, int tickCount, float tickHeightDP, int tickColor, float barWeight, int barColor) {
        this.mLeftX = x;
        this.mRightX = x + length;
        this.mY = y;
        this.mNumSegments = tickCount - 1;
        this.mTickDistance = length / ((float) this.mNumSegments);
        this.mTickHeight = TypedValue.applyDimension(1, tickHeightDP, ctx.getResources().getDisplayMetrics());
        this.mBarPaint.setColor(barColor);
        this.mBarPaint.setStrokeWidth(barWeight);
        this.mBarPaint.setAntiAlias(true);
        this.mTickPaint = new Paint();
        this.mTickPaint.setColor(tickColor);
        this.mTickPaint.setStrokeWidth(barWeight);
        this.mTickPaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(this.mLeftX, this.mY, this.mRightX, this.mY, this.mBarPaint);
    }

    public float getLeftX() {
        return this.mLeftX;
    }

    public float getRightX() {
        return this.mRightX;
    }

    public float getNearestTickCoordinate(PinView thumb) {
        return this.mLeftX + (((float) getNearestTickIndex(thumb)) * this.mTickDistance);
    }

    public int getNearestTickIndex(PinView thumb) {
        return (int) (((thumb.getX() - this.mLeftX) + (this.mTickDistance / 2.0f)) / this.mTickDistance);
    }

    public void setTickCount(int tickCount) {
        float barLength = this.mRightX - this.mLeftX;
        this.mNumSegments = tickCount - 1;
        this.mTickDistance = barLength / ((float) this.mNumSegments);
    }

    public void drawTicks(Canvas canvas) {
        for (int i = 0; i < this.mNumSegments; i++) {
            canvas.drawCircle((((float) i) * this.mTickDistance) + this.mLeftX, this.mY, this.mTickHeight, this.mTickPaint);
        }
        canvas.drawCircle(this.mRightX, this.mY, this.mTickHeight, this.mTickPaint);
    }
}
