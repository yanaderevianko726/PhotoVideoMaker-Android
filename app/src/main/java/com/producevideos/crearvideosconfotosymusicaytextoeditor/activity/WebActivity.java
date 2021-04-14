package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;

public class WebActivity extends AppCompatActivity {


    private static final String TAG = "Main";
    private ProgressDialog progressBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        AdmobCls.Interstitialload();

        LinearLayout llt_banner = findViewById(R.id.llt_banner_web);
        AdmobCls ads = new AdmobCls();
        ads.BannerAd(this, llt_banner);

        getWindow().setFlags(1024, 1024);
        WebView webview = findViewById(R.id.webView);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(WebActivity.this).create();

        progressBar = ProgressDialog.show(WebActivity.this, this.getString(R.string.privacy_policy), this.getString(R.string.loading));

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle(getString(R.string.error));
                alertDialog.setMessage(description);
                alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });

        webview.loadUrl("https://www.google.com/");

    }

    public void onBackPressed() {
        finish();
    }
}
