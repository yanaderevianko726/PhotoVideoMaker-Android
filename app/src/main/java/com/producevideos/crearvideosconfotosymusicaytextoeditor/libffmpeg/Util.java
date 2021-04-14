package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Util {
    public static final boolean IS_GINGERBREAD_MR1;
    public static final boolean IS_ISC;
    public static final boolean IS_JBMR2 = (VERSION.SDK_INT >= 18);
    public static final File project_dir = new File(Environment.getExternalStorageDirectory(), "/EscrowFFmpeg");
    public static final File temp_animated_video_dir = new File(project_dir, "/.temp/anim_video");
    public static final File temp_audio_dir = new File(project_dir, "/.temp/audio");
    public static final File temp_simple_video_dir = new File(project_dir, "/.temp/video");

    static {
        boolean z;
        boolean z2 = true;
        if (VERSION.SDK_INT >= 14) {
            z = true;
        } else {
            z = false;
        }
        IS_ISC = z;
        if (VERSION.SDK_INT < 10) {
            z2 = false;
        }
        IS_GINGERBREAD_MR1 = z2;
        makeDirectorys();
    }

    public static void makeDirectorys() {
        if (!project_dir.exists()) {
            project_dir.mkdirs();
        }
        if (!temp_audio_dir.exists()) {
            temp_audio_dir.mkdirs();
        }
        if (!temp_simple_video_dir.exists()) {
            temp_simple_video_dir.mkdirs();
        }
        if (!temp_animated_video_dir.exists()) {
            temp_animated_video_dir.mkdirs();
        }
    }

    public static boolean isDebug(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationContext().getApplicationInfo();
        int i = applicationInfo.flags & 2;
        applicationInfo.flags = i;
        return i != 0;
    }

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static String convertInputStreamToString(InputStream inputStream) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String str = r.readLine();
                if (str == null) {
                    return sb.toString();
                }
                sb.append(str);
            }
        } catch (IOException e) {
            Log.m21e("error converting input stream to string", e);
            return null;
        }
    }

    public static void destroyProcess(Process process) {
        if (process != null) {
            process.destroy();
        }
    }

    public static boolean killAsync(AsyncTask<?, ?, ?> asyncTask) {
        return (asyncTask == null || asyncTask.isCancelled() || !asyncTask.cancel(true)) ? false : true;
    }

    public static boolean isProcessCompleted(Process process) {
        if (process == null) {
            return true;
        }
        try {
            process.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            return false;
        }
    }
}
