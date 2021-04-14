package com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.activity.PreviewActivity;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.FileUtils;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.service.ImageCreatorService;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.themes.THEMES;

import java.util.ArrayList;
import java.util.Arrays;

public class MoviewThemeAdapter extends Adapter<MoviewThemeAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private LayoutInflater inflater;
    private ArrayList<THEMES> list;
    private PreviewActivity previewActivity;

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

    public MoviewThemeAdapter(PreviewActivity previewActivity) {
        this.previewActivity = previewActivity;
        this.list = new ArrayList(Arrays.asList(THEMES.values()));
        this.inflater = LayoutInflater.from(previewActivity);
    }

    public Holder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new Holder(this.inflater.inflate(R.layout.movie_theme_items, paramViewGroup, false));
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        THEMES themes = (THEMES) this.list.get(pos);
        Glide.with(this.application).load(Integer.valueOf(themes.getThemeDrawable())).into(holder.ivThumb);
        holder.tvThemeName.setText(themes.toString());
        holder.cbSelect.setChecked(themes == this.application.selectedTheme);
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MoviewThemeAdapter.this.list.get(pos) != MoviewThemeAdapter.this.application.selectedTheme) {
                    MoviewThemeAdapter.this.deleteThemeDir(MoviewThemeAdapter.this.application.selectedTheme.toString());
                    MoviewThemeAdapter.this.application.videoImages.clear();
                    MoviewThemeAdapter.this.application.selectedTheme = (THEMES) MoviewThemeAdapter.this.list.get(pos);
                    MoviewThemeAdapter.this.application.setCurrentTheme(MoviewThemeAdapter.this.application.selectedTheme.toString());
                    MoviewThemeAdapter.this.previewActivity.reset();
                    Intent intent = new Intent(MoviewThemeAdapter.this.application, ImageCreatorService.class);
                    intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, MoviewThemeAdapter.this.application.getCurrentTheme());
                    MoviewThemeAdapter.this.application.startService(intent);
                    MoviewThemeAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }

    private void deleteThemeDir(final String dir) {
        new Thread() {
            public void run() {
                FileUtils.deleteThemeDir(dir);
            }
        }.start();
    }

    public ArrayList<THEMES> getList() {
        return this.list;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
