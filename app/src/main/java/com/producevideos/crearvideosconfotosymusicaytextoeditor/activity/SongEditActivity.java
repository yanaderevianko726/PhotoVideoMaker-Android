package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.MusicData;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FileUtils;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.soundfile.CheapSoundFile;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.soundfile.CheapSoundFile.ProgressListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.SongMetadataReader;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.MarkerView;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.MarkerView.MarkerListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.WaveformView;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.WaveformView.WaveformListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SongEditActivity extends AppCompatActivity implements MarkerListener, WaveformListener {
    private boolean isFromItemClick = false;
    boolean isPlaying = false;
    private MusicAdapter mAdapter;
    private String mArtist;
    private boolean mCanSeekAccurately;
    private float mDensity;
    private MarkerView mEndMarker;
    private int mEndPos;
    private TextView mEndText;
    private boolean mEndVisible;
    private String mExtension;
    private ImageButton mFfwdButton;
    private OnClickListener mFfwdListener = new C08181();
    private File mFile;
    private String mFilename = "record";
    private int mFlingVelocity;
    private Handler mHandler;
    private boolean mIsPlaying;
    private boolean mKeyDown;
    private int mLastDisplayedEndPos;
    private int mLastDisplayedStartPos;
    private boolean mLoadingKeepGoing;
    private long mLoadingLastUpdateTime;
    private int mMarkerBottomOffset;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMaxPos;
    private ArrayList<MusicData> mMusicDatas;
    private RecyclerView mMusicList;
    private int mOffset;
    private int mOffsetGoal;
    private ImageButton mPlayButton;
    private int mPlayEndMsec;
    private OnClickListener mPlayListener = new C08214();
    private int mPlayStartMsec;
    private int mPlayStartOffset;
    private MediaPlayer mPlayer;
    private ProgressDialog mProgressDialog;
    private String mRecordingFilename;
    private Uri mRecordingUri;
    private ImageButton mRewindButton;
    private OnClickListener mRewindListener = new C08225();
    private CheapSoundFile mSoundFile;
    private MarkerView mStartMarker;
    private int mStartPos;
    private TextView mStartText;
    private boolean mStartVisible;
    private TextWatcher mTextWatcher = new C08236();
    private Runnable mTimerRunnable = new C08247();
    private String mTitle;
    private boolean mTouchDragging;
    private int mTouchInitialEndPos;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private float mTouchStart;
    private long mWaveformTouchStartMsec;
    private WaveformView mWaveformView;
    private int mWidth;
    private MusicData selectedMusicData;
    private Toolbar toolbar;

    class C08181 implements OnClickListener {
        C08181() {
        }

        public void onClick(View sender) {
            if (SongEditActivity.this.mIsPlaying) {
                int newPos = SongEditActivity.this.mPlayer.getCurrentPosition() + 5000;
                if (newPos > SongEditActivity.this.mPlayEndMsec) {
                    newPos = SongEditActivity.this.mPlayEndMsec;
                }
                SongEditActivity.this.mPlayer.seekTo(newPos);
                return;
            }
            SongEditActivity.this.mEndMarker.requestFocus();
            SongEditActivity.this.markerFocus(SongEditActivity.this.mEndMarker);
        }
    }

    class C08192 implements OnClickListener {
        C08192() {
        }

        public void onClick(View sender) {
            if (SongEditActivity.this.mIsPlaying) {
                SongEditActivity.this.mEndPos = SongEditActivity.this.mWaveformView.millisecsToPixels(SongEditActivity.this.mPlayer.getCurrentPosition() + SongEditActivity.this.mPlayStartOffset);
                SongEditActivity.this.updateDisplay();
                SongEditActivity.this.handlePause();
            }
        }
    }

    class C08203 implements OnClickListener {
        C08203() {
        }

        public void onClick(View sender) {
            if (SongEditActivity.this.mIsPlaying) {
                SongEditActivity.this.mStartPos = SongEditActivity.this.mWaveformView.millisecsToPixels(SongEditActivity.this.mPlayer.getCurrentPosition() + SongEditActivity.this.mPlayStartOffset);
                SongEditActivity.this.updateDisplay();
            }
        }
    }

    class C08214 implements OnClickListener {
        C08214() {
        }

        public void onClick(View sender) {
            SongEditActivity.this.onPlay(SongEditActivity.this.mStartPos);
        }
    }

    class C08225 implements OnClickListener {
        C08225() {
        }

        public void onClick(View sender) {
            if (SongEditActivity.this.mIsPlaying) {
                int newPos = SongEditActivity.this.mPlayer.getCurrentPosition() - 5000;
                if (newPos < SongEditActivity.this.mPlayStartMsec) {
                    newPos = SongEditActivity.this.mPlayStartMsec;
                }
                SongEditActivity.this.mPlayer.seekTo(newPos);
                return;
            }
            SongEditActivity.this.mStartMarker.requestFocus();
            SongEditActivity.this.markerFocus(SongEditActivity.this.mStartMarker);
        }
    }

    class C08236 implements TextWatcher {
        C08236() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (SongEditActivity.this.mStartText.hasFocus()) {
                try {
                    SongEditActivity.this.mStartPos = SongEditActivity.this.mWaveformView.secondsToPixels(Double.parseDouble(SongEditActivity.this.mStartText.getText().toString()));
                    SongEditActivity.this.updateDisplay();
                } catch (NumberFormatException e) {
                }
            }
            if (SongEditActivity.this.mEndText.hasFocus()) {
                try {
                    SongEditActivity.this.mEndPos = SongEditActivity.this.mWaveformView.secondsToPixels(Double.parseDouble(SongEditActivity.this.mEndText.getText().toString()));
                    SongEditActivity.this.updateDisplay();
                } catch (NumberFormatException e2) {
                }
            }
        }
    }

    class C08247 implements Runnable {
        C08247() {
        }

        public void run() {
            if (!(SongEditActivity.this.mStartPos == SongEditActivity.this.mLastDisplayedStartPos || SongEditActivity.this.mStartText.hasFocus())) {
                SongEditActivity.this.mStartText.setText(SongEditActivity.this.formatTime(SongEditActivity.this.mStartPos));
                SongEditActivity.this.mLastDisplayedStartPos = SongEditActivity.this.mStartPos;
            }
            if (!(SongEditActivity.this.mEndPos == SongEditActivity.this.mLastDisplayedEndPos || SongEditActivity.this.mEndText.hasFocus())) {
                SongEditActivity.this.mEndText.setText(SongEditActivity.this.formatTime(SongEditActivity.this.mEndPos));
                SongEditActivity.this.mLastDisplayedEndPos = SongEditActivity.this.mEndPos;
            }
            SongEditActivity.this.mHandler.postDelayed(SongEditActivity.this.mTimerRunnable, 100);
        }
    }

    public class LoadMusics extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            this.pDialog = new ProgressDialog(SongEditActivity.this);
            this.pDialog.setTitle(SongEditActivity.this.getString(R.string.please_wait));
            this.pDialog.setMessage(SongEditActivity.this.getString(R.string.loading_music));
            this.pDialog.show();
        }

        protected Void doInBackground(Void... paramVarArgs) {
            SongEditActivity.this.mMusicDatas = SongEditActivity.this.getMusicFiles();
            if (SongEditActivity.this.mMusicDatas.size() > 0) {
                SongEditActivity.this.selectedMusicData = (MusicData) SongEditActivity.this.mMusicDatas.get(0);
                SongEditActivity.this.mFilename = SongEditActivity.this.selectedMusicData.getTrack_data();
            } else {
                SongEditActivity.this.mFilename = "record";
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            this.pDialog.dismiss();
            if (!SongEditActivity.this.mFilename.equals("record")) {
                SongEditActivity.this.setUpRecyclerView();
                SongEditActivity.this.loadFromFile();
                SongEditActivity.this.supportInvalidateOptionsMenu();
            } else if (SongEditActivity.this.mMusicDatas.size() > 0) {
                Toast.makeText(SongEditActivity.this.getApplicationContext(), SongEditActivity.this.getString(R.string.no_find_music), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MusicAdapter extends Adapter<MusicAdapter.Holder> {
        SparseBooleanArray booleanArray = new SparseBooleanArray();
        int mSelectedChoice = 0;
        private ArrayList<MusicData> musicDatas;

        public class Holder extends ViewHolder {
            public CheckBox radioMusicName;

            public Holder(View v) {
                super(v);
                this.radioMusicName = v.findViewById(R.id.radioMusicName);
            }
        }

        public MusicAdapter(ArrayList<MusicData> mMusicDatas) {
            this.musicDatas = mMusicDatas;
            this.booleanArray.put(0, true);
        }

        public Holder onCreateViewHolder(ViewGroup parent, int paramInt) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_items, parent, false));
        }

        public void onBindViewHolder(Holder holder, final int pos) {
            holder.radioMusicName.setText(this.musicDatas.get(pos).track_displayName);
            holder.radioMusicName.setChecked(this.booleanArray.get(pos, false));
            holder.radioMusicName.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    MusicAdapter.this.booleanArray.clear();
                    MusicAdapter.this.booleanArray.put(pos, true);
                    SongEditActivity.this.onPlay(-1);
                    MusicAdapter.this.playMusic(pos);
                    SongEditActivity.this.isFromItemClick = true;
                    MusicAdapter.this.notifyDataSetChanged();
                }
            });
        }

        public int getItemCount() {
            return this.musicDatas.size();
        }

        public void playMusic(int pos) {
            if (this.mSelectedChoice != pos) {
                SongEditActivity.this.selectedMusicData = (MusicData) SongEditActivity.this.mMusicDatas.get(pos);
                SongEditActivity.this.mFilename = SongEditActivity.this.selectedMusicData.getTrack_data();
                SongEditActivity.this.loadFromFile();
            }
            this.mSelectedChoice = pos;
        }
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.mRecordingFilename = null;
        this.mRecordingUri = null;
        this.mPlayer = null;
        this.mIsPlaying = false;
        this.mSoundFile = null;
        this.mKeyDown = false;
        this.mHandler = new Handler();
        loadGui();
        init();

        AdmobCls.Interstitialload();

        this.mHandler.postDelayed(this.mTimerRunnable, 100);
    }

    private void bindView() {
        this.mMusicList = findViewById(R.id.rvMusicList);
        this.toolbar = findViewById(R.id.toolbar);
    }

    private void init() {
        setSupportActionBar(this.toolbar);
        new LoadMusics().execute(new Void[0]);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpRecyclerView() {
        this.mAdapter = new MusicAdapter(this.mMusicDatas);
        this.mMusicList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mMusicList.setItemAnimator(new DefaultItemAnimator());
        this.mMusicList.setAdapter(this.mAdapter);
    }

    private ArrayList<MusicData> getMusicFiles() {
        ArrayList<MusicData> mMusicDatas = new ArrayList();
        Cursor mCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "title", "_data", "_display_name", "duration"}, "is_music != 0", null, "title ASC");

        int trackId = mCursor.getColumnIndex("_id");
        int trackTitle = mCursor.getColumnIndex("title");
        int trackDisplayName = mCursor.getColumnIndex("_display_name");
        int trackData = mCursor.getColumnIndex("_data");
        int trackDuration = mCursor.getColumnIndex("duration");

        while (mCursor.moveToNext()) {
            String path = mCursor.getString(trackData);
            if (isAudioFile(path)) {
                MusicData musicData = new MusicData();
                musicData.track_Id = mCursor.getLong(trackId);
                musicData.track_Title = mCursor.getString(trackTitle);
                musicData.track_data = path;
                musicData.track_duration = mCursor.getLong(trackDuration);
                musicData.track_displayName = mCursor.getString(trackDisplayName);
                mMusicDatas.add(musicData);
            }
        }
        return mMusicDatas;
    }

    private boolean isAudioFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return path.endsWith(".mp3");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (TextUtils.isEmpty(this.mFilename) || !this.mFilename.equals("record")) {
            getMenuInflater().inflate(R.menu.menu_selection, menu);
            menu.removeItem(R.id.menu_clear);
        } else {
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AdmobCls.Interstitialload();
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.menu_done:
                onSave();
                MyApplication.getInstance().setMusicData(this.selectedMusicData);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.stop();
        }
        this.mPlayer = null;
        if (this.mRecordingFilename != null) {
            try {
                if (!new File(this.mRecordingFilename).delete()) {
                    showFinalAlert(new Exception(), (int) R.string.delete_tmp_error);
                }
                getContentResolver().delete(this.mRecordingUri, null, null);
            } catch (Exception e) {
                showFinalAlert(e, (int) R.string.delete_tmp_error);
            }
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 62) {
            return super.onKeyDown(keyCode, event);
        }
        onPlay(this.mStartPos);
        return true;
    }

    public void waveformDraw() {
        this.mWidth = this.mWaveformView.getMeasuredWidth();
        if (this.mOffsetGoal != this.mOffset && !this.mKeyDown) {
            updateDisplay();
        } else if (this.mIsPlaying) {
            updateDisplay();
        } else if (this.mFlingVelocity != 0) {
            updateDisplay();
        }
    }

    public void waveformTouchStart(float x) {
        this.mTouchDragging = true;
        this.mTouchStart = x;
        this.mTouchInitialOffset = this.mOffset;
        this.mFlingVelocity = 0;
        this.mWaveformTouchStartMsec = System.currentTimeMillis();
    }

    public void waveformTouchMove(float x) {
        this.mOffset = trap((int) (((float) this.mTouchInitialOffset) + (this.mTouchStart - x)));
        updateDisplay();
    }

    public void waveformTouchEnd() {
        this.mTouchDragging = false;
        this.mOffsetGoal = this.mOffset;
        if (System.currentTimeMillis() - this.mWaveformTouchStartMsec < 300) {
            if (this.mIsPlaying) {
                int seekMsec = this.mWaveformView.pixelsToMillisecs((int) (this.mTouchStart + ((float) this.mOffset)));
                if (seekMsec < this.mPlayStartMsec || seekMsec >= this.mPlayEndMsec) {
                    handlePause();
                    return;
                } else {
                    this.mPlayer.seekTo(seekMsec - this.mPlayStartOffset);
                    return;
                }
            }
            onPlay((int) (this.mTouchStart + ((float) this.mOffset)));
        }
    }

    public void waveformFling(float vx) {
        this.mTouchDragging = false;
        this.mOffsetGoal = this.mOffset;
        this.mFlingVelocity = (int) (-vx);
        updateDisplay();
    }

    public void markerDraw() {
    }

    public void markerTouchStart(MarkerView marker, float x) {
        this.mTouchDragging = true;
        this.mTouchStart = x;
        this.mTouchInitialStartPos = this.mStartPos;
        this.mTouchInitialEndPos = this.mEndPos;
    }

    public void markerTouchMove(MarkerView marker, float x) {
        float delta = x - this.mTouchStart;
        if (marker == this.mStartMarker) {
            this.mStartPos = trap((int) (((float) this.mTouchInitialStartPos) + delta));
            this.mEndPos = trap((int) (((float) this.mTouchInitialEndPos) + delta));
        } else {
            this.mEndPos = trap((int) (((float) this.mTouchInitialEndPos) + delta));
            if (this.mEndPos < this.mStartPos) {
                this.mEndPos = this.mStartPos;
            }
        }
        updateDisplay();
    }

    public void markerTouchEnd(MarkerView marker) {
        this.mTouchDragging = false;
        if (marker == this.mStartMarker) {
            setOffsetGoalStart();
        } else {
            setOffsetGoalEnd();
        }
    }

    public void markerLeft(MarkerView marker, int velocity) {
        this.mKeyDown = true;
        if (marker == this.mStartMarker) {
            int saveStart = this.mStartPos;
            this.mStartPos = trap(this.mStartPos - velocity);
            this.mEndPos = trap(this.mEndPos - (saveStart - this.mStartPos));
            setOffsetGoalStart();
        }
        if (marker == this.mEndMarker) {
            if (this.mEndPos == this.mStartPos) {
                this.mStartPos = trap(this.mStartPos - velocity);
                this.mEndPos = this.mStartPos;
            } else {
                this.mEndPos = trap(this.mEndPos - velocity);
            }
            setOffsetGoalEnd();
        }
        updateDisplay();
    }

    public void markerRight(MarkerView marker, int velocity) {
        this.mKeyDown = true;
        if (marker == this.mStartMarker) {
            int saveStart = this.mStartPos;
            this.mStartPos += velocity;
            if (this.mStartPos > this.mMaxPos) {
                this.mStartPos = this.mMaxPos;
            }
            this.mEndPos += this.mStartPos - saveStart;
            if (this.mEndPos > this.mMaxPos) {
                this.mEndPos = this.mMaxPos;
            }
            setOffsetGoalStart();
        }
        if (marker == this.mEndMarker) {
            this.mEndPos += velocity;
            if (this.mEndPos > this.mMaxPos) {
                this.mEndPos = this.mMaxPos;
            }
            setOffsetGoalEnd();
        }
        updateDisplay();
    }

    public void markerEnter(MarkerView marker) {
    }

    public void markerKeyUp() {
        this.mKeyDown = false;
        updateDisplay();
    }

    public void markerFocus(MarkerView marker) {
        this.mKeyDown = false;
        if (marker == this.mStartMarker) {
            setOffsetGoalStartNoUpdate();
        } else {
            setOffsetGoalEndNoUpdate();
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                SongEditActivity.this.updateDisplay();
            }
        }, 100);
    }

    private void loadGui() {
        setContentView((int) R.layout.activity_add_music);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getWindow().setFlags(1024, 1024);
        bindView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
        this.mMarkerLeftInset = (int) (46.0f * this.mDensity);
        this.mMarkerRightInset = (int) (48.0f * this.mDensity);
        this.mMarkerTopOffset = (int) (this.mDensity * 10.0f);
        this.mMarkerBottomOffset = (int) (this.mDensity * 10.0f);
        this.mStartText = (TextView) findViewById(R.id.starttext);
        this.mStartText.addTextChangedListener(this.mTextWatcher);
        this.mEndText = (TextView) findViewById(R.id.endtext);
        this.mEndText.addTextChangedListener(this.mTextWatcher);
        this.mPlayButton = (ImageButton) findViewById(R.id.play);
        this.mPlayButton.setOnClickListener(this.mPlayListener);
        this.mRewindButton = (ImageButton) findViewById(R.id.rew);
        this.mRewindButton.setOnClickListener(this.mRewindListener);
        this.mFfwdButton = (ImageButton) findViewById(R.id.ffwd);
        this.mFfwdButton.setOnClickListener(this.mFfwdListener);
        enableDisableButtons();
        this.mWaveformView = (WaveformView) findViewById(R.id.waveform);
        this.mWaveformView.setListener(this);
        this.mMaxPos = 0;
        this.mLastDisplayedStartPos = -1;
        this.mLastDisplayedEndPos = -1;
        if (this.mSoundFile != null) {
            this.mWaveformView.setSoundFile(this.mSoundFile);
            this.mWaveformView.recomputeHeights(this.mDensity);
            this.mMaxPos = this.mWaveformView.maxPos();
        }
        this.mStartMarker = (MarkerView) findViewById(R.id.startmarker);
        this.mStartMarker.setListener(this);
        this.mStartMarker.setAlpha(255);
        this.mStartMarker.setFocusable(true);
        this.mStartMarker.setFocusableInTouchMode(true);
        this.mStartVisible = true;
        this.mEndMarker = (MarkerView) findViewById(R.id.endmarker);
        this.mEndMarker.setListener(this);
        this.mEndMarker.setAlpha(255);
        this.mEndMarker.setFocusable(true);
        this.mEndMarker.setFocusableInTouchMode(true);
        this.mEndVisible = true;
        updateDisplay();
    }

    private void loadFromFile() {
        this.mFile = new File(this.mFilename);
        this.mExtension = getExtensionFromFilename(this.mFilename);
        SongMetadataReader metadataReader = new SongMetadataReader(this, this.mFilename);
        this.mTitle = metadataReader.mTitle;
        this.mArtist = metadataReader.mArtist;
        String titleLabel = this.mTitle;
        if (this.mArtist != null && this.mArtist.length() > 0) {
            titleLabel = new StringBuilder(String.valueOf(titleLabel)).append(" - ").append(this.mArtist).toString();
        }
        setTitle(titleLabel);
        this.mLoadingLastUpdateTime = System.currentTimeMillis();
        this.mLoadingKeepGoing = true;
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setTitle(R.string.progress_dialog_loading);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                SongEditActivity.this.mLoadingKeepGoing = false;
            }
        });
        this.mProgressDialog.show();
        final ProgressListener listener = new ProgressListener() {
            public boolean reportProgress(double fractionComplete) {
                long now = System.currentTimeMillis();
                if (now - SongEditActivity.this.mLoadingLastUpdateTime > 100) {
                    SongEditActivity.this.mProgressDialog.setProgress((int) (((double) SongEditActivity.this.mProgressDialog.getMax()) * fractionComplete));
                    SongEditActivity.this.mLoadingLastUpdateTime = now;
                }
                return SongEditActivity.this.mLoadingKeepGoing;
            }
        };
        this.mCanSeekAccurately = false;
        new Thread() {
            public void run() {
                SongEditActivity.this.mCanSeekAccurately = SeekTest.CanSeekAccurately(SongEditActivity.this.getPreferences(0));
                System.out.println("Seek test done, creating media player.");
                try {
                    MediaPlayer player = new MediaPlayer();
                    player.setDataSource(SongEditActivity.this.mFile.getAbsolutePath());
                    player.setAudioStreamType(3);
                    player.prepare();
                    SongEditActivity.this.mPlayer = player;
                } catch (final IOException e) {
                    SongEditActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            SongEditActivity.this.handleFatalError(SongEditActivity.this.getString(R.string.read_error), SongEditActivity.this.getResources().getText(R.string.read_error), e);
                        }
                    });
                }
            }
        }.start();
        new Thread() {

            class C08142 implements Runnable {
                C08142() {
                }

                public void run() {
                    SongEditActivity.this.finishOpeningSoundFile();
                }
            }

            public void run() {
                try {
                    SongEditActivity.this.mSoundFile = CheapSoundFile.create(SongEditActivity.this.mFile.getAbsolutePath(), listener);
                    if (SongEditActivity.this.mSoundFile == null) {
                        String err;
                        SongEditActivity.this.mProgressDialog.dismiss();
                        String[] components = SongEditActivity.this.mFile.getName().toLowerCase().split("\\.");
                        if (components.length < 2) {
                            err = SongEditActivity.this.getResources().getString(R.string.no_extension_error);
                        } else {
                            err = new StringBuilder(String.valueOf(SongEditActivity.this.getResources().getString(R.string.bad_extension_error))).append(" ").append(components[components.length - 1]).toString();
                        }
                        final String finalErr = err;
                        SongEditActivity.this.mHandler.post(new Runnable() {
                            public void run() {
                                SongEditActivity.this.handleFatalError(SongEditActivity.this.getString(R.string.unsupported_extension), finalErr, new Exception());
                            }
                        });
                        return;
                    }
                    SongEditActivity.this.mProgressDialog.dismiss();
                    if (SongEditActivity.this.mLoadingKeepGoing) {
                        SongEditActivity.this.mHandler.post(new C08142());
                    } else {
                        SongEditActivity.this.finish();
                    }
                } catch (final Exception e) {
                    SongEditActivity.this.mProgressDialog.dismiss();
                    e.printStackTrace();
                    SongEditActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            SongEditActivity.this.handleFatalError(SongEditActivity.this.getString(R.string.read_error), SongEditActivity.this.getResources().getText(R.string.read_error), e);
                        }
                    });
                }
            }
        }.start();
    }

    private void finishOpeningSoundFile() {
        this.mWaveformView.setSoundFile(this.mSoundFile);
        this.mWaveformView.recomputeHeights(this.mDensity);
        this.mMaxPos = this.mWaveformView.maxPos();
        this.mLastDisplayedStartPos = -1;
        this.mLastDisplayedEndPos = -1;
        this.mTouchDragging = false;
        this.mOffset = 0;
        this.mOffsetGoal = 0;
        this.mFlingVelocity = 0;
        resetPositions();
        if (this.mEndPos > this.mMaxPos) {
            this.mEndPos = this.mMaxPos;
        }
        updateDisplay();
        if (this.isFromItemClick) {
            onPlay(this.mStartPos);
        }
    }

    private synchronized void updateDisplay() {
        if (this.mIsPlaying) {
            int now = this.mPlayer.getCurrentPosition() + this.mPlayStartOffset;
            int frames = this.mWaveformView.millisecsToPixels(now);
            this.mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - (this.mWidth / 2));
            if (now >= this.mPlayEndMsec) {
                handlePause();
            }
        }
        if (!this.mTouchDragging) {
            int offsetDelta;
            if (this.mFlingVelocity != 0) {
                offsetDelta = this.mFlingVelocity / 30;
                if (this.mFlingVelocity > 80) {
                    this.mFlingVelocity -= 80;
                } else if (this.mFlingVelocity < -80) {
                    this.mFlingVelocity += 80;
                } else {
                    this.mFlingVelocity = 0;
                }
                this.mOffset += offsetDelta;
                if (this.mOffset + (this.mWidth / 2) > this.mMaxPos) {
                    this.mOffset = this.mMaxPos - (this.mWidth / 2);
                    this.mFlingVelocity = 0;
                }
                if (this.mOffset < 0) {
                    this.mOffset = 0;
                    this.mFlingVelocity = 0;
                }
                this.mOffsetGoal = this.mOffset;
            } else {
                offsetDelta = this.mOffsetGoal - this.mOffset;
                if (offsetDelta > 10) {
                    offsetDelta /= 10;
                } else if (offsetDelta > 0) {
                    offsetDelta = 1;
                } else if (offsetDelta < -10) {
                    offsetDelta /= 10;
                } else if (offsetDelta < 0) {
                    offsetDelta = -1;
                } else {
                    offsetDelta = 0;
                }
                this.mOffset += offsetDelta;
            }
        }
        this.mWaveformView.setParameters(this.mStartPos, this.mEndPos, this.mOffset);
        this.mWaveformView.invalidate();
        this.mStartMarker.setContentDescription(getResources().getText(R.string.start_marker) + " " + formatTime(this.mStartPos));
        this.mEndMarker.setContentDescription(getResources().getText(R.string.end_marker) + " " + formatTime(this.mEndPos));
        int startX = (this.mStartPos - this.mOffset) - this.mMarkerLeftInset;
        if (this.mStartMarker.getWidth() + startX < 0) {
            if (this.mStartVisible) {
                this.mStartMarker.setAlpha(0);
                this.mStartVisible = false;
            }
            startX = 0;
        } else if (!this.mStartVisible) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SongEditActivity.this.mStartVisible = true;
                    SongEditActivity.this.mStartMarker.setAlpha(255);
                }
            }, 0);
        }
        int endX = ((this.mEndPos - this.mOffset) - this.mEndMarker.getWidth()) + this.mMarkerRightInset;
        if (this.mEndMarker.getWidth() + endX < 0) {
            if (this.mEndVisible) {
                this.mEndMarker.setAlpha(0);
                this.mEndVisible = false;
            }
            endX = 0;
        } else if (!this.mEndVisible) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SongEditActivity.this.mEndVisible = true;
                    SongEditActivity.this.mEndMarker.setAlpha(255);
                }
            }, 0);
        }
        this.mStartMarker.setLayoutParams(new LayoutParams(-2, -2, startX, this.mMarkerTopOffset));
        this.mEndMarker.setLayoutParams(new LayoutParams(-2, -2, endX, (this.mWaveformView.getMeasuredHeight() - this.mEndMarker.getHeight()) - this.mMarkerBottomOffset));
    }

    @SuppressLint({"ResourceType"})
    private void enableDisableButtons() {
        if (this.mIsPlaying) {
            this.mPlayButton.setImageResource(17301539);
            this.mPlayButton.setContentDescription(getResources().getText(R.string.stop));
            return;
        }
        this.mPlayButton.setImageResource(17301540);
        this.mPlayButton.setContentDescription(getResources().getText(R.string.play));
    }

    private void resetPositions() {
        this.mStartPos = this.mWaveformView.secondsToPixels(0.0d);
        this.mEndPos = this.mWaveformView.secondsToPixels((double) this.mMaxPos);
    }

    private int trap(int pos) {
        if (pos < 0) {
            return 0;
        }
        if (pos > this.mMaxPos) {
            return this.mMaxPos;
        }
        return pos;
    }

    private void setOffsetGoalStart() {
        setOffsetGoal(this.mStartPos - (this.mWidth / 2));
    }

    private void setOffsetGoalStartNoUpdate() {
        setOffsetGoalNoUpdate(this.mStartPos - (this.mWidth / 2));
    }

    private void setOffsetGoalEnd() {
        setOffsetGoal(this.mEndPos - (this.mWidth / 2));
    }

    private void setOffsetGoalEndNoUpdate() {
        setOffsetGoalNoUpdate(this.mEndPos - (this.mWidth / 2));
    }

    private void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateDisplay();
    }

    private void setOffsetGoalNoUpdate(int offset) {
        if (!this.mTouchDragging) {
            this.mOffsetGoal = offset;
            if (this.mOffsetGoal + (this.mWidth / 2) > this.mMaxPos) {
                this.mOffsetGoal = this.mMaxPos - (this.mWidth / 2);
            }
            if (this.mOffsetGoal < 0) {
                this.mOffsetGoal = 0;
            }
        }
    }

    private String formatTime(int pixels) {
        if (this.mWaveformView == null || !this.mWaveformView.isInitialized()) {
            return "";
        }
        return formatDecimal(this.mWaveformView.pixelsToSeconds(pixels));
    }

    private String formatDecimal(double x) {
        int xWhole = (int) x;
        int xFrac = (int) ((100.0d * (x - ((double) xWhole))) + 0.5d);
        if (xFrac >= 100) {
            xWhole++;
            xFrac -= 100;
            if (xFrac < 10) {
                xFrac *= 10;
            }
        }
        if (xFrac < 10) {
            return new StringBuilder(String.valueOf(xWhole)).append(".0").append(xFrac).toString();
        }
        return new StringBuilder(String.valueOf(xWhole)).append(".").append(xFrac).toString();
    }

    private synchronized void handlePause() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        }
        this.mWaveformView.setPlayback(-1);
        this.mIsPlaying = false;
        enableDisableButtons();
    }

    private synchronized void onPlay(int startPosition) {
        if (this.mIsPlaying) {
            handlePause();
        } else if (!(this.mPlayer == null || startPosition == -1)) {
            try {
                this.mPlayStartMsec = this.mWaveformView.pixelsToMillisecs(startPosition);
                if (startPosition < this.mStartPos) {
                    this.mPlayEndMsec = this.mWaveformView.pixelsToMillisecs(this.mStartPos);
                } else if (startPosition > this.mEndPos) {
                    this.mPlayEndMsec = this.mWaveformView.pixelsToMillisecs(this.mMaxPos);
                } else {
                    this.mPlayEndMsec = this.mWaveformView.pixelsToMillisecs(this.mEndPos);
                }
                this.mPlayStartOffset = 0;
                int startFrame = this.mWaveformView.secondsToFrames(((double) this.mPlayStartMsec) * 0.001d);
                int endFrame = this.mWaveformView.secondsToFrames(((double) this.mPlayEndMsec) * 0.001d);
                int startByte = this.mSoundFile.getSeekableFrameOffset(startFrame);
                int endByte = this.mSoundFile.getSeekableFrameOffset(endFrame);
                if (this.mCanSeekAccurately && startByte >= 0 && endByte >= 0) {
                    try {
                        this.mPlayer.reset();
                        this.mPlayer.setAudioStreamType(3);
                        this.mPlayer.setDataSource(new FileInputStream(this.mFile.getAbsolutePath()).getFD(), (long) startByte, (long) (endByte - startByte));
                        this.mPlayer.prepare();
                        this.mPlayStartOffset = this.mPlayStartMsec;
                    } catch (Exception e) {
                        System.out.println("Exception trying to play file subset");
                        this.mPlayer.reset();
                        this.mPlayer.setAudioStreamType(3);
                        this.mPlayer.setDataSource(this.mFile.getAbsolutePath());
                        this.mPlayer.prepare();
                        this.mPlayStartOffset = 0;
                    }
                }
                this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
                    public synchronized void onCompletion(MediaPlayer arg0) {
                        SongEditActivity.this.handlePause();
                    }
                });
                this.mIsPlaying = true;
                if (this.mPlayStartOffset == 0) {
                    this.mPlayer.seekTo(this.mPlayStartMsec);
                }
                this.mPlayer.start();
                updateDisplay();
                enableDisableButtons();
            } catch (Exception e2) {
                showFinalAlert(e2, (int) R.string.play_error);
            }
        }
    }

    private void showFinalAlert(Exception e, CharSequence message) {
        CharSequence title;
        if (e != null) {
            title = getResources().getText(R.string.alert_title_failure);
            setResult(0, new Intent());
        } else {
            title = getResources().getText(R.string.alert_title_success);
        }
        new Builder(this, R.style.Theme_MovieMaker_AlertDialog).setTitle(title).setMessage(message).setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SongEditActivity.this.finish();
            }
        }).setCancelable(false).show();
    }

    private void showFinalAlert(Exception e, int messageResourceId) {
        showFinalAlert(e, getResources().getText(messageResourceId));
    }

    private String makeRingtoneFilename(CharSequence title, String extension) {
        FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
        File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, title + extension);
        if (tempFile.exists()) {
            FileUtils.deleteFile(tempFile);
        }
        return tempFile.getAbsolutePath();
    }

    private void saveRingtone(CharSequence title) {
        final String outPath = makeRingtoneFilename(title, this.mExtension);
        if (outPath == null) {
            showFinalAlert(new Exception(), (int) R.string.no_unique_filename);
            return;
        }
        double startTime = this.mWaveformView.pixelsToSeconds(this.mStartPos);
        double endTime = this.mWaveformView.pixelsToSeconds(this.mEndPos);
        final int startFrame = this.mWaveformView.secondsToFrames(startTime);
        final int endFrame = this.mWaveformView.secondsToFrames(endTime);
        final int duration = (int) ((endTime - startTime) + 0.5d);
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setTitle(R.string.progress_dialog_saving);
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
        final CharSequence charSequence = title;
        new Thread() {

            class C11581 implements ProgressListener {
                C11581() {
                }

                public boolean reportProgress(double frac) {
                    return true;
                }
            }

            public void run() {
                final File outFile = new File(outPath);
                try {
                    SongEditActivity.this.mSoundFile.WriteFile(outFile, startFrame, endFrame - startFrame);
                    CheapSoundFile.create(outPath, new C11581());
                    SongEditActivity.this.mProgressDialog.dismiss();
                    final String str = outPath;
                    final int i = duration;
                    SongEditActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            SongEditActivity.this.afterSavingRingtone(charSequence, str, outFile, i);
                        }
                    });
                } catch (Exception e) {
                    CharSequence errorMessage;
                    Exception e2 = e;
                    SongEditActivity.this.mProgressDialog.dismiss();
                    if (e2.getMessage().equals("No space left on device")) {
                        errorMessage = SongEditActivity.this.getResources().getText(R.string.no_space_error);
                        e2 = null;
                    } else {
                        errorMessage = SongEditActivity.this.getResources().getText(R.string.write_error);
                    }
                    final CharSequence finalErrorMessage = errorMessage;
                    final Exception finalException = e2;
                    SongEditActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            SongEditActivity.this.handleFatalError("WriteError", finalErrorMessage, finalException);
                        }
                    });
                }
            }
        }.start();
    }

    private void afterSavingRingtone(CharSequence title, String outPath, File outFile, int duration) {
        if (outFile.length() <= 512) {
            outFile.delete();
            new Builder(this).setTitle(R.string.alert_title_failure).setMessage(R.string.too_small_error).setPositiveButton(R.string.alert_ok_button, null).setCancelable(false).show();
            return;
        }
        long fileSize = outFile.length();
        String artist = (String) getResources().getText(R.string.artist_name);
        ContentValues values = new ContentValues();
        values.put("_data", outPath);
        values.put("title", title.toString());
        values.put("_size", Long.valueOf(fileSize));
        values.put("mime_type", "audio/mpeg");
        values.put("artist", artist);
        values.put("duration", Integer.valueOf(duration));
        values.put("is_music", Boolean.valueOf(true));
        Log.e("audio", "duaration is " + duration);
        setResult(-1, new Intent().setData(getContentResolver().insert(Media.getContentUriForPath(outPath), values)));
        this.selectedMusicData.track_data = outPath;
        this.selectedMusicData.track_duration = (long) (duration * 1000);
        MyApplication.getInstance().setMusicData(this.selectedMusicData);
        finish();
    }

    private void handleFatalError(CharSequence errorInternalName, CharSequence errorString, Exception exception) {

    }

    private void onSave() {
        if (this.mIsPlaying) {
            handlePause();
        }
        saveRingtone("temp");
    }

    private String getStackTrace(Exception e) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(stream, true));
        return stream.toString();
    }

    private String getExtensionFromFilename(String filename) {
        return filename.substring(filename.lastIndexOf(46), filename.length());
    }

    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
        if (this.isPlaying) {
            this.mPlayer.release();
        }
    }
}
