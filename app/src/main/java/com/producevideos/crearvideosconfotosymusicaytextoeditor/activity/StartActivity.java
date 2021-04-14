package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.service.CreateVideoService;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.service.ImageCreatorService;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartActivity extends AppCompatActivity implements OnClickListener {

    private boolean blnMyCreation = false;
    private boolean blnStart = false;
    public ImageView myalbum;
    public ImageView start;

    class C08281 implements DialogInterface.OnClickListener {
        C08281() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                StartActivity.this.checkAndRequestPermissions();
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(1024, 1024);

        new AdmobCls(this, new AdmobCls.AdsCallback() {
            @Override
            public void onLoaded() {

            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.start = findViewById(R.id.start);
        this.start.setOnClickListener(this);
        this.myalbum = findViewById(R.id.myalbum);
        this.myalbum.setOnClickListener(this);
        ImageView iv_share = findViewById(R.id.iv_share);
        ImageView iv_reta = findViewById(R.id.iv_reta);
        ImageView iv_privecy = findViewById(R.id.iv_privecy);


        iv_share.setOnClickListener(this);
        iv_reta.setOnClickListener(this);
        iv_privecy.setOnClickListener(this);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.myalbum:
                this.blnStart = false;
                this.blnMyCreation = true;
                if (VERSION.SDK_INT < 23) {
                    callmycreation();
                    return;
                } else if (checkAndRequestPermissions()) {
                    callmycreation();
                    return;
                } else {
                    return;
                }
            case R.id.start:
                this.blnStart = true;
                this.blnMyCreation = false;
                if (VERSION.SDK_INT < 23) {
                    callnext();
                    return;
                } else if (checkAndRequestPermissions()) {
                    callnext();
                    return;
                } else {
                    return;
                }
            case R.id.iv_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name));
                    String shareMessage = getString(R.string.recommend_this_app) + "\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.producevideos.crearvideosconfotosymusicaytextoeditor";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                    startActivity(Intent.createChooser(shareIntent, this.getString(R.string.choose_one)));
                } catch (Exception e) {
                }
                break;

            case R.id.iv_reta:
                Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i3);
                break;

            case R.id.iv_privecy:
                AdmobCls.Interstitialload();
                startActivity(new Intent(StartActivity.this, WebActivity.class));
                break;

            default:
                return;
        }
    }

    private void callmycreation() {
        blnMyCreation = false;
        startActivity(new Intent(StartActivity.this, MyCreationActivity.class));
    }



    private boolean checkAndRequestPermissions() {
        int writeStorage = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        int readStorage = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (writeStorage != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (readStorage != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2000);
        return false;
    }

    private void callnext() {
        if (isVideoInprocess()) {
            startActivity(new Intent(StartActivity.this, ProgressActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return;
        }
        startActivity(new Intent(StartActivity.this, ImageSelectionActivity.class));
        MyApplication.getInstance().getFolderList();
    }

    private boolean isVideoInprocess() {
        return MyApplication.isMyServiceRunning(this, CreateVideoService.class) || MyApplication.isMyServiceRunning(this, ImageCreatorService.class);
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2000:
                Map<String, Integer> perms = new HashMap();
                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.READ_EXTERNAL_STORAGE", Integer.valueOf(0));
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                    }
                    if (perms.get("android.permission.WRITE_EXTERNAL_STORAGE").intValue() == 0 && ((Integer) perms.get("android.permission.READ_EXTERNAL_STORAGE")).intValue() == 0) {
                        if (this.blnStart) {
                            callnext();
                            return;
                        } else if (this.blnMyCreation) {
                            callmycreation();
                            return;
                        } else {
                            return;
                        }
                    } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                        showDialogOK(this.getString(R.string.permission_required), new C08281());
                        return;
                    } else {
                        Toast.makeText(this, getString(R.string.enable_permission), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                return;
            default:
                return;
        }
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new Builder(this).setMessage(message).setPositiveButton((CharSequence) "OK", okListener).setNegativeButton((CharSequence) "Cancel", okListener).create().show();
    }

    public void onBackPressed() {
        showAlertDialogButtonClicked();
    }

    public void showAlertDialogButtonClicked() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exist));
        builder.setMessage(this.getString(R.string.want_exist));


        builder.setPositiveButton(this.getString(R.string.rate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchMarket();
            }
        });
        builder.setNeutralButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        builder.setNegativeButton(this.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, this.getString(R.string.cant_find_market), Toast.LENGTH_LONG).show();
        }
    }

}
