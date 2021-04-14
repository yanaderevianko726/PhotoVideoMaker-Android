package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

public class FreshDownloadView extends View {
    private static final float DEGREE_END_ANGLE = 270.0f;
    private static final float MARK_START_ANGLE = 65.0f;
    private static final float START_ANGLE = -90.0f;
    private static final float TOTAL_ANGLE = 360.0f;
    private final String STR_PERCENT;
    private final String TAG;
    private Rect bounds;
    private int circular_color;
    private float circular_edge;
    private int circular_progress_color;
    private float circular_width;
    private float mArrowStart;
    private DashPathEffect mArrow_center_effect;
    private float mArrow_center_length;
    private DashPathEffect mArrow_left_effect;
    private float mArrow_left_length;
    private DashPathEffect mArrow_right_effect;
    private float mArrow_right_length;
    private boolean mAttached;
    private Path mDst;
    private AnimatorSet mErrorAnimatorSet;
    private float mErrorLeftDegree;
    private float mErrorPathLengthLeft;
    private float mErrorPathLengthRight;
    private float mErrorRightDegree;
    private boolean mIfShowError;
    private boolean mIfShowMarkRun;
    private float mMarkArcAngle;
    private boolean mMarkOkAniRun;
    private float mMarkOkdegree;
    private float mMarkOklength;
    private float mMarkOkstart;
    private AnimatorSet mOkAnimatorSet;
    private boolean mPrepareAniRun;
    private float mProgress;
    private float mProgressTextSize;
    private float mRealLeft;
    private float mRealTop;
    private RectF mTempBounds;
    private boolean mUsing;
    private Path path1;
    private Path path2;
    private Path path3;
    private PathMeasure pathMeasure1;
    private PathMeasure pathMeasure2;
    private PathMeasure pathMeasure3;
    private AnimatorSet prepareAnimator;
    private Paint publicPaint;
    private float radius;
    private float startingArrow;
    private STATUS status;
    private STATUS_MARK status_mark;
    private Rect textBounds;

