package com.producevideos.crearvideosconfotosymusicaytextoeditor;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.ImageData;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.MusicData;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FFmpeg;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.themes.THEMES;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.PermissionModelUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends Application {
    public static int VIDEO_HEIGHT = 480;
    public static int VIDEO_WIDTH = 720;
    private static MyApplication instance;
    public static boolean isBreak = false;
    public HashMap<String, ArrayList<ImageData>> allAlbum;
    private ArrayList<String> allFolder;
    int frame = -1;
    public boolean isEditModeEnable = false;
    public boolean isFromSdCardAudio = false;
    public int min_pos = Integer.MAX_VALUE;
    private MusicData musicData;
    private OnProgressReceiver onProgressReceiver;
    private float second = 2.0f;
    private String selectedFolderId = "";
    private final ArrayList<ImageData> selectedImages = new ArrayList();
    public THEMES selectedTheme = THEMES.Shine;
    public ArrayList<String> videoImages = new ArrayList();

    class C11551 implements FFmpegLoadBinaryResponseHandler {
        C11551() {
        }

        public void onStart() {
        }

        public void onFinish() {
        }

        public void onSuccess(String cpuType) {
            Log.e("ffmpeg", cpuType);
        }

        public void onFailure(String cpuType) {
            Log.e("ffmpeg", cpuType);
        }

        public void onSuccess() {
        }

        public void onFailure() {
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        if (!new PermissionModelUtil(this).needPermissionCheck()) {
            getFolderList();
        }
        try {
            loadLib();
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void initArray() {
        this.videoImages = new ArrayList();
    }

    public float getSecond() {
        return this.second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    public void setMusicData(MusicData musicData) {
        this.isFromSdCardAudio = false;
        this.musicData = musicData;
    }

    public MusicData getMusicData() {
        return this.musicData;
    }

    public void setSelectedFolderId(String selectedFolderId) {
        this.selectedFolderId = selectedFolderId;
    }

    public String getSelectedFolderId() {
        return this.selectedFolderId;
    }

    public HashMap<String, ArrayList<ImageData>> getAllAlbum() {
        return this.allAlbum;
    }

    public ArrayList<ImageData> getImageByAlbum(String folderId) {
        ArrayList<ImageData> arrayList = (ArrayList) getAllAlbum().get(folderId);
        if (arrayList == null) {
            return new ArrayList();
        }
        return arrayList;
    }

    public ArrayList<ImageData> getSelectedImages() {
        return this.selectedImages;
    }

    public void addSelectedImage(ImageData imageData) {
        this.selectedImages.add(imageData);
        imageData.imageCount++;
    }

    public void removeSelectedImage(int imageData) {
        if (imageData <= this.selectedImages.size()) {
            ImageData imageData2 = (ImageData) this.selectedImages.remove(imageData);
            imageData2.imageCount--;
        }
    }

    public void getFolderList() {
        this.allFolder = new ArrayList();
        this.allAlbum = new HashMap();
        String orderBy = "bucket_display_name";
        Cursor cur = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id", "bucket_display_name", "bucket_id", "datetaken"}, null, null, "bucket_display_name DESC");
        if (cur.moveToFirst()) {
            int bucketColumn = cur.getColumnIndex("bucket_display_name");
            int bucketIdColumn = cur.getColumnIndex("bucket_id");
            int dateColumn = cur.getColumnIndex("datetaken");
            setSelectedFolderId(cur.getString(bucketIdColumn));
            do {
                ImageData data = new ImageData();
                data.imagePath = cur.getString(cur.getColumnIndex("_data"));
                if (!data.imagePath.endsWith(".gif")) {
                    String date = cur.getString(dateColumn);
                    String folderName = cur.getString(bucketColumn);
                    String folderId = cur.getString(bucketIdColumn);
                    if (!this.allFolder.contains(folderId)) {
                        this.allFolder.add(folderId);
                    }
                    ArrayList<ImageData> imagePath = (ArrayList) this.allAlbum.get(folderId);
                    if (imagePath == null) {
                        imagePath = new ArrayList();
                    }
                    data.folderName = folderName;
                    imagePath.add(data);
                    this.allAlbum.put(folderId, imagePath);
                }
            } while (cur.moveToNext());
        }
    }

    private void loadLib() throws FFmpegNotSupportedException {
        FFmpeg.getInstance(this).loadBinary(new C11551());
    }

    public Typeface getApplicationTypeFace() {
        return null;
    }

    public void clearAllSelection() {
        this.videoImages.clear();
        this.allAlbum = null;
        getSelectedImages().clear();
        System.gc();
        getFolderList();
    }

    public void setCurrentTheme(String currentTheme) {
        Editor editor = getSharedPreferences("theme", 0).edit();
        editor.putString("current_theme", currentTheme);
        editor.commit();
    }

    public String getCurrentTheme() {
        return getSharedPreferences("theme", 0).getString("current_theme", THEMES.Shine.toString());
    }

    public void setOnProgressReceiver(OnProgressReceiver onProgressReceiver) {
        this.onProgressReceiver = onProgressReceiver;
    }

    public OnProgressReceiver getOnProgressReceiver() {
        return this.onProgressReceiver;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        for (RunningServiceInfo service : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setFrame(int data) {
        this.frame = data;
    }

    public int getFrame() {
        return this.frame;
    }
}
