package com.producevideos.crearvideosconfotosymusicaytextoeditor.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

@SuppressLint({"NewApi"})
public class PermissionModelUtil {
    public static final String[] NECESSARY_PERMISSIONS = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final String PERMISSION_CHECK_PREF = "marshmallow_permission_check";
    private Context context;
    private SharedPreferences sharedPrefs;

    class C06441 implements OnClickListener {
        C06441() {
        }

        public void onClick(DialogInterface dialog, int which) {
            PermissionModelUtil.this.requestPermissions();
            PermissionModelUtil.this.sharedPrefs.edit().putBoolean(PermissionModelUtil.PERMISSION_CHECK_PREF, false).commit();
        }
    }

    private PermissionModelUtil() {
    }

    public PermissionModelUtil(Context context) {
        this.context = context;
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @SuppressLint({"WrongConstant"})
    public boolean needPermissionCheck() {
        if (VERSION.SDK_INT < 23 || this.context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return false;
        }
        return true;
    }

    public void showPermissionExplanationThenAuthorization() {
        new Builder(this.context, R.style.Theme_MovieMaker_AlertDialog).setTitle(R.string.permission_check_title).setMessage(R.string.permission_check_message).setPositiveButton(R.string.ok, new C06441()).setCancelable(false).create().show();
    }

    private void requestPermissions() {
        ((Activity) this.context).requestPermissions(NECESSARY_PERMISSIONS, 1);
    }
}
