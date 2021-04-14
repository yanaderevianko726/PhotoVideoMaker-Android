package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.FrameAdapter;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.ImageEditAdapter;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.MoviewThemeAdapter;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.ImageData;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.MusicData;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FileUtils;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.service.CreateVideoService;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.service.ImageCreatorService;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.themes.THEMES;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity implements OnClickListener, OnSeekBarChangeListener {
    private MyApplication application;
    private ArrayList<ImageData> arrayList;
    private BottomSheetBehavior<View> behavior;
    private Float[] duration = new Float[]{Float.valueOf(1.0f), Float.valueOf(1.5f), Float.valueOf(2.0f), Float.valueOf(2.5f), Float.valueOf(3.0f), Float.valueOf(3.5f), Float.valueOf(4.0f)};
    int f21i = 0;
    private View flLoader;
    int frame;
    private FrameAdapter frameAdapter;
    private RequestManager glide;
    private Handler handler = new Handler();
    protected int id;
    LayoutInflater inflater;
    boolean isFromTouch = false;
    private ImageView ivFrame;
    private View ivPlayPause;
    private ImageView ivPreview;
    ArrayList<ImageData> lastData = new ArrayList();
    private LinearLayout llEdit;
    private LockRunnable lockRunnable = new LockRunnable();
    private MediaPlayer mPlayer;
    private RecyclerView rvDuration;
    private RecyclerView rvFrame;
    private RecyclerView rvThemes;
    private float seconds = 2.0f;
    private SeekBar seekBar;
    private MoviewThemeAdapter themeAdapter;
    private Toolbar toolbar;
    private TextView tvEndTime;
    private TextView tvTime;

    class C05853 implements Runnable {
        C05853() {
        }

        public void run() {
            MyApplication.isBreak = false;
            PreviewActivity.this.application.videoImages.clear();
            PreviewActivity.this.application.min_pos = Integer.MAX_VALUE;
            Intent intent = new Intent(PreviewActivity.this.getApplicationContext(), ImageCreatorService.class);
            intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, PreviewActivity.this.application.getCurrentTheme());
            PreviewActivity.this.startService(intent);
        }
    }

    class C05864 extends Thread {
        C05864() {
        }

        public void run() {
            Glide.get(PreviewActivity.this).clearDiskCache();
        }
    }

    class C05875 implements DialogInterface.OnClickListener {
        C05875() {
        }

        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            PreviewActivity.this.application.videoImages.clear();
            MyApplication.isBreak = true;
            ((NotificationManager) PreviewActivity.this.getSystemService(NOTIFICATION_SERVICE)).cancel(1001);
            PreviewActivity.this.finish();
        }
    }

    class C05906 extends Thread {

        class C05892 implements Runnable {
            C05892() {
            }

            public void run() {
                PreviewActivity.this.reinitMusic();
                PreviewActivity.this.lockRunnable.play();
            }
        }

        C05906() {
        }

        public void run() {
            THEMES themes = PreviewActivity.this.application.selectedTheme;
            try {
                FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
                File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");
                if (tempFile.exists()) {
                    FileUtils.deleteFile(tempFile);
                }
                InputStream in = PreviewActivity.this.getResources().openRawResource(themes.getThemeMusic());
                FileOutputStream out = new FileOutputStream(tempFile);
                byte[] buff = new byte[1024];
                while (true) {
                    int read = in.read(buff);
                    if (read <= 0) {
                        break;
                    }
                    out.write(buff, 0, read);
                }
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(tempFile.getAbsolutePath());
                player.setAudioStreamType(3);
                player.prepare();
                final MusicData musicData = new MusicData();
                musicData.track_data = tempFile.getAbsolutePath();
                player.setOnPreparedListener(new OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        musicData.track_duration = (long) mp.getDuration();
                        mp.stop();
                    }
                });
                musicData.track_Title = "temp";
                PreviewActivity.this.application.setMusicData(musicData);
            } catch (Exception e) {
            }
            PreviewActivity.this.runOnUiThread(new C05892());
        }
    }

    class LockRunnable implements Runnable {
        ArrayList<ImageData> appList = new ArrayList();
        boolean isPause = false;

        class C05921 implements AnimationListener {
            C05921() {
            }

            public void onAnimationStart(Animation animation) {
                PreviewActivity.this.ivPlayPause.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                PreviewActivity.this.ivPlayPause.setVisibility(View.INVISIBLE);
            }
        }

        class C05932 implements AnimationListener {
            C05932() {
            }

            public void onAnimationStart(Animation animation) {
                PreviewActivity.this.ivPlayPause.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }
        }

        LockRunnable() {
        }

        void setAppList(ArrayList<ImageData> appList) {
            this.appList.clear();
            this.appList.addAll(appList);
        }

        public void run() {
            PreviewActivity.this.displayImage();
            if (!this.isPause) {
                PreviewActivity.this.handler.postDelayed(PreviewActivity.this.lockRunnable, (long) Math.round(50.0f * PreviewActivity.this.seconds));
            }
        }

        public boolean isPause() {
            return this.isPause;
        }

        public void play() {
            this.isPause = false;
            PreviewActivity.this.playMusic();
            PreviewActivity.this.handler.postDelayed(PreviewActivity.this.lockRunnable, (long) Math.round(50.0f * PreviewActivity.this.seconds));
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            animation.setAnimationListener(new C05921());
            PreviewActivity.this.ivPlayPause.startAnimation(animation);
            if (PreviewActivity.this.llEdit.getVisibility() != View.VISIBLE) {
                PreviewActivity.this.llEdit.setVisibility(View.VISIBLE);
                PreviewActivity.this.application.isEditModeEnable = false;
                if (ImageCreatorService.isImageComplate) {
                    Intent intent = new Intent(PreviewActivity.this.getApplicationContext(), ImageCreatorService.class);
                    intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, PreviewActivity.this.application.getCurrentTheme());
                    PreviewActivity.this.startService(intent);
                }
            }
            if (PreviewActivity.this.behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                PreviewActivity.this.behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }

        public void pause() {
            this.isPause = true;
            PreviewActivity.this.pauseMusic();
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            PreviewActivity.this.ivPlayPause.startAnimation(animation);
            animation.setAnimationListener(new C05932());
        }

        public void stop() {
            pause();
            PreviewActivity.this.f21i = 0;
            if (PreviewActivity.this.mPlayer != null) {
                PreviewActivity.this.mPlayer.stop();
            }
            PreviewActivity.this.reinitMusic();
            PreviewActivity.this.seekBar.setProgress(PreviewActivity.this.f21i);
        }
    }

    class C10211 extends BottomSheetCallback {
        C10211() {
        }

        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == 3 && !PreviewActivity.this.lockRunnable.isPause()) {
                PreviewActivity.this.lockRunnable.pause();
            }
        }

        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    }

    private class DurationAdapter extends Adapter<DurationAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckedTextView checkedTextView;

            @SuppressLint({"ResourceType"})
            public ViewHolder(View view) {
                super(view);
                this.checkedTextView = (CheckedTextView) view.findViewById(16908308);
            }
        }

        private DurationAdapter() {
        }

        public int getItemCount() {
            return PreviewActivity.this.duration.length;
        }

        public void onBindViewHolder(ViewHolder holder, int pos) {
            boolean z = true;
            final float dur = PreviewActivity.this.duration[pos].floatValue();
            holder.checkedTextView.setText(String.format("%.1f Second", new Object[]{Float.valueOf(dur)}));
            CheckedTextView checkedTextView = holder.checkedTextView;
            if (dur != PreviewActivity.this.seconds) {
                z = false;
            }
            checkedTextView.setChecked(z);
            holder.checkedTextView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreviewActivity.this.seconds = dur;
                    PreviewActivity.this.application.setSecond(PreviewActivity.this.seconds);
                    DurationAdapter.this.notifyDataSetChanged();
                    PreviewActivity.this.lockRunnable.play();
                }
            });
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            return new ViewHolder(PreviewActivity.this.inflater.inflate(R.layout.duration_list_item, parent, false));
        }
    }

    class C12412 extends SimpleTarget<Bitmap> {
        C12412() {
        }

        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            PreviewActivity.this.ivPreview.setImageBitmap(resource);
        }
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    private void reinitMusic() {
        Exception e;
        MusicData musicData = this.application.getMusicData();
        if (musicData != null) {
            this.mPlayer = MediaPlayer.create(this, Uri.parse(musicData.track_data));
            this.mPlayer.setLooping(true);
            try {
                this.mPlayer.prepare();
                return;
            } catch (Exception e2) {
                e = e2;
            }
        } else {
            return;
        }
        e.printStackTrace();
    }

    private void playMusic() {
        if (this.flLoader.getVisibility() != View.VISIBLE && this.mPlayer != null && !this.mPlayer.isPlaying()) {
            this.mPlayer.start();
        }
    }

    private void pauseMusic() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.application = MyApplication.getInstance();
        this.application.videoImages.clear();
        MyApplication.isBreak = false;
        Intent intent = new Intent(getApplicationContext(), ImageCreatorService.class);
        intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
        startService(intent);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_preview);

        AdmobCls.Interstitialload();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        bindView();
        init();
        addListner();
    }

    private void bindView() {
        this.flLoader = findViewById(R.id.flLoader);
        this.ivPreview = (ImageView) findViewById(R.id.previewImageView1);
        this.ivFrame = (ImageView) findViewById(R.id.ivFrame);
        this.seekBar = (SeekBar) findViewById(R.id.sbPlayTime);
        this.tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.llEdit = (LinearLayout) findViewById(R.id.llEdit);
        this.ivPlayPause = findViewById(R.id.ivPlayPause);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.rvThemes = (RecyclerView) findViewById(R.id.rvThemes);
        this.rvDuration = (RecyclerView) findViewById(R.id.rvDuration);
        this.rvFrame = (RecyclerView) findViewById(R.id.rvFrame);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView mTitle = (TextView) this.toolbar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void init() {
        this.seconds = this.application.getSecond();
        this.inflater = LayoutInflater.from(this);
        this.glide = Glide.with((FragmentActivity) this);
        this.application = MyApplication.getInstance();
        this.arrayList = this.application.getSelectedImages();
        this.seekBar.setMax((this.arrayList.size() - 1) * 30);
        int total = (int) (((float) (this.arrayList.size() - 1)) * this.seconds);
        this.tvEndTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(total / 60), Integer.valueOf(total % 60)}));
        setUpThemeAdapter();
        this.glide.load(((ImageData) this.application.getSelectedImages().get(0)).imagePath).into(this.ivPreview);
        this.behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        this.behavior.setBottomSheetCallback(new C10211());
        this.behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        setTheme();
    }

    private void setUpThemeAdapter() {
        this.themeAdapter = new MoviewThemeAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager((Context) this, 1, RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManagerFrame = new GridLayoutManager((Context) this, 1, RecyclerView.HORIZONTAL, false);
        this.rvThemes.setLayoutManager(gridLayoutManager);
        this.rvThemes.setItemAnimator(new DefaultItemAnimator());
        this.rvThemes.setAdapter(this.themeAdapter);
        this.frameAdapter = new FrameAdapter(this);
        this.rvFrame.setLayoutManager(gridLayoutManagerFrame);
        this.rvFrame.setItemAnimator(new DefaultItemAnimator());
        this.rvFrame.setAdapter(this.frameAdapter);
        this.rvDuration.setHasFixedSize(true);
        this.rvDuration.setLayoutManager(new LinearLayoutManager(this));
        this.rvDuration.setAdapter(new DurationAdapter());
    }

    private void addListner() {
        findViewById(R.id.ibAddImages).setOnClickListener(this);
        findViewById(R.id.video_clicker).setOnClickListener(this);
        this.seekBar.setOnSeekBarChangeListener(this);
        findViewById(R.id.ibAddMusic).setOnClickListener(this);
        findViewById(R.id.ibAddDuration).setOnClickListener(this);
        findViewById(R.id.ibEditMode).setOnClickListener(this);
    }

    private synchronized void displayImage() {
        try {
            if (this.f21i >= this.seekBar.getMax()) {
                this.f21i = 0;
                this.lockRunnable.stop();
            } else {
                if (this.f21i > 0 && this.flLoader.getVisibility() == View.VISIBLE) {
                    this.flLoader.setVisibility(View.GONE);
                    if (!(this.mPlayer == null || this.mPlayer.isPlaying())) {
                        this.mPlayer.start();
                    }
                }
                this.seekBar.setSecondaryProgress(this.application.videoImages.size());
                if (this.seekBar.getProgress() < this.seekBar.getSecondaryProgress()) {
                    this.f21i %= this.application.videoImages.size();
                    this.glide.load((String) this.application.videoImages.get(this.f21i)).asBitmap().signature(new MediaStoreSignature("image/*", System.currentTimeMillis(), 0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new C12412());
                    this.f21i++;
                    if (!this.isFromTouch) {
                        this.seekBar.setProgress(this.f21i);
                    }
                    int j = (int) ((((float) this.f21i) / 30.0f) * this.seconds);
                    int mm = j / 60;
                    int ss = j % 60;
                    this.tvTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(mm), Integer.valueOf(ss)}));
                    int total = (int) (((float) (this.arrayList.size() - 1)) * this.seconds);
                    this.tvEndTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(total / 60), Integer.valueOf(total % 60)}));
                }
            }
        } catch (Exception e) {
            this.glide = Glide.with(this);
        }
    }

    @SuppressLint({"WrongConstant"})
    public void onClick(View v) {

        AdmobCls.Interstitialload();
        switch (v.getId()) {
            case R.id.ibAddDuration:
                this.behavior.setState(3);
                return;
            case R.id.ibAddImages:
                this.flLoader.setVisibility(8);
                MyApplication.isBreak = true;
                this.application.isEditModeEnable = true;
                this.lastData.clear();
                this.lastData.addAll(this.arrayList);
                Intent intent = new Intent(this, ImageSelectionActivity.class);
                intent.setFlags(4194304);
                intent.putExtra(ImageSelectionActivity.EXTRA_FROM_PREVIEW, true);
                startActivityForResult(intent, 102);
                return;
            case R.id.ibAddMusic:
                this.flLoader.setVisibility(8);
                this.id = R.id.ibAddMusic;
                loadSongSelection();
                return;
            case R.id.ibEditMode:
                this.flLoader.setVisibility(8);
                this.application.isEditModeEnable = true;
                this.lockRunnable.pause();
                startActivityForResult(new Intent(this, ImageEditActivity.class).putExtra(ImageSelectionActivity.EXTRA_FROM_PREVIEW, true), 103);
                return;
            case R.id.video_clicker:
                if (this.lockRunnable.isPause()) {
                    this.lockRunnable.play();
                    return;
                } else {
                    this.lockRunnable.pause();
                    return;
                }
            default:
                return;
        }
    }

    protected void onPause() {
        super.onPause();
        this.lockRunnable.pause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        menu.removeItem(R.id.menu_clear);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.menu_done:
                this.id = R.id.menu_done;
                loadProgress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.application.isEditModeEnable = false;
        if (resultCode == -1) {
            int total;
            Intent intent;
            switch (requestCode) {
                case 101:
                    this.application.isFromSdCardAudio = true;
                    this.f21i = 0;
                    reinitMusic();
                    return;
                case 102:
                    if (isNeedRestart()) {
                        stopService(new Intent(getApplicationContext(), ImageCreatorService.class));
                        this.lockRunnable.stop();
                        this.seekBar.postDelayed(new C05853(), 1000);
                        total = (int) (((float) (this.arrayList.size() - 1)) * this.seconds);
                        this.arrayList = this.application.getSelectedImages();
                        this.seekBar.setMax((this.application.getSelectedImages().size() - 1) * 30);
                        this.tvEndTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(total / 60), Integer.valueOf(total % 60)}));
                        return;
                    }
                    if (ImageCreatorService.isImageComplate) {
                        MyApplication.isBreak = false;
                        this.application.videoImages.clear();
                        this.application.min_pos = Integer.MAX_VALUE;
                        intent = new Intent(getApplicationContext(), ImageCreatorService.class);
                        intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
                        startService(intent);
                        this.f21i = 0;
                        this.seekBar.setProgress(0);
                    }
                    total = (int) (((float) (this.arrayList.size() - 1)) * this.seconds);
                    this.arrayList = this.application.getSelectedImages();
                    this.seekBar.setMax((this.application.getSelectedImages().size() - 1) * 30);
                    this.tvEndTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(total / 60), Integer.valueOf(total % 60)}));
                    return;
                case 103:
                    this.lockRunnable.stop();
                    if (ImageCreatorService.isImageComplate || !MyApplication.isMyServiceRunning(this.application, ImageCreatorService.class)) {
                        MyApplication.isBreak = false;
                        this.application.videoImages.clear();
                        this.application.min_pos = Integer.MAX_VALUE;
                        intent = new Intent(getApplicationContext(), ImageCreatorService.class);
                        intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
                        startService(intent);
                    }
                    this.f21i = 0;
                    this.seekBar.setProgress(this.f21i);
                    this.arrayList = this.application.getSelectedImages();
                    total = (int) (((float) (this.arrayList.size() - 1)) * this.seconds);
                    this.seekBar.setMax((this.application.getSelectedImages().size() - 1) * 30);
                    this.tvEndTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(total / 60), Integer.valueOf(total % 60)}));
                    return;
                default:
                    return;
            }
        }
    }

    private boolean isNeedRestart() {
        if (this.lastData.size() > this.application.getSelectedImages().size()) {
            MyApplication.isBreak = true;
            return true;
        }
        int i = 0;
        while (i < this.lastData.size()) {
            if (((ImageData) this.lastData.get(i)).imagePath.equals(((ImageData) this.application.getSelectedImages().get(i)).imagePath)) {
                i++;
            } else {
                MyApplication.isBreak = true;
                return true;
            }
        }
        return false;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.f21i = progress;
        if (this.isFromTouch) {
            seekBar.setProgress(Math.min(progress, seekBar.getSecondaryProgress()));
            displayImage();
            seekMediaPlayer();
        }
    }

    private void seekMediaPlayer() {
        if (this.mPlayer != null) {
            try {
                this.mPlayer.seekTo(((int) (((((float) this.f21i) / 30.0f) * this.seconds) * 1000.0f)) % this.mPlayer.getDuration());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.isFromTouch = true;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        this.isFromTouch = false;
    }

    public void reset() {
        MyApplication.isBreak = false;
        this.application.videoImages.clear();
        this.handler.removeCallbacks(this.lockRunnable);
        this.lockRunnable.stop();
        Glide.get(this).clearMemory();
        new C05864().start();
        FileUtils.deleteTempDir();
        this.glide = Glide.with((FragmentActivity) this);
        this.flLoader.setVisibility(View.VISIBLE);
        setTheme();
    }

    public void onBackPressed() {
        if (this.behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            this.behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (this.llEdit.getVisibility() != View.VISIBLE) {
            this.llEdit.setVisibility(View.VISIBLE);
            this.application.isEditModeEnable = false;
        } else {
            onBackDialog();
        }
    }

    private void onBackDialog() {
        new Builder(this, R.style.Theme_MovieMaker_AlertDialog).setTitle((int) R.string.app_name).setMessage((CharSequence) "Are you sure ? \nYour video is not prepared yet!").setPositiveButton((CharSequence) "Go Back", new C05875()).setNegativeButton((CharSequence) "Stay here", null).create().show();
    }

    public void setTheme() {
        if (this.application.isFromSdCardAudio) {
            this.lockRunnable.play();
        } else {
            new C05906().start();
        }
    }

    private void startService() {
        MyApplication.isBreak = false;
        this.application.videoImages.clear();
        this.application.min_pos = Integer.MAX_VALUE;
        Intent intent = new Intent(getApplicationContext(), ImageCreatorService.class);
        intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
        startService(intent);
        this.seekBar.setProgress(0);
        this.f21i = 0;
        int total = (int) (((float) (this.arrayList.size() - 1)) * this.seconds);
        this.arrayList = this.application.getSelectedImages();
        this.seekBar.setMax((this.application.getSelectedImages().size() - 1) * 30);
        this.tvEndTime.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(total / 60), Integer.valueOf(total % 60)}));
        if (this.mPlayer != null) {
            this.mPlayer.seekTo(0);
        }
    }

    public void setFrame(int data) {
        this.frame = data;
        if (data == -1) {
            this.ivFrame.setImageDrawable(null);
        } else {
            this.ivFrame.setImageResource(data);
        }
        this.application.setFrame(data);
    }

    public int getFrame() {
        return this.application.getFrame();
    }

    private void loadSongSelection() {
        startActivityForResult(new Intent(this, SongEditActivity.class), 101);
    }

    @SuppressLint({"WrongConstant"})
    private void loadProgress() {
        this.handler.removeCallbacks(this.lockRunnable);
        startService(new Intent(this, CreateVideoService.class));
        Intent intent2 = new Intent(this.application, ProgressActivity.class);
        intent2.setFlags(268468224);
        startActivity(intent2);
        finish();
    }
}
