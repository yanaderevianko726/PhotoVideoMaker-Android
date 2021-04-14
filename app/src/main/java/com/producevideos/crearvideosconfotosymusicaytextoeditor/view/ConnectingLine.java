package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.TypedValue;

public class ConnectingLine {
    private final Paint mPaint = new Paint();
    private final float mY;

    public ConnectingLine(Context ctx, float y, float connectingLineWeight, int connectingLineColor) {
        float connectingLineWeight1 = TypedValue.applyDimension(1, connectingLineWeight, ctx.getResources().getDisplayMetrics());
        this.mPaint.setColor(connectingLineColor);
        this.mPaint.setStrokeWidth(connectingLineWeight1);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setAntiAlias(true);
        this.mY = y;
    }

    public void draw(Canvas canvas, PinView leftThumb, PinView rightThumb) {
        canvas.drawLine(leftThumb.getX(), this.mY, rightThumb.getX(), this.mY, this.mPaint);
    }

    public void draw(Canvas canvas, float leftMargin, PinView rightThumb) {
        canvas.drawLine(leftMargin, this.mY, rightThumb.getX(), this.mY, this.mPaint);
    }
}
