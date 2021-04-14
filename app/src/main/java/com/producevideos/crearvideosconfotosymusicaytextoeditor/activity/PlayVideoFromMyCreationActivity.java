package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;

public class PlayVideoFromMyCreationActivity extends AppCompatActivity implements OnClickListener {
    private ImageView ivBack;
    private String strSavedVideo;
    private VideoView vvMyCreationVideo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_from_my_creation);

        AdmobCls.Interstitialload();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getWindow().setFlags(1024, 1024);
        this.strSavedVideo = getIntent().getStringExtra("video_path");
        bindView();
    }

    private void bindView() {
        this.vvMyCreationVideo = findViewById(R.id.vvMyCreationVideo);
        this.vvMyCreationVideo.setVideoURI(Uri.parse(this.strSavedVideo));
        MediaController controller = new MediaController(this);
        controller.setAnchorView(this.vvMyCreationVideo);
        controller.setMediaPlayer(this.vvMyCreationVideo);
        this.vvMyCreationVideo.setMediaController(controller);
        this.vvMyCreationVideo.start();
        this.ivBack = findViewById(R.id.ivBack);
        this.ivBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
