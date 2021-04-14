package com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters;

import android.content.Context;
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
import com.bumptech.glide.RequestManager;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.ImageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumAdapterById extends Adapter<AlbumAdapterById.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private ArrayList<String> folderId = new ArrayList(this.application.getAllAlbum().keySet());
    RequestManager glide;
    private LayoutInflater inflater;

    class C06281 implements Comparator<String> {
        C06281() {
        }

        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }

    public class Holder extends ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        ImageView imageView;
        View parent;
        TextView textView;

        public Holder(View v) {
            super(v);
            this.parent = v;
            this.cbSelect = (CheckBox) v.findViewById(R.id.cbSelect);
            this.imageView = (ImageView) v.findViewById(R.id.imageView1);
            this.textView = (TextView) v.findViewById(R.id.textView1);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (AlbumAdapterById.this.clickListner != null) {
                AlbumAdapterById.this.clickListner.onItemClick(view, item);
            }
        }
    }

    public AlbumAdapterById(Context activity) {
        this.glide = Glide.with(activity);
        Collections.sort(this.folderId, new C06281());
        this.application.setSelectedFolderId((String) this.folderId.get(0));
        this.inflater = LayoutInflater.from(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.folderId.size();
    }

    public String getItem(int pos) {
        return (String) this.folderId.get(pos);
    }

    public void onBindViewHolder(Holder holder, int pos) {
        final String currentFolderId = getItem(pos);
        final ImageData data = (ImageData) this.application.getImageByAlbum(currentFolderId).get(0);
        holder.textView.setSelected(true);
        holder.textView.setText(data.folderName);
        this.glide.load(data.imagePath).into(holder.imageView);
        holder.cbSelect.setChecked(currentFolderId.equals(this.application.getSelectedFolderId()));
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AlbumAdapterById.this.application.setSelectedFolderId(currentFolderId);
                if (AlbumAdapterById.this.clickListner != null) {
                    AlbumAdapterById.this.clickListner.onItemClick(v, data);
                }
                AlbumAdapterById.this.notifyDataSetChanged();
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items, parent, false));
    }
}
