package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.Album;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.TokanData.Glob;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FileUtils;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;

import java.io.File;
import java.util.ArrayList;

public class MyCreationActivity extends AppCompatActivity implements OnClickListener {

    public static final String TAG = "MyCreationActivity";
    static int id;
    public static int screenWidth;

    private ImageView ivBack;

    private MyAlbum_Adapter myAlbum_adapter;
    private RecyclerView rv_my_album;
    private ArrayList<Album> videoList;

    class C07953 implements Runnable {
        C07953() {
        }

        public void run() {
            if (!MyCreationActivity.this.isFinishing()) {
                MyCreationActivity.this.showDialogforshow();
            }
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private ProgressDialog progress;

        private AsyncTaskRunner() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progress = new ProgressDialog(MyCreationActivity.this);
            this.progress.setMessage(MyCreationActivity.this.getString(R.string.loading));
            this.progress.setProgressStyle(0);
            MyCreationActivity.this.videoList.clear();
            Glob.fileList.clear();
            this.progress.show();
        }

        protected String doInBackground(String... params) {
            MyCreationActivity.this.videoList = Glob.getfile(FileUtils.APP_DIRECTORY, "video", MyCreationActivity.this);
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            this.progress.dismiss();
            MyCreationActivity.this.myAlbum_adapter = new MyAlbum_Adapter();
            MyCreationActivity.this.rv_my_album.setAdapter(MyCreationActivity.this.myAlbum_adapter);
        }
    }

    private class MenuItemClickListener implements OnMenuItemClickListener {
        Album videoData;

        public MenuItemClickListener(Album videoData) {
            this.videoData = videoData;
        }

        public boolean onMenuItemClick(MenuItem menu) {

            AdmobCls.Interstitialload();

            final int pos = MyCreationActivity.this.videoList.indexOf(this.videoData);

            switch (menu.getItemId()) {
                case R.id.action_delete:
                    Builder builder = new Builder(MyCreationActivity.this, R.style.Theme_MovieMaker_AlertDialog);
                    builder.setTitle(getString(R.string.delete_video));
                    builder.setMessage(getString(R.string.sure_to_delete) + new File(((Album) MyCreationActivity.this.videoList.get(pos)).strImagePath).getName() + " ?");
                    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtils.deleteFile(MyCreationActivity.this.videoList.remove(pos).strImagePath);
                            MyCreationActivity.this.myAlbum_adapter.notifyItemRemoved(pos);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancel), null);
                    builder.show();
                    new AsyncTaskRunner().execute(new String[0]);
                    break;

                case R.id.action_share_native:
                    File file = new File(MyCreationActivity.this.videoList.get(pos).strImagePath);
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    String imagePath =  getIntent().getExtras().get("FinalURI").toString();
                    ArrayList<Uri> streamUris = new ArrayList<Uri>();
                    for (int i = 0; i < 10; i++) {
                        File tmpFile = new File(imagePath);
                        String authority = getPackageName() + ".fileprovider";
                        Uri tmp = FileProvider.getUriForFile(MyCreationActivity.this,authority, tmpFile);
                        streamUris.add(tmp);
                    }
                    Intent shareIntent = new Intent("android.intent.action.SEND");
                    shareIntent.setType("video/*");
                    shareIntent.putExtra("android.intent.extra.SUBJECT", new File(((Album) MyCreationActivity.this.videoList.get(pos)).strImagePath).getName());
                    shareIntent.putExtra("android.intent.extra.TITLE", new File(((Album) MyCreationActivity.this.videoList.get(pos)).strImagePath).getName());
                    shareIntent.putExtra("android.intent.extra.STREAM", streamUris.get(0));
                    MyCreationActivity.this.startActivity(Intent.createChooser(shareIntent, MyCreationActivity.this.getString(R.string.share_video)));
                    break;
            }
            return false;
        }
    }

    private class MyAlbum_Adapter extends Adapter<MyAlbum_Adapter.MyViewHolder> {

        public class MyViewHolder extends ViewHolder {
            private final ImageView list_item_video_thumb;
            private final TextView list_item_video_title;
            private final Toolbar toolbar;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.list_item_video_thumb = itemView.findViewById(R.id.list_item_video_thumb);
                this.list_item_video_title = itemView.findViewById(R.id.list_item_video_title);
                this.toolbar = itemView.findViewById(R.id.list_item_video_toolbar);
            }
        }

        private MyAlbum_Adapter() {
        }

        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MyCreationActivity.this).inflate(R.layout.my_album_item, null));
        }

        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            Glide.with(MyCreationActivity.this).load(((Album) MyCreationActivity.this.videoList.get(position)).strImagePath).into(holder.list_item_video_thumb);
            holder.list_item_video_thumb.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MyCreationActivity.this.startActivity(new Intent(MyCreationActivity.this, PlayVideoFromMyCreationActivity.class).putExtra("video_path", ((Album) MyCreationActivity.this.videoList.get(position)).strImagePath));
                }
            });
            holder.list_item_video_title.setText(new File(((Album) MyCreationActivity.this.videoList.get(position)).strImagePath).getName());
            MyCreationActivity.menu(holder.toolbar, R.menu.home_item_exported_video_local_menu, new MenuItemClickListener((Album) MyCreationActivity.this.videoList.get(position)));
        }

        public int getItemCount() {
            return MyCreationActivity.this.videoList.size();
        }
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_my_creation);

        AdmobCls.Interstitialload();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getWindow().setFlags(1024, 1024);
        this.videoList = new ArrayList();

        rateUs();
        bindView();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        new AsyncTaskRunner().execute(new String[0]);
    }

    private void bindView() {
        this.ivBack = findViewById(R.id.ivBack);
        this.ivBack.setOnClickListener(this);
        this.rv_my_album = findViewById(R.id.rv_my_album);
        this.rv_my_album.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
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

    @SuppressLint("WrongConstant")
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StartActivity.class).addFlags(335544320));
        finish();
    }

    public static void menu(Toolbar paramToolbar, int paramInt, OnMenuItemClickListener paramOnMenuItemClickListener) {
        paramToolbar.getMenu().clear();
        paramToolbar.inflateMenu(paramInt);
        paramToolbar.setOnMenuItemClickListener(paramOnMenuItemClickListener);
    }
    protected void onResume() {
        super.onResume();
    }

    private void rateUs() {
        id = Glob.getPref(this, "dialog_count");
        if (id == 1 && !isFinishing()) {
            new Handler().postDelayed(new C07953(), 3000);
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

        TextView textView = dialog.findViewById(R.id.rate);
        TextView textView2 = dialog.findViewById(R.id.remindlater);
        TextView textView3 = dialog.findViewById(R.id.nothanks);

        ((ImageView) dialog.findViewById(R.id.img)).setAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MyCreationActivity.this.gotoStore();
                MyCreationActivity.id++;
                Glob.setPref(MyCreationActivity.this, "dialog_count", MyCreationActivity.id);
                Glob.setBoolPref(MyCreationActivity.this, "isRated", true);
                dialog.dismiss();
            }
        });
        textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Glob.dialog = false;
                Glob.setBoolPref(MyCreationActivity.this, "isRated", false);
                Glob.setPref(MyCreationActivity.this, "dialog_count", 1);
                dialog.cancel();
            }
        });
        textView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Glob.setBoolPref(MyCreationActivity.this, "isRated", false);
                Glob.setPref(MyCreationActivity.this, "dialog_count", 1);
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
        myAppLinkToMarket.setFlags(26846824);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.dont_have_google_play), Toast.LENGTH_LONG).show();
        }
    }

}
