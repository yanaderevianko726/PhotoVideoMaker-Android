package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ExpandIconView extends View {
    private static final long DEFAULT_ANIMATION_DURATION = 150;
    private static final float DELTA_ALPHA = 90.0f;
    private static final int INTERMEDIATE = 2;
    public static final int LESS = 1;
    public static final int MORE = 0;
    private static final float MORE_STATE_ALPHA = -45.0f;
    private static final float PADDING_PROPORTION = 0.16666667f;
    private static final float THICKNESS_PROPORTION = 0.1388889f;
    private float alpha;
    private final float animationSpeed;
    @Nullable
    private ValueAnimator arrowAnimator;
    private final Point center;
    private float centerTranslation;
    private int color;
    private final int colorLess;
    private final int colorMore;
    @FloatRange(from = 0.0d, to = 1.0d)
    private float fraction;
    private final Point left;
    private int padding;
    @NonNull
    private final Paint paint;
    private final Path path;
    private final Point right;
    private int state;
    private boolean switchColor;
    private final Point tempLeft;
    private final Point tempRight;
    private final boolean useDefaultPadding;

    class C06451 implements AnimatorUpdateListener {
        private final ArgbEvaluator colorEvaluator = new ArgbEvaluator();

        C06451() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ExpandIconView.this.alpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ExpandIconView.this.updateArrowPath();
            if (ExpandIconView.this.switchColor) {
                ExpandIconView.this.updateColor(this.colorEvaluator);
            }
            ExpandIconView.this.postInvalidateOnAnimationCompat();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public ExpandIconView(@NonNull Context context) {
        this(context, null);
    }

    public ExpandIconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"ResourceType"})
    public ExpandIconView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        boolean z = true;
        this.alpha = MORE_STATE_ALPHA;
        this.centerTranslation = 0.0f;
        this.fraction = 1.0f;
        this.switchColor = false;
        this.color = -16777216;
        this.left = new Point();
        this.right = new Point();
        this.center = new Point();
        this.tempLeft = new Point();
        this.tempRight = new Point();
        this.path = new Path();
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandIconView, 0, 0);
        try {
            boolean roundedCorners = array.getBoolean(0, false);
            this.switchColor = array.getBoolean(1, false);
            this.color = array.getColor(2, -1);
            this.colorMore = array.getColor(3, -1);
            this.colorLess = array.getColor(4, -1);
            long animationDuration = (long) array.getInteger(5, 150);
            this.padding = array.getDimensionPixelSize(6, -1);
            if (this.padding != -1) {
                z = false;
            }
            this.useDefaultPadding = z;
            this.paint = new Paint(1);
            this.paint.setColor(this.color);
            this.paint.setStyle(Style.STROKE);
            this.paint.setDither(true);
            if (roundedCorners) {
                this.paint.setStrokeJoin(Join.ROUND);
                this.paint.setStrokeCap(Cap.ROUND);
            }
            this.animationSpeed = DELTA_ALPHA / ((float) animationDuration);
            setState(1, false);
        } finally {
            array.recycle();
        }
    }

    public void switchState() {
        switchState(true);
    }

    public void switchState(boolean animate) {
        int newState;
        switch (this.state) {
            case 0:
                newState = 1;
                break;
            case 1:
                newState = 0;
                break;
            case 2:
                newState = getFinalStateByFraction();
                break;
            default:
                throw new IllegalArgumentException("Unknown state [" + this.state + "]");
        }
        setState(newState, animate);
    }

    public void setState(int state, boolean animate) {
        this.state = state;
        if (state == 0) {
            this.fraction = 0.0f;
        } else if (state == 1) {
            this.fraction = 1.0f;
        } else {
            throw new IllegalArgumentException("Unknown state, must be one of STATE_MORE = 0,  STATE_LESS = 1");
        }
        updateArrow(animate);
    }

    public void setFraction(@FloatRange(from = 0.0d, to = 1.0d) float fraction, boolean animate) {
        if (fraction < 0.0f || fraction > 1.0f) {
            throw new IllegalArgumentException("Fraction value must be from 0 to 1f, fraction=" + fraction);
        } else if (this.fraction != fraction) {
            this.fraction = fraction;
            if (fraction == 0.0f) {
                this.state = 0;
            } else if (fraction == 1.0f) {
                this.state = 1;
            } else {
                this.state = 2;
            }
            updateArrow(animate);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0.0f, this.centerTranslation);
        canvas.drawPath(this.path, this.paint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateArrowMetrics();
        updateArrowPath();
    }

    private void calculateArrowMetrics() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int arrowMaxHeight = height - (this.padding * 2);
        int arrowWidth = width - (this.padding * 2);
        if (arrowMaxHeight < arrowWidth) {
            arrowWidth = arrowMaxHeight;
        }
        if (this.useDefaultPadding) {
            this.padding = (int) (PADDING_PROPORTION * ((float) width));
        }
        this.paint.setStrokeWidth((float) ((int) (((float) arrowWidth) * THICKNESS_PROPORTION)));
        this.center.set(width / 2, height / 2);
        this.left.set(this.center.x - (arrowWidth / 2), this.center.y);
        this.right.set(this.center.x + (arrowWidth / 2), this.center.y);
    }

    private void updateArrow(boolean animate) {
        float toAlpha = MORE_STATE_ALPHA + (this.fraction * DELTA_ALPHA);
        if (animate) {
            animateArrow(toAlpha);
            return;
        }
        cancelAnimation();
        this.alpha = toAlpha;
        if (this.switchColor) {
            updateColor(new ArgbEvaluator());
        }
        updateArrowPath();
        invalidate();
    }

    private void updateArrowPath() {
        this.path.reset();
        if (this.left != null && this.right != null) {
            rotate(this.left, (double) (-this.alpha), this.tempLeft);
            rotate(this.right, (double) this.alpha, this.tempRight);
            this.centerTranslation = (float) ((this.center.y - this.tempLeft.y) / 2);
            this.path.moveTo((float) this.tempLeft.x, (float) this.tempLeft.y);
            this.path.lineTo((float) this.center.x, (float) this.center.y);
            this.path.lineTo((float) this.tempRight.x, (float) this.tempRight.y);
        }
    }

    private void animateArrow(float toAlpha) {
        cancelAnimation();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{this.alpha, toAlpha});
        valueAnimator.addUpdateListener(new C06451());
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(calculateAnimationDuration(toAlpha));
        valueAnimator.start();
        this.arrowAnimator = valueAnimator;
    }

    private void cancelAnimation() {
        if (this.arrowAnimator != null && this.arrowAnimator.isRunning()) {
            this.arrowAnimator.cancel();
        }
    }

    private void updateColor(@NonNull ArgbEvaluator colorEvaluator) {
        this.color = ((Integer) colorEvaluator.evaluate((this.alpha + 45.0f) / DELTA_ALPHA, Integer.valueOf(this.colorMore), Integer.valueOf(this.colorLess))).intValue();
        this.paint.setColor(this.color);
    }

    private long calculateAnimationDuration(float toAlpha) {
        return (long) (Math.abs(toAlpha - this.alpha) / this.animationSpeed);
    }

    private void rotate(@NonNull Point startPosition, double degrees, @NonNull Point target) {
        double angle = Math.toRadians(degrees);
        target.set((int) ((((double) this.center.x) + (((double) (startPosition.x - this.center.x)) * Math.cos(angle))) - (((double) (startPosition.y - this.center.y)) * Math.sin(angle))), (int) ((((double) this.center.y) + (((double) (startPosition.x - this.center.x)) * Math.sin(angle))) + (((double) (startPosition.y - this.center.y)) * Math.cos(angle))));
    }

    private int getFinalStateByFraction() {
        if (this.fraction <= 0.5f) {
            return 0;
        }
        return 1;
    }

    private void postInvalidateOnAnimationCompat() {
        if (VERSION.SDK_INT > 15) {
            postInvalidateOnAnimation();
        } else {
            postInvalidateDelayed(10);
        }
    }
}
