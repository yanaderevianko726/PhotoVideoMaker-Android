package com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.activity.PreviewActivity;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FileUtils;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.util.ScalingUtilities;

import java.io.FileOutputStream;

public class FrameAdapter extends Adapter<FrameAdapter.Holder> {
    PreviewActivity activity;
    private MyApplication application;
    private OnItemClickListner<Object> clickListner;
    private int[] drawable = new int[]{-1, R.drawable.f_1, R.drawable.f_2, R.drawable.f_3, R.drawable.f_4, R.drawable.f_5, R.drawable.f_6, R.drawable.f_7, R.drawable.f_8, R.drawable.f_9, R.drawable.f_10, R.drawable.f_11, R.drawable.f_12, R.drawable.f_13, R.drawable.f_14, R.drawable.f_15, R.drawable.f_17, R.drawable.f_18};
    private RequestManager glide;
    private LayoutInflater inflater;
    int lastPos = 0;

    public class Holder extends ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        private ImageView ivThumb;
        private View mainView;
        private TextView tvThemeName;

        public Holder(View v) {
            super(v);
            this.cbSelect = (CheckBox) v.findViewById(R.id.cbSelect);
            this.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            this.tvThemeName = (TextView) v.findViewById(R.id.tvThemeName);
            this.clickableView = v.findViewById(R.id.clickableView);
            this.mainView = v;
        }
    }

    public FrameAdapter(PreviewActivity activity) {
        this.activity = activity;
        this.application = MyApplication.getInstance();
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with((FragmentActivity) activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.drawable.length;
    }

    public int getItem(int pos) {
        return this.drawable[pos];
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        final int themes = getItem(pos);
        holder.ivThumb.setScaleType(ScaleType.FIT_XY);
        Glide.with(this.application).load(Integer.valueOf(themes)).into(holder.ivThumb);
        holder.cbSelect.setChecked(themes == this.activity.getFrame());
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (themes != FrameAdapter.this.activity.getFrame()) {
                    FrameAdapter.this.activity.setFrame(themes);
                    if (themes != -1) {
                        FrameAdapter.this.notifyItemChanged(FrameAdapter.this.lastPos);
                        FrameAdapter.this.notifyItemChanged(pos);
                        FrameAdapter.this.lastPos = pos;
                        FileUtils.deleteFile(FileUtils.frameFile);
                        try {
                            Bitmap bm = ScalingUtilities.scaleCenterCrop(BitmapFactory.decodeResource(FrameAdapter.this.activity.getResources(), themes), MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                            FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                            bm.compress(CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                            bm.recycle();
                            System.gc();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.movie_theme_items, parent, false));
    }
}
