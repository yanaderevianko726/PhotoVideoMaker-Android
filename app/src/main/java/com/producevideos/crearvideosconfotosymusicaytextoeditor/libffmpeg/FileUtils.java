package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.TokanData.Glob;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileUtils {
    public static File mSdCard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    public static File APP_DIRECTORY = new File(mSdCard, Glob.app_name);
    public static final File TEMP_DIRECTORY = new File(APP_DIRECTORY, ".temp");
    public static final File TEMP_DIRECTORY_AUDIO = new File(APP_DIRECTORY, ".temp_audio");
    public static final File TEMP_VID_DIRECTORY = new File(TEMP_DIRECTORY, ".temp_vid");
    static final String ffmpegFileName = "ffmpeg";
    public static final File frameFile = new File(APP_DIRECTORY, ".frame.png");
    public static final String hiddenDirectoryName = ".MyGalaryLock/";
    public static final String hiddenDirectoryNameImage = "Image/";
    public static final String hiddenDirectoryNameThumbImage = ".thumb/Image/";
    public static final String hiddenDirectoryNameThumbVideo = ".thumb/Video/";
    public static final String hiddenDirectoryNameVideo = "Video/";
    public static long mDeleteFileCount = 0;

    private static File[] mStorageList;
    public static final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
    public static String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
    public static String unlockDirectoryNameImage = "GalaryLock/Image/";
    public static String unlockDirectoryNameVideo = "GalaryLock/Video/";

    static class AnonymousClass1 extends Thread {
        private final File val$child;

        AnonymousClass1(File file) {
            this.val$child = file;
        }

        public void run() {
            FileUtils.deleteFile(this.val$child);
        }
    }

    static {
        if (!TEMP_DIRECTORY.exists()) {
            TEMP_DIRECTORY.mkdirs();
        }
        if (!TEMP_VID_DIRECTORY.exists()) {
            TEMP_VID_DIRECTORY.mkdirs();
        }
    }

    public static File getVideoDirectory() {
        if (!TEMP_VID_DIRECTORY.exists()) {
            TEMP_VID_DIRECTORY.mkdirs();
        }
        return TEMP_VID_DIRECTORY;
    }

    public static File getVideoFile(int vNo) {
        if (!TEMP_VID_DIRECTORY.exists()) {
            TEMP_VID_DIRECTORY.mkdirs();
        }
        return new File(TEMP_VID_DIRECTORY, String.format("vid_%03d.mp4", new Object[]{Integer.valueOf(vNo)}));
    }

    public static File getImageDirectory(int iNo) {
        File imageDir = new File(TEMP_DIRECTORY, String.format("IMG_%03d", new Object[]{Integer.valueOf(iNo)}));
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return imageDir;
    }

    public static File getImageDirectory(String theme) {
        File imageDir = new File(TEMP_DIRECTORY, theme);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return imageDir;
    }

    public static File getImageDirectory(String theme, int iNo) {
        File imageDir = new File(getImageDirectory(theme), String.format("IMG_%03d", new Object[]{Integer.valueOf(iNo)}));
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return imageDir;
    }

    public static boolean deleteThemeDir(String theme) {
        return deleteFile(getImageDirectory(theme));
    }

    public FileUtils() {
        mDeleteFileCount = 0;
    }

    private static File[] getStorge() {
        List<File> storage = new ArrayList();
        if (rawExternalStorage != null) {
            storage.add(new File(rawExternalStorage));
        } else if (mSdCard != null) {
            storage.add(mSdCard);
        }
        if (rawSecondaryStoragesStr != null) {
            storage.add(new File(rawSecondaryStoragesStr));
        }
        mStorageList = new File[storage.size()];
        for (int i = 0; i < storage.size(); i++) {
            mStorageList[i] = (File) storage.get(i);
        }
        return mStorageList;
    }

    public static File[] getStorages() {
        if (mStorageList != null) {
            return mStorageList;
        }
        return getStorge();
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < ((long) unit)) {
            return new StringBuilder(String.valueOf(bytes)).append(" B").toString();
        }
        String pre = new StringBuilder(String.valueOf((si ? "kMGTPE" : "KMGTPE").charAt(((int) (Math.log((double) bytes) / Math.log((double) unit))) - 1))).append(si ? "" : "i").toString();
        return String.format("%.1f %sB", new Object[]{Double.valueOf(((double) bytes) / Math.pow((double) unit, (double) ((int) (Math.log((double) bytes) / Math.log((double) unit))))), pre});
    }

    public static File getHiddenAppDirectory(File sdCard) {
        File file = new File(sdCard, hiddenDirectoryName);
        if (file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getMoveFolderpath(File oldFilePath, String newFolderName) {
        return new File(oldFilePath.getParentFile().getParentFile(), new StringBuilder(String.valueOf(newFolderName)).append("/").append(oldFilePath.getName()).toString());
    }

    public static File getRestoreVideoDirectory(File sdCFile, String folderName) {
        return new File(sdCFile, unlockDirectoryNameVideo + folderName);
    }

    public static File getRestoreImageDirectory(File sdCFile, String folderName) {
        return new File(sdCFile, unlockDirectoryNameImage + folderName);
    }

    public static File getHiddenVideoDirectory(File sdCFile, String folderName) {
        return new File(sdCFile, ".MyGalaryLock/Video/" + folderName);
    }

    public static File getHiddenImageDirectory(File sdCFile, String folderName) {
        return new File(sdCFile, ".MyGalaryLock/Image/" + folderName);
    }

    public static File getThumbnailDirectory(File sdCard, int type) {
        File file;
        if (type == 0) {
            file = new File(getHiddenAppDirectory(sdCard), hiddenDirectoryNameThumbVideo);
        } else {
            file = new File(getHiddenAppDirectory(sdCard), hiddenDirectoryNameThumbImage);
        }
        if (file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getHiddenImageAppDirectory(File sdCard) {
        File file = new File(getHiddenAppDirectory(sdCard), hiddenDirectoryNameImage);
        if (file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getHiddenVideoAppDirectory(File sdCard) {
        File file = new File(getHiddenAppDirectory(sdCard), hiddenDirectoryNameVideo);
        if (file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static ArrayList<File> getCacheDirectory(String packageName) {
        ArrayList<File> cacheDirs = new ArrayList();
        for (File sdCard : getStorages()) {
            File file = new File(sdCard, "Android/data/" + packageName + "/cache");
            if (file.exists()) {
                cacheDirs.add(file);
            }
            file = new File(sdCard, "Android/data/" + packageName + "/Cache");
            if (file.exists()) {
                cacheDirs.add(file);
            }
            file = new File(sdCard, "Android/data/" + packageName + "/.cache");
            if (file.exists()) {
                cacheDirs.add(file);
            }
            file = new File(sdCard, "Android/data/" + packageName + "/.Cache");
            if (file.exists()) {
                cacheDirs.add(file);
            }
        }
        return cacheDirs;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static File getOutputImageFile() {
        if (APP_DIRECTORY.exists() || APP_DIRECTORY.mkdirs()) {
            return new File(APP_DIRECTORY, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
        }
        return null;
    }

    public static Bitmap getPicFromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Options o = new Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, o);
        int scale = 1;
        if (o.outWidth >= o.outHeight) {
            if (o.outWidth >= 1024) {
                scale = Math.round(((float) o.outWidth) / 1024.0f);
            }
        } else if (o.outHeight >= 1024) {
            scale = Math.round(((float) o.outHeight) / 1024.0f);
        }
        Options o2 = new Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, o2).copy(Config.RGB_565, true);
    }

    public static long getDirectorySize(File directory) {
        long dirSize = 0;
        if (directory != null) {
            File[] listFile = directory.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (File mFile : listFile) {
                    if (mFile.isDirectory()) {
                        dirSize += getDirectorySize(mFile);
                    } else {
                        dirSize += mFile.length();
                    }
                }
            }
        }
        return dirSize;
    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len <= 0) {
                in.close();
                out.close();
                return;
            }
            out.write(buf, 0, len);
        }
    }

    public static void deleteTempDir() {
        for (File child : TEMP_DIRECTORY.listFiles()) {
            new AnonymousClass1(child).start();
        }
    }

    public static boolean deleteFile(File mFile) {
        int i = 0;
        boolean idDelete = false;
        if (mFile == null) {
            return false;
        }
        if (mFile.exists()) {
            if (mFile.isDirectory()) {
                File[] children = mFile.listFiles();
                if (children != null && children.length > 0) {
                    int length = children.length;
                    while (i < length) {
                        File child = children[i];
                        mDeleteFileCount += child.length();
                        idDelete = deleteFile(child);
                        i++;
                    }
                }
                mDeleteFileCount += mFile.length();
                idDelete = mFile.delete();
            } else {
                mDeleteFileCount += mFile.length();
                idDelete = mFile.delete();
            }
        }
        return idDelete;
    }

    public static boolean deleteFile(String s) {
        return deleteFile(new File(s));
    }

    public static void moveFile(File src, File des) throws IOException {
        if (src.exists() && !src.renameTo(des)) {
            if (!des.getParentFile().exists()) {
                des.getParentFile().mkdirs();
            }
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(des);
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len <= 0) {
                    break;
                }
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            if (src.exists()) {
                src.delete();
            }
        }
    }

    public static void moveFile(String sourceLocation, String targetLocation) throws IOException {
        if (!new File(sourceLocation).renameTo(new File(targetLocation))) {
            File file = new File(sourceLocation);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len <= 0) {
                    break;
                }
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            File deleteFile = new File(sourceLocation);
            if (deleteFile.exists()) {
                deleteFile.delete();
            }
        }
    }

    public static String getFileFormat(String id) {
        String format = System.currentTimeMillis() + "_0";
        if (id.endsWith("_1")) {
            return ".jpg";
        }
        if (id.endsWith("_2")) {
            return ".JPG";
        }
        if (id.endsWith("_3")) {
            return ".png";
        }
        if (id.endsWith("_4")) {
            return ".PNG";
        }
        if (id.endsWith("_5")) {
            return ".jpeg";
        }
        if (id.endsWith("_6")) {
            return ".JPEG";
        }
        if (id.endsWith("_7")) {
            return ".mp4";
        }
        if (id.endsWith("_8")) {
            return ".3gp";
        }
        if (id.endsWith("_9")) {
            return ".flv";
        }
        if (id.endsWith("_10")) {
            return ".m4v";
        }
        if (id.endsWith("_11")) {
            return ".avi";
        }
        if (id.endsWith("_12")) {
            return ".wmv";
        }
        if (id.endsWith("_13")) {
            return ".mpeg";
        }
        if (id.endsWith("_14")) {
            return ".VOB";
        }
        if (id.endsWith("_15")) {
            return ".MOV";
        }
        if (id.endsWith("_16")) {
            return ".MPEG4";
        }
        if (id.endsWith("_17")) {
            return ".DivX";
        }
        if (id.endsWith("_18")) {
            return ".mkv";
        }
        return format;
    }

    public static String genrateFileId(String filePath) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String format = String.valueOf(System.currentTimeMillis());
        if (filePath.endsWith(".jpg")) {
            return new StringBuilder(String.valueOf(format)).append("_1").toString();
        }
        if (filePath.endsWith(".JPG")) {
            return new StringBuilder(String.valueOf(format)).append("_2").toString();
        }
        if (filePath.endsWith(".png")) {
            return new StringBuilder(String.valueOf(format)).append("_3").toString();
        }
        if (filePath.endsWith(".PNG")) {
            return new StringBuilder(String.valueOf(format)).append("_4").toString();
        }
        if (filePath.endsWith(".jpeg")) {
            return new StringBuilder(String.valueOf(format)).append("_5").toString();
        }
        if (filePath.endsWith(".JPEG")) {
            return new StringBuilder(String.valueOf(format)).append("_6").toString();
        }
        if (filePath.endsWith(".mp4")) {
            return new StringBuilder(String.valueOf(format)).append("_7").toString();
        }
        if (filePath.endsWith(".3gp")) {
            return new StringBuilder(String.valueOf(format)).append("_8").toString();
        }
        if (filePath.endsWith(".flv")) {
            return new StringBuilder(String.valueOf(format)).append("_9").toString();
        }
        if (filePath.endsWith(".m4v")) {
            return new StringBuilder(String.valueOf(format)).append("_10").toString();
        }
        if (filePath.endsWith(".avi")) {
            return new StringBuilder(String.valueOf(format)).append("_11").toString();
        }
        if (filePath.endsWith(".wmv")) {
            return new StringBuilder(String.valueOf(format)).append("_12").toString();
        }
        if (filePath.endsWith(".mpeg")) {
            return new StringBuilder(String.valueOf(format)).append("_13").toString();
        }
        if (filePath.endsWith(".VOB")) {
            return new StringBuilder(String.valueOf(format)).append("_14").toString();
        }
        if (filePath.endsWith(".MOV")) {
            return new StringBuilder(String.valueOf(format)).append("_15").toString();
        }
        if (filePath.endsWith(".MPEG4")) {
            return new StringBuilder(String.valueOf(format)).append("_16").toString();
        }
        if (filePath.endsWith(".DivX")) {
            return new StringBuilder(String.valueOf(format)).append("_17").toString();
        }
        if (filePath.endsWith(".mkv")) {
            return new StringBuilder(String.valueOf(format)).append("_18").toString();
        }
        return format;
    }

    public static String getDuration(long milliseconds) {
        String format = "";
        String secondsString = "";
        String minutesString = "";
        int hours = (int) (milliseconds / 3600000);
        int minutes = ((int) (milliseconds % 3600000)) / 60000;
        int seconds = (int) (((milliseconds % 3600000) % 60000) / 1000);
        if (hours > 0) {
            format = hours + ":";
        }
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        return format + minutesString + ":" + secondsString;
    }

    public static char[] readPatternData(Context activity) {
        try {
            if (new File(activity.getFilesDir() + "/pattern").exists()) {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(activity.getFilesDir() + "/pattern"));
                char[] array = (char[]) objectInputStream.readObject();
                objectInputStream.close();
                return array;
            }
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressLint({"LongLogTag"})
    static boolean copyBinaryFromAssetsToData(Context context, String fileNameFromAssets, String outputFileName) {
        File filesDirectory = getFilesDirectory(context);
        try {
            InputStream is = context.getAssets().open(fileNameFromAssets);
            OutputStream os = new FileOutputStream(new File(filesDirectory, outputFileName));
            byte[] buffer = new byte[4096];
            while (true) {
                int n = is.read(buffer);
                if (-1 == n) {
                    Util.close(os);
                    Util.close(is);
                    return true;
                }
                os.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e("issue in coping binary from assets to data. ", e.getMessage());
            return false;
        }
    }

    static File getFilesDirectory(Context context) {
        return context.getFilesDir();
    }

    public static String getFFmpeg(Context context) {
        return new StringBuilder(String.valueOf(getFilesDirectory(context).getAbsolutePath())).append(File.separator).append(ffmpegFileName).toString();
    }

    static String getFFmpeg(Context context, Map<String, String> environmentVars) {
        String ffmpegCommand = "";
        if (environmentVars != null) {
            for (Entry<String, String> var : environmentVars.entrySet()) {
                ffmpegCommand = new StringBuilder(String.valueOf(ffmpegCommand)).append((String) var.getKey()).append("=").append((String) var.getValue()).append(" ").toString();
            }
        }
        return new StringBuilder(String.valueOf(ffmpegCommand)).append(getFFmpeg(context)).toString();
    }

    static String SHA1(String file) {
        InputStream is = null;
        try {
            InputStream is2 = new BufferedInputStream(new FileInputStream(file));
            try {
                String SHA1 = SHA1(is2);
                Util.close(is2);
                is = is2;
                return SHA1;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        } catch (IOException e3) {
            Log.e("IOException", e3.getMessage());
            Util.close(is);
            return null;
        }
    }

    static String SHA1(InputStream is) {
        String str = String.valueOf(0);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] buffer = new byte[4096];
            while (true) {
                int read = is.read(buffer);
                if (read == -1) {
                    break;
                }
                messageDigest.update(buffer, 0, read);
            }
            Formatter formatter = new Formatter();
            int length = messageDigest.digest().length;
            str = formatter.toString();
            return str;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (IOException e2) {
            return null;
        } finally {
            Util.close(is);
        }
    }

    public static void addVideoToConcat(int i) {
        appendLog(new File(getVideoDirectory(), "video.txt"), String.format("file '%s'", new Object[]{getVideoFile(i).getAbsolutePath()}));
    }

    public static void addImageTovideo(File file) {
        appendLog(getVideoDirectory(), String.format("file '%s'", new Object[]{file.getAbsolutePath()}));
    }

    public static void appendLog(File parent, String text) {
        File logFile = parent;
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
