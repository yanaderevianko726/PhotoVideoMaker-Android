package com.producevideos.crearvideosconfotosymusicaytextoeditor.service;

import android.app.IntentService;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.OnProgressReceiver;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.ImageData;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FileUtils;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.mask.FinalMaskBitmap3D;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.mask.FinalMaskBitmap3D.EFFECT;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.ScalingUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ImageCreatorService extends IntentService {
    public static final String EXTRA_SELECTED_THEME = "selected_theme";
    public static boolean isImageComplate = false;
    MyApplication application;
    ArrayList<ImageData> arrayList;
    private Builder mBuilder;
    private NotificationManager mNotifyManager;
    private String selectedTheme;
    int totalImages;

    public ImageCreatorService() {
        this(ImageCreatorService.class.getName());
    }

    public ImageCreatorService(String name) {
        super(name);
    }

    public void onCreate() {
        super.onCreate();
        this.application = MyApplication.getInstance();
    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        this.mNotifyManager = (NotificationManager) getSystemService("notification");
        this.mBuilder = new Builder(this);
        this.mBuilder.setContentTitle("Preparing Video").setContentText("Making in progress").setSmallIcon(R.mipmap.ic_launcher);
        this.selectedTheme = intent.getStringExtra(EXTRA_SELECTED_THEME);
        this.arrayList = this.application.getSelectedImages();
        this.application.initArray();
        isImageComplate = false;
        createImages();
    }

    private void createImages() {
        Bitmap newSecondBmp2 = null;
        this.totalImages = this.arrayList.size();
        int i = 0;
        while (i < this.arrayList.size() - 1) {
            if (!isSameTheme() || MyApplication.isBreak) {
                Log.e("ImageCreatorService", this.selectedTheme + " break");
                break;
            }
            Bitmap newFirstBmp;
            File imgDir = FileUtils.getImageDirectory(this.application.selectedTheme.toString(), i);
            Bitmap firstBitmap;
            Bitmap temp;
            if (i == 0) {
                firstBitmap = ScalingUtilities.checkBitmap(((ImageData) this.arrayList.get(i)).imagePath);
                temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                newFirstBmp = ScalingUtilities.ConvetrSameSizeNew(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                temp.recycle();
                firstBitmap.recycle();
                System.gc();
            } else {
                if (newSecondBmp2 == null || newSecondBmp2.isRecycled()) {
                    firstBitmap = ScalingUtilities.checkBitmap(((ImageData) this.arrayList.get(i)).imagePath);
                    temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    newSecondBmp2 = ScalingUtilities.ConvetrSameSizeNew(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    temp.recycle();
                    firstBitmap.recycle();
                }
                newFirstBmp = newSecondBmp2;
            }
            Bitmap secondBitmap = ScalingUtilities.checkBitmap(((ImageData) this.arrayList.get(i + 1)).imagePath);
            Bitmap temp2 = ScalingUtilities.scaleCenterCrop(secondBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            newSecondBmp2 = ScalingUtilities.ConvetrSameSizeNew(secondBitmap, temp2, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            temp2.recycle();
            secondBitmap.recycle();
            System.gc();
            EFFECT effect = (EFFECT) this.application.selectedTheme.getTheme().get(i % this.application.selectedTheme.getTheme().size());
            effect.initBitmaps(newFirstBmp, newSecondBmp2);
            int j = 0;
            while (((float) j) < FinalMaskBitmap3D.ANIMATED_FRAME) {
                if (!isSameTheme() || MyApplication.isBreak) {
                    Log.e("ImageCreatorService", this.selectedTheme + " break");
                    break;
                }
                Bitmap bitmap3 = effect.getMask(newFirstBmp, newSecondBmp2, j);
                if (isSameTheme()) {
                    File file = new File(imgDir, String.format("img%02d.jpg", new Object[]{Integer.valueOf(j)}));
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        OutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap3.compress(CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    boolean isBreak = false;
                    while (this.application.isEditModeEnable) {
                        Log.e("ImageCreatorService", "application.isEditModeEnable :");
                        if (this.application.min_pos != Integer.MAX_VALUE) {
                            i = this.application.min_pos;
                            isBreak = true;
                        }
                        if (MyApplication.isBreak) {
                            Log.e("ImageCreatorService", "application.isEditModeEnable Break:");
                            isImageComplate = true;
                            stopSelf();
                            return;
                        }
                    }
                    if (isBreak) {
                        ArrayList<String> str = new ArrayList();
                        str.addAll(this.application.videoImages);
                        this.application.videoImages.clear();
                        int size = Math.min(str.size(), Math.max(0, i - i) * 30);
                        for (int p = 0; p < size; p++) {
                            this.application.videoImages.add((String) str.get(p));
                        }
                        this.application.min_pos = Integer.MAX_VALUE;
                    }
                    if (!isSameTheme() || MyApplication.isBreak) {
                        Log.i("ImageCreatorService", this.selectedTheme + " :");
                        break;
                    }
                    this.application.videoImages.add(file.getAbsolutePath());
                    updateImageProgress();
                    if (((float) j) == FinalMaskBitmap3D.ANIMATED_FRAME - 1.0f) {
                        for (int m = 0; m < 8 && isSameTheme() && !MyApplication.isBreak; m++) {
                            this.application.videoImages.add(file.getAbsolutePath());
                            updateImageProgress();
                        }
                    }
                    j++;
                }
            }
            i++;
        }
        Glide.get(this).clearDiskCache();
        isImageComplate = true;
        stopSelf();
        isSameTheme();
    }

    private boolean isSameTheme() {
        return this.selectedTheme.equals(this.application.getCurrentTheme());
    }

    private void updateImageProgress() {
        final float progress = (100.0f * ((float) this.application.videoImages.size())) / ((float) ((this.totalImages - 1) * 30));
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                OnProgressReceiver receiver = ImageCreatorService.this.application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onImageProgressFrameUpdate(progress);
                }
            }
        });
    }

    private void updateNotification(int progress) {
        this.mBuilder.setProgress(100, (int) ((25.0f * ((float) progress)) / 100.0f), false);
        this.mNotifyManager.notify(1001, this.mBuilder.build());
    }
}
