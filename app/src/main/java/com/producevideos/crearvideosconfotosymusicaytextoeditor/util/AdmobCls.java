package com.producevideos.crearvideosconfotosymusicaytextoeditor.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

public class AdmobCls {

    public static InterstitialAd mInterstitialAd;
    public static boolean loadedFromView = true;

    public AdmobCls(Context context, final AdsCallback callback){
        MobileAds.initialize(context, context.getString(R.string.admob_app_id));
        final AdRequest adIRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.admob_interstitial));
        mInterstitialAd.loadAd(adIRequest);

        mInterstitialAd.setAdListener(new AdListener()
        {
            public void onAdLoaded() {
                callback.onLoaded();
                if(loadedFromView){
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdClosed() {
                loadedFromView = false;
                AdRequest adIRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adIRequest);
            }
        });
    }

    public static void Interstitialload() {
        loadedFromView = true;
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else{
            AdRequest adIRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adIRequest);
        }
    }


    public void BannerAd(Activity context, LinearLayout adContainer) {

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(context.getResources().getString(R.string.admob_banner));
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        mAdView.setVisibility(View.GONE); // Change to View.Visible to show ads
        mAdView.loadAd(adRequestBuilder.build());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        adContainer.addView(mAdView, params);
        adContainer.invalidate();
    }

    public AdmobCls() {

    }

    public interface AdsCallback {
        void onLoaded();
    }
}
