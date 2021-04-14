package com.producevideos.crearvideosconfotosymusicaytextoeditor.mask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Roll3DView extends View {
    private static final String TAG = "TDAct";
    private static int[] f6x7caf88d4 = null;
    int averageHeight = 0;
    int averageWidth = 0;
    private float axisX = 0.0f;
    private float axisY = 0.0f;
    private List<Bitmap> bitmapList;
    private Bitmap[][] bitmaps;
    private Camera camera;
    private Context context;
    private int currIndex = 0;
    private int direction = 1;
    private Matrix matrix;
    private int nextIndex = 0;
    private Paint paint;
    private int partNumber = 1;
    private int preIndex = 0;
    private int rollDuration = 1000;
    private RollMode rollMode = RollMode.SepartConbine;
    private boolean rolling;
    private float rotateDegree = 0.0f;
    private AnimatorListenerAdapter toNextAnimListener = new C06381();
    private AnimatorListenerAdapter toPreAnimListener = new C06403();
    private AnimatorUpdateListener updateListener = new C06392();
    private ValueAnimator valueAnimator;
    private int viewHeight;
    private int viewWidth;
    private int temp;

    class C06381 extends AnimatorListenerAdapter {
        C06381() {
        }

        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            Roll3DView roll3DView = Roll3DView.this;
            roll3DView.currIndex = roll3DView.currIndex + 1;
            if (Roll3DView.this.currIndex > Roll3DView.this.bitmapList.size() - 1) {
                Roll3DView.this.currIndex = 0;
            }
            Roll3DView.this.initIndex();
            Roll3DView.this.setRotateDegree(0.0f);
            Roll3DView.this.rolling = false;
        }
    }

    class C06392 implements AnimatorUpdateListener {
        C06392() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Roll3DView.this.setRotateDegree(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    class C06403 extends AnimatorListenerAdapter {
        C06403() {
        }

        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            Roll3DView.this.rollIndex(false);
            Roll3DView roll3DView = Roll3DView.this;
            roll3DView.currIndex = roll3DView.currIndex - 1;
            if (Roll3DView.this.currIndex < 0) {
                Roll3DView.this.currIndex = Roll3DView.this.bitmapList.size() - 1;
            }
            Roll3DView.this.rolling = false;
            Roll3DView.this.initIndex();
            Roll3DView.this.invalidate();
        }
    }

    public enum RollMode {
        Roll2D,
        Whole3D,
        SepartConbine,
        RollInTurn,
        Jalousie
    }

    static int[] m15x7caf88d4() {
        int[] iArr = f6x7caf88d4;
        if (iArr == null) {
            iArr = new int[RollMode.values().length];
            try {
                iArr[RollMode.Jalousie.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[RollMode.Roll2D.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[RollMode.RollInTurn.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[RollMode.SepartConbine.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[RollMode.Whole3D.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            f6x7caf88d4 = iArr;
        }
        return iArr;
    }

    public Roll3DView(Context context) {
        super(context);
        init(context);
    }

    public Roll3DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Roll3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.bitmapList = new ArrayList();
        this.paint = new Paint(1);
        this.camera = new Camera();
        this.matrix = new Matrix();
        this.context = context;
    }

    private void initBitmaps() {
        if ((this.viewHeight > 0 || this.viewWidth > 0) && this.bitmapList != null && this.bitmapList.size() > 0) {
            this.bitmaps = (Bitmap[][]) Array.newInstance(Bitmap.class, new int[]{this.bitmapList.size(), this.partNumber});
            initIndex();
            this.averageWidth = this.viewWidth / this.partNumber;
            this.averageHeight = this.viewHeight / this.partNumber;
            for (int i = 0; i < this.bitmapList.size(); i++) {
                for (int j = 0; j < this.partNumber; j++) {
                    Bitmap partBitmap;
                    if (this.rollMode != RollMode.Jalousie) {
                        if (this.direction == 1) {
                            partBitmap = getPartBitmap((Bitmap) this.bitmapList.get(i), this.averageWidth * j, 0, new Rect(this.averageWidth * j, 0, (j + 1) * this.averageWidth, this.viewHeight));
                        } else {
                            partBitmap = getPartBitmap((Bitmap) this.bitmapList.get(i), 0, this.averageHeight * j, new Rect(0, this.averageHeight * j, this.viewWidth, (j + 1) * this.averageHeight));
                        }
                    } else if (this.direction == 1) {
                        partBitmap = getPartBitmap((Bitmap) this.bitmapList.get(i), 0, this.averageHeight * j, new Rect(0, this.averageHeight * j, this.viewWidth, (j + 1) * this.averageHeight));
                    } else {
                        partBitmap = getPartBitmap((Bitmap) this.bitmapList.get(i), this.averageWidth * j, 0, new Rect(this.averageWidth * j, 0, (j + 1) * this.averageWidth, this.viewHeight));
                    }
                    this.bitmaps[i][j] = partBitmap;
                }
            }
        }
    }

    private void initIndex() {
        int listSize = this.bitmapList.size();
        this.nextIndex = this.currIndex + 1;
        this.preIndex = this.currIndex - 1;
        if (this.nextIndex > listSize - 1) {
            this.nextIndex = 0;
        }
        if (this.preIndex < 0) {
            this.preIndex = listSize - 1;
        }
    }

    @SuppressLint({"NewApi"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.bitmapList != null && this.bitmapList.size() > 0) {
            switch (m15x7caf88d4()[this.rollMode.ordinal()]) {
                case 1:
                    drawRollWhole3D(canvas, true);
                    return;
                case 2:
                    drawRollWhole3D(canvas, false);
                    return;
                case 3:
                    drawSepartConbine(canvas);
                    return;
                case 4:
                    drawRollInTurn(canvas);
                    return;
                case 5:
                    drawJalousie(canvas);
                    return;
                default:
                    return;
            }
        }
    }

    private void drawRollWhole3D(Canvas canvas, boolean draw2D) {
        Bitmap currWholeBitmap = (Bitmap) this.bitmapList.get(this.currIndex);
        Bitmap nextWholeBitmap = (Bitmap) this.bitmapList.get(this.nextIndex);
        canvas.save();
        if (this.direction == 1) {
            this.camera.save();
            if (draw2D) {
                this.camera.rotateX(0.0f);
            } else {
                this.camera.rotateX(-this.rotateDegree);
            }
            this.camera.getMatrix(this.matrix);
            this.camera.restore();
            this.matrix.preTranslate((float) ((-this.viewWidth) / 2), 0.0f);
            this.matrix.postTranslate((float) (this.viewWidth / 2), this.axisY);
            canvas.drawBitmap(currWholeBitmap, this.matrix, this.paint);
            this.camera.save();
            if (draw2D) {
                this.camera.rotateX(0.0f);
            } else {
                this.camera.rotateX(90.0f - this.rotateDegree);
            }
            this.camera.getMatrix(this.matrix);
            this.camera.restore();
            this.matrix.preTranslate((float) ((-this.viewWidth) / 2), (float) (-this.viewHeight));
            this.matrix.postTranslate((float) (this.viewWidth / 2), this.axisY);
            canvas.drawBitmap(nextWholeBitmap, this.matrix, this.paint);
        } else {
            this.camera.save();
            if (draw2D) {
                this.camera.rotateY(0.0f);
            } else {
                this.camera.rotateY(this.rotateDegree);
            }
            this.camera.getMatrix(this.matrix);
            this.camera.restore();
            this.matrix.preTranslate(0.0f, (float) ((-this.viewHeight) / 2));
            this.matrix.postTranslate(this.axisX, (float) (this.viewHeight / 2));
            canvas.drawBitmap(currWholeBitmap, this.matrix, this.paint);
            this.camera.save();
            if (draw2D) {
                this.camera.rotateY(0.0f);
            } else {
                this.camera.rotateY(this.rotateDegree - 90.0f);
            }
            this.camera.getMatrix(this.matrix);
            this.camera.restore();
            this.matrix.preTranslate((float) (-this.viewWidth), (float) ((-this.viewHeight) / 2));
            this.matrix.postTranslate(this.axisX, (float) (this.viewHeight / 2));
            canvas.drawBitmap(nextWholeBitmap, this.matrix, this.paint);
        }
        canvas.restore();
    }

    private void drawSepartConbine(Canvas canvas) {
        for (int i = 0; i < this.partNumber; i++) {
            Bitmap currBitmap = this.bitmaps[this.currIndex][i];
            Bitmap nextBitmap = this.bitmaps[this.nextIndex][i];
            canvas.save();
            if (this.direction == 1) {
                this.camera.save();
                this.camera.rotateX(-this.rotateDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) ((-currBitmap.getWidth()) / 2), 0.0f);
                this.matrix.postTranslate((float) ((currBitmap.getWidth() / 2) + (this.averageWidth * i)), this.axisY);
                canvas.drawBitmap(currBitmap, this.matrix, this.paint);
                this.camera.save();
                this.camera.rotateX(90.0f - this.rotateDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) ((-nextBitmap.getWidth()) / 2), (float) (-nextBitmap.getHeight()));
                this.matrix.postTranslate((float) ((nextBitmap.getWidth() / 2) + (this.averageWidth * i)), this.axisY);
                canvas.drawBitmap(nextBitmap, this.matrix, this.paint);
            } else {
                this.camera.save();
                this.camera.rotateY(this.rotateDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate(0.0f, (float) ((-currBitmap.getHeight()) / 2));
                this.matrix.postTranslate(this.axisX, (float) ((currBitmap.getHeight() / 2) + (this.averageHeight * i)));
                canvas.drawBitmap(currBitmap, this.matrix, this.paint);
                this.camera.save();
                this.camera.rotateY(this.rotateDegree - 90.0f);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) (-nextBitmap.getWidth()), (float) ((-nextBitmap.getHeight()) / 2));
                this.matrix.postTranslate(this.axisX, (float) ((nextBitmap.getHeight() / 2) + (this.averageHeight * i)));
                canvas.drawBitmap(nextBitmap, this.matrix, this.paint);
            }
            canvas.restore();
        }
    }

    private void drawRollInTurn(Canvas canvas) {
        for (int i = 0; i < this.partNumber; i++) {
            Bitmap currBitmap = this.bitmaps[this.currIndex][i];
            Bitmap nextBitmap = this.bitmaps[this.nextIndex][i];
            float tDegree = this.rotateDegree - ((float) (i * 30));
            if (tDegree < 0.0f) {
                tDegree = 0.0f;
            }
            if (tDegree > 90.0f) {
                tDegree = 90.0f;
            }
            canvas.save();
            if (this.direction == 1) {
                float tAxisY = (tDegree / 90.0f) * ((float) this.viewHeight);
                if (tAxisY > ((float) this.viewHeight)) {
                    tAxisY = (float) this.viewHeight;
                }
                if (tAxisY < 0.0f) {
                    tAxisY = 0.0f;
                }
                this.camera.save();
                this.camera.rotateX(-tDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) (-currBitmap.getWidth()), 0.0f);
                this.matrix.postTranslate((float) (currBitmap.getWidth() + (this.averageWidth * i)), tAxisY);
                canvas.drawBitmap(currBitmap, this.matrix, this.paint);
                this.camera.save();
                this.camera.rotateX(90.0f - tDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) (-nextBitmap.getWidth()), (float) (-nextBitmap.getHeight()));
                this.matrix.postTranslate((float) (nextBitmap.getWidth() + (this.averageWidth * i)), tAxisY);
                canvas.drawBitmap(nextBitmap, this.matrix, this.paint);
            } else {
                float tAxisX = (tDegree / 90.0f) * ((float) this.viewWidth);
                if (tAxisX > ((float) this.viewWidth)) {
                    tAxisX = (float) this.viewWidth;
                }
                if (tAxisX < 0.0f) {
                    tAxisX = 0.0f;
                }
                this.camera.save();
                this.camera.rotateY(tDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate(0.0f, (float) ((-currBitmap.getHeight()) / 2));
                this.matrix.postTranslate(tAxisX, (float) ((currBitmap.getHeight() / 2) + (this.averageHeight * i)));
                canvas.drawBitmap(currBitmap, this.matrix, this.paint);
                this.camera.save();
                this.camera.rotateY(tDegree - 90.0f);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) (-nextBitmap.getWidth()), (float) ((-nextBitmap.getHeight()) / 2));
                this.matrix.postTranslate(tAxisX, (float) ((nextBitmap.getHeight() / 2) + (this.averageHeight * i)));
                canvas.drawBitmap(nextBitmap, this.matrix, this.paint);
            }
            canvas.restore();
        }
    }

    private void drawJalousie(Canvas canvas) {
        for (int i = 0; i < this.partNumber; i++) {
            Bitmap currBitmap = this.bitmaps[this.currIndex][i];
            Bitmap nextBitmap = this.bitmaps[this.nextIndex][i];
            canvas.save();
            if (this.direction == 1) {
                if (this.rotateDegree < 90.0f) {
                    this.camera.save();
                    this.camera.rotateX(this.rotateDegree);
                    this.camera.getMatrix(this.matrix);
                    this.camera.restore();
                    this.matrix.preTranslate((float) ((-currBitmap.getWidth()) / 2), (float) ((-currBitmap.getHeight()) / 2));
                    this.matrix.postTranslate((float) (currBitmap.getWidth() / 2), (float) ((currBitmap.getHeight() / 2) + (this.averageHeight * i)));
                    canvas.drawBitmap(currBitmap, this.matrix, this.paint);
                } else {
                    this.camera.save();
                    this.camera.rotateX(180.0f - this.rotateDegree);
                    this.camera.getMatrix(this.matrix);
                    this.camera.restore();
                    this.matrix.preTranslate((float) ((-nextBitmap.getWidth()) / 2), (float) ((-nextBitmap.getHeight()) / 2));
                    this.matrix.postTranslate((float) (nextBitmap.getWidth() / 2), (float) ((nextBitmap.getHeight() / 2) + (this.averageHeight * i)));
                    canvas.drawBitmap(nextBitmap, this.matrix, this.paint);
                }
            } else if (this.rotateDegree < 90.0f) {
                this.camera.save();
                this.camera.rotateY(this.rotateDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) ((-currBitmap.getWidth()) / 2), (float) ((-currBitmap.getHeight()) / 2));
                this.matrix.postTranslate((float) ((currBitmap.getWidth() / 2) + (this.averageWidth * i)), (float) (currBitmap.getHeight() / 2));
                canvas.drawBitmap(currBitmap, this.matrix, this.paint);
            } else {
                this.camera.save();
                this.camera.rotateY(180.0f - this.rotateDegree);
                this.camera.getMatrix(this.matrix);
                this.camera.restore();
                this.matrix.preTranslate((float) ((-nextBitmap.getWidth()) / 2), (float) ((-nextBitmap.getHeight()) / 2));
                this.matrix.postTranslate((float) ((nextBitmap.getWidth() / 2) + (this.averageWidth * i)), (float) (nextBitmap.getHeight() / 2));
                canvas.drawBitmap(nextBitmap, this.matrix, this.paint);
            }
            canvas.restore();
        }
    }

    private Bitmap getPartBitmap(Bitmap bitmap, int x, int y, Rect rect) {
        return Bitmap.createBitmap(bitmap, x, y, rect.width(), rect.height());
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        initBitmaps();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.viewWidth = getMeasuredWidth();
        this.viewHeight = getMeasuredHeight();
        if (this.viewWidth != 0 && this.viewHeight != 0) {
            for (int i = 0; i < this.bitmapList.size(); i++) {
                this.bitmapList.set(i, scaleBitmap((Bitmap) this.bitmapList.get(i)));
            }
            initBitmaps();
            invalidate();
        }
    }

    public void setRotateDegree(float rotateDegree) {
        int i = 180;
        this.rotateDegree = rotateDegree;
        if (this.direction == 1) {
            if (this.rollMode != RollMode.Jalousie) {
                i = 90;
            }
            this.axisY = (rotateDegree / ((float) i)) * ((float) this.viewHeight);
        } else {
            if (this.rollMode != RollMode.Jalousie) {
                i = 90;
            }
            this.axisX = (rotateDegree / ((float) i)) * ((float) this.viewWidth);
        }
        invalidate();
    }

    public void setRollMode(RollMode rollMode) {
        this.rollMode = rollMode;
    }

    public void setRollDirection(int direction) {
        this.direction = direction;
        initBitmaps();
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        initBitmaps();
    }

    public void addImageBitmap(Bitmap bitmap) {
        this.bitmapList.add(bitmap);
        initBitmaps();
        invalidate();
    }

    public void removeBitmapAt(int index) {
        this.bitmapList.remove(index);
    }

    private Bitmap scaleBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) this.viewWidth) / ((float) width);
        float scaleHeight = ((float) this.viewHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
    }

    public void toNext() {
        if (!this.rolling) {
            if (this.rollMode == RollMode.RollInTurn) {
                this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, (float) (((this.partNumber - 1) * 30) + 90)});
            } else if (this.rollMode == RollMode.Jalousie) {
                this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 180.0f});
            } else {
                this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 90.0f});
            }
            this.rolling = true;
            this.valueAnimator.setDuration((long) this.rollDuration);
            this.valueAnimator.addUpdateListener(this.updateListener);
            this.valueAnimator.addListener(this.toNextAnimListener);
            this.valueAnimator.start();
        }
    }

    public void toPre() {
        if (!this.rolling) {
            int startRotate;
            if (this.rollMode == RollMode.RollInTurn) {
                startRotate = ((this.partNumber - 1) * 30) + 90;
            } else if (this.rollMode == RollMode.Jalousie) {
                startRotate = 180;
            } else {
                startRotate = 90;
            }
            rollIndex(true);
            setRotateDegree((float) startRotate);
            this.rolling = true;
            this.valueAnimator = ValueAnimator.ofFloat(new float[]{(float) startRotate, 0.0f});
            this.valueAnimator.setDuration((long) this.rollDuration);
            this.valueAnimator.addUpdateListener(this.updateListener);
            this.valueAnimator.addListener(this.toPreAnimListener);
            this.valueAnimator.start();
        }
    }

    private void rollIndex(boolean toPre) {
        if (toPre) {
            int temp = this.currIndex;
            this.currIndex = this.preIndex;
            this.preIndex = this.nextIndex;
            this.nextIndex = temp;
            return;
        }
        temp = this.currIndex;
        this.currIndex = this.nextIndex;
        this.nextIndex = this.preIndex;
        this.preIndex = temp;
    }

    public void setRollDuration(int rollDuration) {
        this.rollDuration = rollDuration;
    }
}