    class C08461 implements AnimatorUpdateListener {
        C08461() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            FreshDownloadView.this.mArrowStart = FreshDownloadView.this.startingArrow + ((0.52f * FreshDownloadView.this.getRadius()) * ((Float) animation.getAnimatedValue()).floatValue());
            FreshDownloadView.this.updateArrow();
            FreshDownloadView.this.invalidate();
        }
    }

    class C08472 implements AnimatorUpdateListener {
        C08472() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            FreshDownloadView.this.mArrow_left_effect = new DashPathEffect(new float[]{FreshDownloadView.this.mArrow_left_length, FreshDownloadView.this.mArrow_left_length}, FreshDownloadView.this.mArrow_left_length * value);
            FreshDownloadView.this.mArrow_right_effect = new DashPathEffect(new float[]{FreshDownloadView.this.mArrow_right_length, FreshDownloadView.this.mArrow_right_length}, FreshDownloadView.this.mArrow_right_length * value);
            float reduceDis = (1.0f - value) * (FreshDownloadView.this.startingArrow - FreshDownloadView.this.mRealTop);
            FreshDownloadView.this.path1.reset();
            FreshDownloadView.this.path1.moveTo(FreshDownloadView.this.mRealLeft + FreshDownloadView.this.radius, FreshDownloadView.this.mRealTop + reduceDis);
            FreshDownloadView.this.path1.lineTo(FreshDownloadView.this.mRealLeft + FreshDownloadView.this.radius, (FreshDownloadView.this.mRealTop + reduceDis) + FreshDownloadView.this.mArrow_center_length);
            FreshDownloadView.this.mArrow_center_effect = new DashPathEffect(new float[]{FreshDownloadView.this.mArrow_center_length, FreshDownloadView.this.mArrow_center_length}, FreshDownloadView.this.mArrow_center_length * value);
            FreshDownloadView.this.invalidate();
        }
    }

    class C08483 extends AnimatorListenerAdapter {
        C08483() {
        }

        public void onAnimationEnd(Animator animation) {
            FreshDownloadView.this.mArrow_center_effect = null;
            FreshDownloadView.this.mArrow_right_effect = null;
            FreshDownloadView.this.mArrow_left_effect = null;
            FreshDownloadView.this.updateArrow();
        }

        public void onAnimationStart(Animator animation) {
        }
    }

    class C08494 extends AnimatorListenerAdapter {
        C08494() {
        }

        public void onAnimationEnd(Animator animation) {
            FreshDownloadView.this.status = STATUS.DOWNLOADING;
            FreshDownloadView.this.invalidate();
        }
    }

    class C08505 extends AnimatorListenerAdapter {
        C08505() {
        }

        public void onAnimationCancel(Animator animation) {
            FreshDownloadView.this.mPrepareAniRun = false;
        }

        public void onAnimationStart(Animator animation) {
            FreshDownloadView.this.mPrepareAniRun = true;
        }

        public void onAnimationEnd(Animator animation) {
            FreshDownloadView.this.mPrepareAniRun = false;
        }
    }

    class C08516 implements AnimatorUpdateListener {
        C08516() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            FreshDownloadView.this.mMarkArcAngle = ((Float) animation.getAnimatedValue()).floatValue();
            FreshDownloadView.this.invalidate();
        }
    }

    class C08527 extends AnimatorListenerAdapter {
        C08527() {
        }

        public void onAnimationStart(Animator animation) {
            FreshDownloadView.this.status_mark = STATUS_MARK.DRAW_ARC;
        }

        public void onAnimationEnd(Animator animation) {
            FreshDownloadView.this.status_mark = STATUS_MARK.DRAW_MARK;
        }
    }

    class C08538 implements AnimatorUpdateListener {
        C08538() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            FreshDownloadView.this.mMarkOkdegree = ((Float) animation.getAnimatedValue()).floatValue();
            FreshDownloadView.this.invalidate();
        }
    }

    class C08549 implements AnimatorUpdateListener {
        C08549() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            FreshDownloadView.this.mMarkOkstart = ((Float) animation.getAnimatedValue()).floatValue();
            FreshDownloadView.this.invalidate();
        }
    }

    static class FreshDownloadStatus extends AbsSavedState {
        public static final Creator<FreshDownloadStatus> CREATOR = new C08551();
        public int circular_color;
        public int circular_progress_color;
        public float circular_width;
        public float mProgressTextSize;
        public float progress;
        public float radius;
        public STATUS status;

        static class C08551 implements Creator<FreshDownloadStatus> {
            C08551() {
            }

            public FreshDownloadStatus createFromParcel(Parcel source) {
                return new FreshDownloadStatus(source);
            }

            public FreshDownloadStatus[] newArray(int size) {
                return new FreshDownloadStatus[size];
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status == null ? -1 : this.status.ordinal());
            dest.writeFloat(this.progress);
            dest.writeFloat(this.radius);
            dest.writeInt(this.circular_color);
            dest.writeInt(this.circular_progress_color);
            dest.writeFloat(this.circular_width);
            dest.writeFloat(this.mProgressTextSize);
        }

        public FreshDownloadStatus(Parcelable superState) {
            super(superState);
        }

        protected FreshDownloadStatus(Parcel in) {
            super(in);
            int tmpStatus = in.readInt();
            this.status = tmpStatus == -1 ? null : STATUS.values()[tmpStatus];
            this.progress = in.readFloat();
            this.radius = in.readFloat();
            this.circular_color = in.readInt();
            this.circular_progress_color = in.readInt();
            this.circular_width = in.readFloat();
            this.mProgressTextSize = in.readFloat();
        }
    }

    public enum STATUS {
        PREPARE,
        DOWNLOADING,
        DOWNLOADED,
        ERROR
    }

    private enum STATUS_MARK {
        DRAW_ARC,
        DRAW_MARK
    }

    public FreshDownloadView(Context context) {
        this(context, null);
    }

    public FreshDownloadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreshDownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.TAG = FreshDownloadView.class.getSimpleName();
        this.mPrepareAniRun = false;
        this.STR_PERCENT = "%";
        this.mIfShowMarkRun = false;
        this.mDst = new Path();
        this.status = STATUS.PREPARE;
        this.circular_edge = getResources().getDimension(R.dimen.edge);
        this.bounds = new Rect();
        this.mTempBounds = new RectF();
        this.publicPaint = new Paint();
        this.path1 = new Path();
        this.path2 = new Path();
        this.path3 = new Path();
        this.pathMeasure1 = new PathMeasure();
        this.pathMeasure2 = new PathMeasure();
        this.pathMeasure3 = new PathMeasure();
        this.textBounds = new Rect();
        parseAttrs(context.obtainStyledAttributes(attrs, R.styleable.FreshDownloadView));
        initPaint();
    }

    private void parseAttrs(TypedArray array) {
        if (array != null) {
            try {
                setRadius(array.getDimension(2, getResources().getDimension(R.dimen.default_radius)));
                setCircularColor(array.getColor(0, getResources().getColor(R.color.default_circular_color)));
                setProgressColor(array.getColor(1, getResources().getColor(R.color.default_circular_progress_color)));
                setCircularWidth(array.getDimension(3, getResources().getDimension(R.dimen.default_circular_width)));
                setProgressTextSize(array.getDimension(4, getResources().getDimension(R.dimen.default_text_size)));
            } finally {
                array.recycle();
            }
        }
    }

    private void initPaint() {
        this.publicPaint.setStrokeCap(Cap.ROUND);
        this.publicPaint.setStrokeWidth(getCircularWidth());
        this.publicPaint.setStyle(Style.STROKE);
        this.publicPaint.setAntiAlias(true);
    }

    public void startDownload() {
        this.mUsing = true;
        if (this.prepareAnimator == null || !this.mPrepareAniRun) {
            if (this.prepareAnimator == null) {
                this.prepareAnimator = getPrepareAnimator();
            }
            this.prepareAnimator.start();
        }
    }

    private AnimatorSet getPrepareAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator downAnimaor = ValueAnimator.ofFloat(new float[]{0.0f, 0.3f, 0.0f}).setDuration(500);
        downAnimaor.setInterpolator(new DecelerateInterpolator());
        downAnimaor.addUpdateListener(new C08461());
        ValueAnimator upAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(800);
        upAnimator.setInterpolator(new DecelerateInterpolator());
        upAnimator.addUpdateListener(new C08472());
        upAnimator.addListener(new C08483());
        animatorSet.addListener(new C08494());
        animatorSet.play(downAnimaor).before(upAnimator);
        animatorSet.addListener(new C08505());
        return animatorSet;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dy = 0 + ((getPaddingTop() + getPaddingBottom()) + getCurrentHeight());
        setMeasuredDimension(Math.max(getSuggestedMinimumWidth(), resolveSizeAndState(0 + ((getPaddingLeft() + getPaddingRight()) + getCurrentWidth()), widthMeasureSpec, 0)), Math.max(getSuggestedMinimumHeight(), resolveSizeAndState(dy, heightMeasureSpec, 0)));
    }

    private int getCurrentHeight() {
        return (int) ((getRadius() * 2.0f) + (this.circular_edge * 2.0f));
    }

    private int getCurrentWidth() {
        return (int) ((getRadius() * 2.0f) + (this.circular_edge * 2.0f));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int top = getPaddingTop() + 0;
        int bottom = getHeight() - getPaddingBottom();
        int left = getPaddingLeft() + 0;
        int right = getWidth() - getPaddingRight();
        updateBounds(top, bottom, left, right);
        initArrowPath(top, bottom, left, right, getRadius());
    }

    private float buildAngle_percent_of_pain_width() {
        return (float) (((double) getCircularWidth()) / (((double) (getRadius() * 2.0f)) * 3.141592653589793d));
    }

    private void updateBounds(int top, int bottom, int left, int right) {
        this.bounds.set(left, top, right, bottom);
    }

    private void initArrowPath(int top, int bottom, int left, int right, float radius) {
        float realTop = ((float) top) + this.circular_edge;
        this.mRealLeft = ((float) left) + this.circular_edge;
        this.mRealTop = realTop;
        this.startingArrow = (0.48f * radius) + realTop;
        this.mArrowStart = this.startingArrow;
        this.status = STATUS.PREPARE;
        updateArrow();
    }

    private void updateArrow() {
        this.path1.reset();
        this.path2.reset();
        this.path3.reset();
        this.path1.moveTo(this.mRealLeft + this.radius, this.mArrowStart);
        this.path1.lineTo(this.mRealLeft + this.radius, this.mArrowStart + this.radius);
        this.path2.moveTo(this.mRealLeft + this.radius, this.mArrowStart + this.radius);
        this.path2.lineTo((float) (((double) (this.mRealLeft + this.radius)) - ((Math.tan(Math.toRadians(40.0d)) * ((double) this.radius)) * 0.46000000834465027d)), (this.mArrowStart + this.radius) - (this.radius * 0.46f));
        this.path3.moveTo(this.mRealLeft + this.radius, this.mArrowStart + this.radius);
        this.path3.lineTo((float) (((double) (this.mRealLeft + this.radius)) + ((Math.tan(Math.toRadians(40.0d)) * ((double) this.radius)) * 0.46000000834465027d)), (this.mArrowStart + this.radius) - (this.radius * 0.46f));
        this.pathMeasure1.setPath(this.path1, false);
        this.pathMeasure2.setPath(this.path2, false);
        this.pathMeasure3.setPath(this.path3, false);
        this.mArrow_center_length = this.pathMeasure1.getLength();
        this.mArrow_left_length = this.pathMeasure2.getLength();
        this.mArrow_right_length = this.pathMeasure3.getLength();
    }

    protected void onDraw(Canvas canvas) {
        this.publicPaint.setPathEffect(null);
        this.publicPaint.setStyle(Style.STROKE);
        this.publicPaint.setColor(getCircularColor());
        RectF arcBounds = this.mTempBounds;
        arcBounds.set(this.bounds);
        arcBounds.inset(this.circular_edge, this.circular_edge);
        canvas.drawArc(arcBounds, 0.0f, TOTAL_ANGLE, false, this.publicPaint);
        switch (this.status) {
            case PREPARE:
                drawPrepare(canvas);
                return;
            case DOWNLOADING:
                drawDownLoading(canvas, arcBounds);
                return;
            case DOWNLOADED:
                drawDownLoaded(canvas, this.status_mark, arcBounds, this.mMarkArcAngle);
                return;
            case ERROR:
                drawDownError(canvas);
                return;
            default:
                return;
        }
    }

    private void drawPrepare(Canvas canvas) {
        this.publicPaint.setColor(getProgressColor());
        if (this.mArrow_center_effect != null) {
            this.publicPaint.setPathEffect(this.mArrow_center_effect);
        }
        canvas.drawPath(this.path1, this.publicPaint);
        if (this.mArrow_left_effect != null) {
            this.publicPaint.setPathEffect(this.mArrow_left_effect);
        }
        canvas.drawPath(this.path2, this.publicPaint);
        if (this.mArrow_right_effect != null) {
            this.publicPaint.setPathEffect(this.mArrow_right_effect);
        }
        canvas.drawPath(this.path3, this.publicPaint);
    }

    private void drawDownLoading(Canvas canvas, RectF arcBounds) {
        float progress_degree = this.mProgress;
        this.publicPaint.setColor(getProgressColor());
        if (progress_degree <= 0.0f) {
            canvas.drawPoint(this.mRealLeft + this.radius, this.mRealTop, this.publicPaint);
        } else {
            canvas.drawArc(arcBounds, START_ANGLE, progress_degree * TOTAL_ANGLE, false, this.publicPaint);
        }
        drawText(canvas, progress_degree);
    }

    private void drawText(Canvas canvas, float progress_degree) {
        String sDegree = String.valueOf(Math.round(100.0f * progress_degree));
        Rect rect = this.bounds;
        this.publicPaint.setStyle(Style.FILL);
        this.publicPaint.setTextSize(getProgressTextSize());
        this.publicPaint.setTextAlign(Align.CENTER);
        FontMetricsInt fontMetrics = this.publicPaint.getFontMetricsInt();
        int baseline = (((rect.bottom + rect.top) - fontMetrics.bottom) - fontMetrics.top) / 2;
        canvas.drawText(sDegree, (float) rect.centerX(), (float) baseline, this.publicPaint);
        this.publicPaint.getTextBounds(sDegree, 0, sDegree.length(), this.textBounds);
        this.publicPaint.setTextSize(getProgressTextSize() / 3.0f);
        this.publicPaint.setTextAlign(Align.LEFT);
        canvas.drawText("%", ((float) (rect.centerX() + (this.textBounds.width() / 2))) + (0.1f * this.radius), (float) baseline, this.publicPaint);
    }

    private void drawDownLoaded(Canvas canvas, STATUS_MARK status, RectF bounds, float angle) {
        this.publicPaint.setColor(getProgressColor());
        switch (status) {
            case DRAW_ARC:
                canvas.drawArc(bounds, DEGREE_END_ANGLE - angle, 0.36f, false, this.publicPaint);
                return;
            case DRAW_MARK:
                Path dst = this.mDst;
                dst.reset();
                dst.lineTo(0.0f, 0.0f);
                this.pathMeasure1.getSegment(this.mMarkOkstart * this.mMarkOklength, (this.mMarkOkstart + this.mMarkOkdegree) * this.mMarkOklength, dst, true);
                canvas.drawPath(dst, this.publicPaint);
                return;
            default:
                return;
        }
    }

    private void drawDownError(Canvas canvas) {
        if (this.mIfShowMarkRun) {
            drawText(canvas, this.mProgress);
        }
        this.publicPaint.setColor(-1);
        Path dst = this.mDst;
        dst.reset();
        dst.lineTo(0.0f, 0.0f);
        this.pathMeasure1.getSegment(this.mErrorPathLengthLeft * 0.2f, this.mErrorRightDegree * this.mErrorPathLengthLeft, dst, true);
        canvas.drawPath(dst, this.publicPaint);
        dst.reset();
        dst.lineTo(0.0f, 0.0f);
        this.pathMeasure2.getSegment(this.mErrorPathLengthRight * 0.2f, this.mErrorLeftDegree * this.mErrorPathLengthRight, dst, true);
        canvas.drawPath(dst, this.publicPaint);
    }

    public void upDateProgress(float progress) {
        setProgressInternal(progress);
    }

    public void upDateProgress(int progress) {
        upDateProgress(((float) progress) / 100.0f);
    }

    public void reset() {
        resetStatus();
    }

    private void resetStatus() {
        if (this.status != STATUS.DOWNLOADING && !this.mPrepareAniRun && !this.mIfShowError && !this.mMarkOkAniRun) {
            this.status = STATUS.PREPARE;
            this.mArrowStart = this.startingArrow;
            updateArrow();
            postInvalidate();
            this.mProgress = 0.0f;
            this.mMarkOkdegree = 0.0f;
            this.mMarkArcAngle = 0.0f;
            this.mMarkOkstart = 0.0f;
            this.mUsing = false;
            this.mErrorLeftDegree = 0.0f;
            this.mErrorRightDegree = 0.0f;
        }
    }

    public boolean using() {
        return this.mUsing;
    }

    synchronized void setProgressInternal(float progressInternal) {
        this.mProgress = progressInternal;
        if (this.status == STATUS.PREPARE) {
            startDownload();
        }
        invalidate();
        if (progressInternal >= 1.0f) {
            showDownloadOk();
        }
    }

    public void showDownloadOk() {
        this.status = STATUS.DOWNLOADED;
        makeOkPath();
        if (this.mOkAnimatorSet == null || !this.mMarkOkAniRun) {
            if (this.mOkAnimatorSet == null) {
                this.mOkAnimatorSet = getDownloadOkAnimator();
            }
            this.mOkAnimatorSet.start();
        }
    }

    private void makeOkPath() {
        this.path1.reset();
        int w2 = getMeasuredWidth() / 2;
        int h2 = getMeasuredHeight() / 2;
        double a = Math.cos(Math.toRadians(25.0d)) * ((double) getRadius());
        double c = Math.sin(Math.toRadians(25.0d)) * ((double) getRadius());
        double l = (Math.cos(Math.toRadians(53.0d)) * 2.0d) * a;
        double b = Math.sin(Math.toRadians(53.0d)) * l;
        double m = Math.cos(Math.toRadians(53.0d)) * l;
        this.path1.moveTo((float) (((double) w2) - a), (float) (((double) h2) - c));
        this.path1.lineTo((float) ((((double) w2) - a) + m), (float) ((((double) h2) - c) + (Math.sin(Math.toRadians(53.0d)) * l)));
        this.path1.lineTo((float) (((double) w2) + a), (float) (((double) h2) - c));
        this.pathMeasure1.setPath(this.path1, false);
        this.mMarkOklength = this.pathMeasure1.getLength();
    }

    private AnimatorSet getDownloadOkAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator roundAnimator = ValueAnimator.ofFloat(new float[]{0.0f, MARK_START_ANGLE}).setDuration(100);
        roundAnimator.addUpdateListener(new C08516());
        roundAnimator.addListener(new C08527());
        ValueAnimator firstAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 0.7f}).setDuration(200);
        firstAnimator.setInterpolator(new AccelerateInterpolator());
        firstAnimator.addUpdateListener(new C08538());
        ValueAnimator secondAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 0.35f, 0.2f}).setDuration(500);
        secondAnimator.addUpdateListener(new C08549());
        secondAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animation) {
                FreshDownloadView.this.mIfShowMarkRun = false;
            }

            public void onAnimationEnd(Animator animation) {
                FreshDownloadView.this.mIfShowMarkRun = false;
            }

            public void onAnimationStart(Animator animation) {
                FreshDownloadView.this.mIfShowMarkRun = true;
            }
        });
        animatorSet.play(firstAnimator).after(roundAnimator);
        animatorSet.play(secondAnimator).after(200);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                FreshDownloadView.this.mMarkOkAniRun = true;
            }

            public void onAnimationEnd(Animator animation) {
                FreshDownloadView.this.mMarkOkAniRun = false;
            }

            public void onAnimationCancel(Animator animation) {
                FreshDownloadView.this.mMarkOkAniRun = false;
            }
        });
        return animatorSet;
    }

    public void showDownloadError() {
        this.status = STATUS.ERROR;
        makeErrorPath();
        invalidate();
        if (this.mErrorAnimatorSet == null || !this.mIfShowError) {
            if (this.mErrorAnimatorSet == null) {
                this.mErrorAnimatorSet = getDownLoadErrorAnimator();
            }
            this.mErrorAnimatorSet.start();
        }
    }

    private void makeErrorPath() {
        Rect bounds = this.bounds;
        int w2 = bounds.centerX();
        int h2 = bounds.centerY();
        this.path1.reset();
        this.path1.moveTo((float) (((double) w2) - (Math.cos(Math.toRadians(45.0d)) * ((double) getRadius()))), (float) (((double) h2) - (Math.sin(Math.toRadians(45.0d)) * ((double) getRadius()))));
        this.path1.lineTo((float) (((double) w2) + (Math.cos(Math.toRadians(45.0d)) * ((double) getRadius()))), (float) (((double) h2) + (Math.sin(Math.toRadians(45.0d)) * ((double) getRadius()))));
        this.pathMeasure1.setPath(this.path1, false);
        this.mErrorPathLengthLeft = this.pathMeasure1.getLength();
        this.path1.reset();
        this.path1.moveTo((float) (((double) w2) + (Math.cos(Math.toRadians(45.0d)) * ((double) getRadius()))), (float) (((double) h2) - (Math.sin(Math.toRadians(45.0d)) * ((double) getRadius()))));
        this.path1.lineTo((float) (((double) w2) - (Math.cos(Math.toRadians(45.0d)) * ((double) getRadius()))), (float) (((double) h2) + (Math.sin(Math.toRadians(45.0d)) * ((double) getRadius()))));
        this.pathMeasure2.setPath(this.path1, false);
        this.mErrorPathLengthRight = this.pathMeasure2.getLength();
    }

    private AnimatorSet getDownLoadErrorAnimator() {
        AnimatorSet errorSet = new AnimatorSet();
        ValueAnimator animator1 = ValueAnimator.ofFloat(new float[]{0.2f, 0.8f}).setDuration(300);
        animator1.setInterpolator(new OvershootInterpolator());
        animator1.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                FreshDownloadView.this.mErrorLeftDegree = ((Float) animation.getAnimatedValue()).floatValue();
                FreshDownloadView.this.invalidate();
            }
        });
        ValueAnimator animator2 = ValueAnimator.ofFloat(new float[]{0.2f, 0.8f}).setDuration(300);
        animator2.setInterpolator(new OvershootInterpolator());
        animator2.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                FreshDownloadView.this.mErrorRightDegree = ((Float) animation.getAnimatedValue()).floatValue();
                FreshDownloadView.this.invalidate();
            }
        });
        errorSet.play(animator1);
        errorSet.play(animator2).after(100);
        errorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animation) {
                FreshDownloadView.this.mIfShowError = false;
            }

            public void onAnimationEnd(Animator animation) {
                FreshDownloadView.this.mIfShowError = false;
            }

            public void onAnimationStart(Animator animation) {
                FreshDownloadView.this.mIfShowError = true;
            }
        });
        return errorSet;
    }

    public float getProgressTextSize() {
        return this.mProgressTextSize;
    }

    public void setProgressTextSize(float mProgressTextSize) {
        this.mProgressTextSize = mProgressTextSize;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getCircularColor() {
        return this.circular_color;
    }

    public void setCircularColor(int circular_color) {
        this.circular_color = circular_color;
    }

    public int getProgressColor() {
        return this.circular_progress_color;
    }

    public void setProgressColor(int circular_progress_color) {
        this.circular_progress_color = circular_progress_color;
    }

    public float getCircularWidth() {
        return this.circular_width;
    }

    public void setCircularWidth(float circular_width) {
        this.circular_width = circular_width;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttached = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAttached = false;
    }

    protected Parcelable onSaveInstanceState() {
        FreshDownloadStatus fds = new FreshDownloadStatus(super.onSaveInstanceState());
        fds.circular_color = this.circular_color;
        fds.circular_progress_color = this.circular_progress_color;
        fds.circular_width = this.circular_width;
        fds.progress = this.mProgress;
        fds.radius = this.radius;
        fds.status = this.status;
        fds.mProgressTextSize = this.mProgressTextSize;
        return fds;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof FreshDownloadStatus) {
            FreshDownloadStatus fds = (FreshDownloadStatus) state;
            this.circular_color = fds.circular_color;
            this.circular_progress_color = fds.circular_progress_color;
            this.circular_width = fds.circular_width;
            this.mProgress = fds.progress;
            this.radius = fds.radius;
            this.status = fds.status;
            this.mProgressTextSize = fds.mProgressTextSize;
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
