package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.OnProgressReceiver;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.TokanData.Glob;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.service.CreateVideoService;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.CircularFillableLoaders;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.FreshDownloadView;

public class ProgressActivity extends AppCompatActivity implements OnProgressReceiver {
    public static final String TAG = "ProgressActivity";
    static int id;
    private MyApplication application;
    private CircularFillableLoaders circularProgress;
    private FreshDownloadView freshDownloadView1;

    private TextView tvMsg;
    private TextView tv_click_here;
    private String videoPath;


    class C08052 implements OnClickListener {
        C08052() {
        }

        public void onClick(View v) {
            ProgressActivity.this.loadVideoPlay();
        }
    }

    class C08086 implements Runnable {
        C08086() {
        }

        public void run() {
            if (!ProgressActivity.this.isFinishing()) {
                ProgressActivity.this.showDialogforshow();
            }
        }
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        AdmobCls.Interstitialload();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getWindow().setFlags(1024, 1024);

        rateUs();
        getWindow().addFlags(128);
        this.application = MyApplication.getInstance();
        bindView();
    }

    private void bindView() {
        this.freshDownloadView1 = (FreshDownloadView) findViewById(R.id.freshDownloadView1);
        this.circularProgress = (CircularFillableLoaders) findViewById(R.id.circularProgress);
        this.tv_click_here = (TextView) findViewById(R.id.tv_click_here);
        this.tv_click_here.setOnClickListener(new C08052());
        this.tvMsg = (TextView) findViewById(R.id.tvMsg);
    }

    protected void onResume() {
        super.onResume();

        this.application.setOnProgressReceiver(this);
    }

    protected void onStop() {
        super.onStop();
        this.application.setOnProgressReceiver(null);
        if (MyApplication.isMyServiceRunning(this, CreateVideoService.class)) {
            finish();
        }
    }

    public void onBackPressed() {
    }

    public void onProgressFinish(String videoPath) {
        this.videoPath = videoPath;
        this.tv_click_here.setVisibility(View.VISIBLE);
        this.tvMsg.setVisibility(View.GONE);
    }

    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int alpha = Color.alpha(endValue.intValue());
        int startA = Color.alpha(startValue.intValue());
        int aDelta = (int) (((float) (alpha - startA)) * fraction);
        alpha = Color.red(endValue.intValue());
        int startR = Color.red(startValue.intValue());
        int rDelta = (int) (((float) (alpha - startR)) * fraction);
        alpha = Color.green(endValue.intValue());
        int startG = Color.green(startValue.intValue());
        int gDelta = (int) (((float) (alpha - startG)) * fraction);
        alpha = Color.blue(endValue.intValue());
        int startB = Color.blue(startValue.intValue());
        return Integer.valueOf(Color.argb(startA + aDelta, startR + rDelta, startG + gDelta, ((int) (((float) (alpha - startB)) * fraction)) + startB));
    }

    public void onImageProgressFrameUpdate(final float progress) {
        if (this.circularProgress != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    int p = (int) ((25.0f * progress) / 100.0f);
                    ProgressActivity.this.circularProgress.setProgress(p);
                    ProgressActivity.this.freshDownloadView1.upDateProgress(p);
                }
            });
        }
    }

    public void onVideoProgressFrameUpdate(final float progress) {
        if (this.circularProgress != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ProgressActivity.this.freshDownloadView1.upDateProgress((int) (25.0f + ((75.0f * progress) / 100.0f)));
                }
            });
        }
    }

    @SuppressLint({"WrongConstant"})
    private void loadVideoPlay() {
        Intent intent = new Intent(this, MyCreationActivity.class);
        intent.setFlags(268435456);
        intent.addFlags(268435456);
        intent.addFlags(67108864);
        intent.addFlags(67108864);
        intent.putExtra("android.intent.extra.TEXT", this.videoPath);
        startActivity(intent);
    }

    private void rateUs() {
        id = Glob.getPref(this, "dialog_count");
        if (id == 1 && !isFinishing()) {
            new Handler().postDelayed(new C08086(), 3000);
        }
        if (Glob.getBoolPref(this, "isRated")) {
            id++;
            if (id == 6) {
                id = 1;
            }
            Glob.setPref(this, "dialog_count", id);
        }
    }

    public void showDialogforshow() {
        final Dialog dialog = new Dialog(this, 16973839);
        dialog.requestWindowFeature(1);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = (int) (((double) displayMetrics.heightPixels) * 1.0d);
        int i2 = (int) (((double) displayMetrics.widthPixels) * 1.0d);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimarytrannew);
        dialog.getWindow().setLayout(i2, i);
        dialog.setContentView(R.layout.rate_dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.rate);
        TextView textView2 = (TextView) dialog.findViewById(R.id.remindlater);
        TextView textView3 = (TextView) dialog.findViewById(R.id.nothanks);

        ((ImageView) dialog.findViewById(R.id.img)).setAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProgressActivity.this.gotoStore();
                ProgressActivity.id++;
                Glob.setPref(ProgressActivity.this, "dialog_count", ProgressActivity.id);
                Glob.setBoolPref(ProgressActivity.this, "isRated", true);
                dialog.dismiss();
            }
        });
        textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Glob.dialog = false;
                Glob.setBoolPref(ProgressActivity.this, "isRated", false);
                Glob.setPref(ProgressActivity.this, "dialog_count", 1);
                dialog.cancel();
            }
        });
        textView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Glob.setBoolPref(ProgressActivity.this, "isRated", false);
                Glob.setPref(ProgressActivity.this, "dialog_count", 1);
                dialog.cancel();
            }
        });
        if (Glob.dialog) {
            dialog.show();
        }
    }

    @SuppressLint("WrongConstant")
    public void gotoStore() {
        Intent myAppLinkToMarket = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
        myAppLinkToMarket.setFlags(268468224);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }


}
