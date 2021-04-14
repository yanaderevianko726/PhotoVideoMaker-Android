package com.producevideos.crearvideosconfotosymusicaytextoeditor.TokanData;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.Album;

import java.io.File;
import java.util.ArrayList;

//import com.google.android.exoplayer2.util.MimeTypes;

public class Glob {
    public static String app_name = "Photo Video Maker";
    public static boolean dialog = true;
    public static ArrayList<Album> fileList = new ArrayList();
     public static ArrayList<Album> getfile(File dir, String fileType, Context mContext) {
        File[] listFile = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            int i = 0;
            while (i < listFile.length) {
                if (listFile[i].isDirectory()) {
                    getfile(listFile[i], fileType, mContext);
                } else {
                    Album album = new Album();
                    if ("music".equals(fileType)) {
                        if (listFile[i].getName().endsWith(".mp3")) {
                            album.strImagePath = listFile[i].toString();
                            fileList.add(album);
                        }
                    } else if ("video".equals(fileType) && listFile[i].getName().endsWith(".mp4")) {
                        album.strImagePath = listFile[i].toString();
                        fileList.add(album);
                    }
                }
                i++;
            }
        }
        return fileList;
    }

   /* public static Boolean CheckNet(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean z = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return Boolean.valueOf(z);
    }*/

    public static boolean getBoolPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean(str, false);
    }

    public static int getPref(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getInt(str, 1);
    }

    public static void setBoolPref(Context context, String str, boolean z) {
        Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static void setPref(Context context, String str, int i) {
        context.getSharedPreferences(context.getPackageName(), 0).edit().putInt(str, i);
    }
}
