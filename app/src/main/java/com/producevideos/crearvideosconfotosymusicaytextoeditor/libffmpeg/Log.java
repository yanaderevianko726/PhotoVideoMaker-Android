package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

class Log {
    private static boolean DEBUG = false;
    private static String TAG = FFmpeg.class.getSimpleName();

    Log() {
    }

    static void setDEBUG(boolean DEBUG) {
    }

    static void m20d(Object obj) {
        if (DEBUG) {
            android.util.Log.d(TAG, obj != null ? obj.toString() : null);
        }
    }

    static void m23i(Object obj) {
        if (DEBUG) {
            android.util.Log.i(TAG, obj != null ? obj.toString() : null);
        }
    }

    static void m21e(Object obj, Throwable throwable) {
        if (DEBUG) {
            android.util.Log.e(TAG, obj != null ? obj.toString() : null, throwable);
        }
    }

    static void m22e(Throwable throwable) {
        if (DEBUG) {
            android.util.Log.e(TAG, "", throwable);
        }
    }
}
