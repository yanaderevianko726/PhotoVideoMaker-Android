package com.producevideos.crearvideosconfotosymusicaytextoeditor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.AlbumAdapterById;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.ImageByAlbumAdapter;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.OnItemClickListner;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters.SelectedImageAdapter;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.AdmobCls;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.EmptyRecyclerView;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.ExpandIconView;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.VerticalSlidingPanel;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.view.VerticalSlidingPanel.PanelSlideListener;

public class ImageSelectionActivity extends AppCompatActivity implements PanelSlideListener {
    public static final String EXTRA_FROM_PREVIEW = "extra_from_preview";
    private AlbumAdapterById albumAdapter;
    private ImageByAlbumAdapter albumImagesAdapter;
    private MyApplication application;
    private Button btnClear;
    private ExpandIconView expandIcon;
    boolean fbshow = true;
    Handler handler = new Handler();
    public boolean isFromPreview = false;
    boolean isPause = false;

    private VerticalSlidingPanel panel;
    private View parent;

    private RecyclerView rvAlbum;
    private RecyclerView rvAlbumImages;
    private EmptyRecyclerView rvSelectedImage;
    private SelectedImageAdapter selectedImageAdapter;
    private Toolbar toolbar;
    private TextView tvImageCount;

    class C05822 implements OnClickListener {
        C05822() {
        }

        public void onClick(View v) {
            ImageSelectionActivity.this.clearData();
        }
    }

    class C10173 implements OnItemClickListner<Object> {
        C10173() {
        }

        public void onItemClick(View view, Object item) {
            ImageSelectionActivity.this.albumImagesAdapter.notifyDataSetChanged();
        }
    }

    class C10184 implements OnItemClickListner<Object> {
        C10184() {
        }

        public void onItemClick(View view, Object item) {
            ImageSelectionActivity.this.tvImageCount.setText(String.valueOf(ImageSelectionActivity.this.application.getSelectedImages().size()));
            ImageSelectionActivity.this.selectedImageAdapter.notifyDataSetChanged();
        }
    }

    class C10195 implements OnItemClickListner<Object> {
        C10195() {
        }

        public void onItemClick(View view, Object item) {
            ImageSelectionActivity.this.tvImageCount.setText(String.valueOf(ImageSelectionActivity.this.application.getSelectedImages().size()));
            ImageSelectionActivity.this.albumImagesAdapter.notifyDataSetChanged();
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
        setContentView((int) R.layout.image_select_activity);
        getWindow().setFlags(1024, 1024);

        AdmobCls.Interstitialload();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.application = MyApplication.getInstance();
        this.isFromPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);
        bindView();
        init();
        addListner();
    }

    private void init() {
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.albumAdapter = new AlbumAdapterById(this);
        this.albumImagesAdapter = new ImageByAlbumAdapter(this);
        this.selectedImageAdapter = new SelectedImageAdapter(this);
        this.rvAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.rvAlbum.setItemAnimator(new DefaultItemAnimator());
        this.rvAlbum.setAdapter(this.albumAdapter);
        this.rvAlbumImages.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        this.rvAlbumImages.setItemAnimator(new DefaultItemAnimator());
        this.rvAlbumImages.setAdapter(this.albumImagesAdapter);
        this.rvSelectedImage.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        this.rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
        this.rvSelectedImage.setAdapter(this.selectedImageAdapter);
        this.rvSelectedImage.setEmptyView(findViewById(R.id.list_empty));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.tvImageCount.setText(String.valueOf(this.application.getSelectedImages().size()));
    }

    private void bindView() {
        this.tvImageCount = findViewById(R.id.tvImageCount);
        this.expandIcon = findViewById(R.id.settings_drag_arrow);
        this.rvAlbum = findViewById(R.id.rvAlbum);
        this.rvAlbumImages = findViewById(R.id.rvImageAlbum);
        this.rvSelectedImage = findViewById(R.id.rvSelectedImagesList);
        this.panel = findViewById(R.id.overview_panel);
        this.panel.setEnableDragViewTouchEvents(true);
        this.panel.setDragView(findViewById(R.id.settings_pane_header));
        this.panel.setPanelSlideListener(this);
        this.parent = findViewById(R.id.default_home_screen_panel);
        this.toolbar = findViewById(R.id.toolbar);
        this.btnClear = findViewById(R.id.btnClear);
    }

    private void addListner() {

        this.btnClear.setOnClickListener(new C05822());
        this.albumAdapter.setOnItemClickListner(new C10173());
        this.albumImagesAdapter.setOnItemClickListner(new C10184());
        this.selectedImageAdapter.setOnItemClickListner(new C10195());
    }

    protected void onResume() {
        super.onResume();

        if (this.isPause) {
            this.isPause = false;
            this.tvImageCount.setText(String.valueOf(this.application.getSelectedImages().size()));
            this.albumImagesAdapter.notifyDataSetChanged();
            this.selectedImageAdapter.notifyDataSetChanged();
        }
    }

    protected void onPause() {
        super.onPause();
        this.isPause = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        if (this.isFromPreview) {
            menu.removeItem(R.id.menu_clear);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AdmobCls.Interstitialload();
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.menu_clear:
                clearData();
                break;
            case R.id.menu_done:
                if (this.application.getSelectedImages().size() <= 2) {
                    Toast.makeText(this, getString(R.string.select_more_than_two), Toast.LENGTH_SHORT).show();
                    break;
                } else if (!this.isFromPreview) {
                    loadImageEdit();
                    break;
                } else {
                    setResult(-1);
                    finish();
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImageEdit() {
        startActivity(new Intent(ImageSelectionActivity.this, ImageEditActivity.class));
    }

    public void onPanelSlide(View panel, float slideOffset) {
        if (this.expandIcon != null) {
            this.expandIcon.setFraction(slideOffset, false);
        }
        if (slideOffset >= 0.005f) {
            if (this.parent != null && this.parent.getVisibility() != View.VISIBLE) {
                this.parent.setVisibility(View.VISIBLE);
            }
        } else if (this.parent != null && this.parent.getVisibility() == View.VISIBLE) {
            this.parent.setVisibility(View.GONE);
        }
    }

    public void onPanelCollapsed(View panel) {
        if (this.parent != null) {
            this.parent.setVisibility(View.VISIBLE);
        }
        this.selectedImageAdapter.isExpanded = false;
        this.selectedImageAdapter.notifyDataSetChanged();
    }

    public void onPanelExpanded(View panel) {
        if (this.parent != null) {
            this.parent.setVisibility(View.GONE);
        }
        this.selectedImageAdapter.isExpanded = true;
        this.selectedImageAdapter.notifyDataSetChanged();
    }

    public void onPanelAnchored(View panel) {
    }

    public void onPanelShown(View panel) {
    }

    public void onBackPressed() {
        if (this.panel.isExpanded()) {
            this.panel.collapsePane();
        } else if (this.isFromPreview) {
            setResult(-1);
            finish();
        } else {
            this.application.videoImages.clear();
            this.application.clearAllSelection();
            super.onBackPressed();
        }
    }

    private void clearData() {
        for (int i = this.application.getSelectedImages().size() - 1; i >= 0; i--) {
            this.application.removeSelectedImage(i);
        }
        this.tvImageCount.setText("0");
        this.selectedImageAdapter.notifyDataSetChanged();
        this.albumImagesAdapter.notifyDataSetChanged();
    }
}
