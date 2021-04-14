package com.producevideos.crearvideosconfotosymusicaytextoeditor.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

@SuppressLint({"AppCompatCustomView"})
public class CircularFillableLoaders extends ImageView {
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    public static final int DEFAULT_BORDER_WIDTH = 10;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    public static final int DEFAULT_WAVE_COLOR = -16777216;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    private float amplitudeRatio;
    private AnimatorSet animatorSetWave;
    private Paint borderPaint;
    private int canvasSize;
    private float defaultWaterLevel;
    private Drawable drawable;
    private Bitmap image;
    private Paint paint;
    private float waterLevelRatio;
    private int waveColor;
    private Paint wavePaint;
    private BitmapShader waveShader;
    private Matrix waveShaderMatrix;
    private float waveShiftRatio;

    public CircularFillableLoaders(Context context) {
        this(context, null);
    }

    public CircularFillableLoaders(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularFillableLoaders(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.waterLevelRatio = DEFAULT_WAVE_LENGTH_RATIO;
        this.waveShiftRatio = 0.0f;
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.waveShaderMatrix = new Matrix();
        this.wavePaint = new Paint();
        this.wavePaint.setAntiAlias(true);
        this.borderPaint = new Paint();
        this.borderPaint.setAntiAlias(true);
        this.borderPaint.setStyle(Style.STROKE);
        initAnimation();
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularFillableLoaders, defStyleAttr, 0);
        this.waveColor = attributes.getColor(4, -16777216);
        float amplitudeRatioAttr = attributes.getFloat(3, DEFAULT_AMPLITUDE_RATIO);
        if (amplitudeRatioAttr > DEFAULT_AMPLITUDE_RATIO) {
            amplitudeRatioAttr = DEFAULT_AMPLITUDE_RATIO;
        }
        this.amplitudeRatio = amplitudeRatioAttr;
        setProgress(attributes.getInteger(2, 0));
        if (attributes.getBoolean(0, true)) {
            this.borderPaint.setStrokeWidth(attributes.getDimension(1, 10.0f * getContext().getResources().getDisplayMetrics().density));
            return;
        }
        this.borderPaint.setStrokeWidth(0.0f);
    }

