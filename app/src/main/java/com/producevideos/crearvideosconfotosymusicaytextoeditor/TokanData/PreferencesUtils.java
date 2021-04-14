package com.producevideos.crearvideosconfotosymusicaytextoeditor.TokanData;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

public class PreferencesUtils {
    public static final String DEVICE_TOKEN = "device_token";
    public static final String EXIT_JSON = "exit_json";
    public static String PREF_NAME = null;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String SPLASH_1_JSON = "splash1_json";
    public static final String TIME_OF_GET_APP_EXIT = "time_of_get_app_EXIT";
    public static final String TIME_OF_GET_APP_SPLASH = "time_of_get_app_splash";
    private static Context mContext;
    private static PreferencesUtils preferencesUtils;

    public static PreferencesUtils getInstance(Context context) {
        mContext = context;
        PREF_NAME = context.getString(R.string.app_name);
        if (preferencesUtils == null) {
            preferencesUtils = new PreferencesUtils();
        }
        return preferencesUtils;
    }

    public String getPrefString(String key) {
        if (mContext != null) {
            return mContext.getSharedPreferences(PREF_NAME, 0).getString(key, "");
        }
        return "";
    }

    public void setPrefString(String key, String value) {
        Editor editor = mContext.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