    public void onDraw(Canvas canvas) {
        loadBitmap();
        if (this.image != null) {
            if (!isInEditMode()) {
                this.canvasSize = canvas.getWidth();
                if (canvas.getHeight() < this.canvasSize) {
                    this.canvasSize = canvas.getHeight();
                }
            }
            int circleCenter = this.canvasSize / 2;
            canvas.drawCircle((float) circleCenter, (float) circleCenter, ((float) circleCenter) - this.borderPaint.getStrokeWidth(), this.paint);
            if (this.waveShader != null) {
                if (this.wavePaint.getShader() == null) {
                    this.wavePaint.setShader(this.waveShader);
                }
                this.waveShaderMatrix.setScale(DEFAULT_WAVE_LENGTH_RATIO, this.amplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0.0f, this.defaultWaterLevel);
                this.waveShaderMatrix.postTranslate(this.waveShiftRatio * ((float) getWidth()), (DEFAULT_WATER_LEVEL_RATIO - this.waterLevelRatio) * ((float) getHeight()));
                this.waveShader.setLocalMatrix(this.waveShaderMatrix);
                this.borderPaint.setColor(this.waveColor);
                float borderWidth = this.borderPaint.getStrokeWidth();
                if (borderWidth > 0.0f) {
                    canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, ((((float) getWidth()) - borderWidth) / 2.0f) - DEFAULT_WAVE_LENGTH_RATIO, this.borderPaint);
                }
                canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, (((float) getWidth()) / 2.0f) - borderWidth, this.wavePaint);
                return;
            }
            this.wavePaint.setShader(null);
        }
    }

    private void loadBitmap() {
        if (this.drawable != getDrawable()) {
            this.drawable = getDrawable();
            this.image = drawableToBitmap(this.drawable);
            updateShader();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.canvasSize = w;
        if (h < this.canvasSize) {
            this.canvasSize = h;
        }
        if (this.image != null) {
            updateShader();
        }
    }

    private void updateShader() {
        if (this.image != null) {
            this.image = cropBitmap(this.image);
            BitmapShader shader = new BitmapShader(this.image, TileMode.CLAMP, TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.setScale(((float) this.canvasSize) / ((float) this.image.getWidth()), ((float) this.canvasSize) / ((float) this.image.getHeight()));
            shader.setLocalMatrix(matrix);
            this.paint.setShader(shader);
            updateWaveShader();
        }
    }

    private void updateWaveShader() {
        int beginX;
        double defaultAngularFrequency = 6.283185307179586d / ((double) getWidth());
        float defaultAmplitude = ((float) getHeight()) * DEFAULT_AMPLITUDE_RATIO;
        this.defaultWaterLevel = ((float) getHeight()) * DEFAULT_WATER_LEVEL_RATIO;
        float defaultWaveLength = (float) getWidth();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2.0f);
        wavePaint.setAntiAlias(true);
        int endX = getWidth() + 1;
        int endY = getHeight() + 1;
        float[] waveY = new float[endX];
        wavePaint.setColor(adjustAlpha(this.waveColor, 0.3f));
        for (beginX = 0; beginX < endX; beginX++) {
            float beginY = (float) (((double) this.defaultWaterLevel) + (((double) defaultAmplitude) * Math.sin(((double) beginX) * defaultAngularFrequency)));
            canvas.drawLine((float) beginX, beginY, (float) beginX, (float) endY, wavePaint);
            waveY[beginX] = beginY;
        }
        wavePaint.setColor(this.waveColor);
        int wave2Shift = (int) (defaultWaveLength / 4.0f);
        for (beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine((float) beginX, waveY[(beginX + wave2Shift) % endX], (float) beginX, (float) endY, wavePaint);
        }
        this.waveShader = new BitmapShader(bitmap, TileMode.REPEAT, TileMode.CLAMP);
        this.wavePaint.setShader(this.waveShader);
    }

    private Bitmap cropBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            return Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2) - (bitmap.getHeight() / 2), 0, bitmap.getHeight(), bitmap.getHeight());
        }
        return Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() / 2) - (bitmap.getWidth() / 2), bitmap.getWidth(), bitmap.getWidth());
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return null;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            Log.e(getClass().toString(), "Encountered OutOfMemoryError while generating bitmap!");
            return null;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int imageSize;
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        if (width < height) {
            imageSize = width;
        } else {
            imageSize = height;
        }
        setMeasuredDimension(imageSize, imageSize);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            return specSize;
        }
        if (specMode == Integer.MIN_VALUE) {
            return specSize;
        }
        return this.canvasSize;
    }

    private int measureHeight(int measureSpecHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);
        if (specMode == 1073741824) {
            result = specSize;
        } else if (specMode == Integer.MIN_VALUE) {
            result = specSize;
        } else {
            result = this.canvasSize;
        }
        return result + 2;
    }

    public void setColor(int color) {
        this.waveColor = color;
        updateWaveShader();
        invalidate();
    }

    public void setBorderWidth(float width) {
        this.borderPaint.setStrokeWidth(width);
        invalidate();
    }

    public void setAmplitudeRatio(float amplitudeRatio) {
        if (this.amplitudeRatio != amplitudeRatio) {
            this.amplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public void setProgress(int progress) {
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", new float[]{this.waterLevelRatio, DEFAULT_WAVE_LENGTH_RATIO - (((float) progress) / 100.0f)});
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        animatorSetProgress.start();
    }

    private void startAnimation() {
        if (this.animatorSetWave != null) {
            this.animatorSetWave.start();
        }
    }

    private void initAnimation() {
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", new float[]{0.0f, DEFAULT_WAVE_LENGTH_RATIO});
        waveShiftAnim.setRepeatCount(-1);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        this.animatorSetWave = new AnimatorSet();
        this.animatorSetWave.play(waveShiftAnim);
    }

    private void setWaveShiftRatio(float waveShiftRatio) {
        if (this.waveShiftRatio != waveShiftRatio) {
            this.waveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    private void setWaterLevelRatio(float waterLevelRatio) {
        if (this.waterLevelRatio != waterLevelRatio) {
            this.waterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    private void cancel() {
        if (this.animatorSetWave != null) {
            this.animatorSetWave.end();
        }
    }

    protected void onAttachedToWindow() {
        startAnimation();
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        cancel();
        super.onDetachedFromWindow();
    }

    private int adjustAlpha(int color, float factor) {
        return Color.argb(Math.round(((float) Color.alpha(color)) * factor), Color.red(color), Color.green(color), Color.blue(color));
    }
}
