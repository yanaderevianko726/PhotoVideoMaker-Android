package com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class ImageEditAdapter extends Adapter<ImageEditAdapter.Holder> {
    final int TYPE_BLANK = 1;
    final int TYPE_IMAGE = 0;
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;

    public class Holder extends ViewHolder {
        private ImageView ivRemove;
        private ImageView ivThumb;
        View parent;
        private TextView textNumber;

        public Holder(View v) {
            super(v);
            this.parent = v;
            this.textNumber = (TextView) v.findViewById(R.id.textNumber);
            this.textNumber.setVisibility(View.INVISIBLE);
            this.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            this.ivRemove = (ImageView) v.findViewById(R.id.ivRemove);
        }

        public void onItemClick(View view, Object item) {
            if (ImageEditAdapter.this.clickListner != null) {
                ImageEditAdapter.this.clickListner.onItemClick(view, item);
            }
        }
    }

    public ImageEditAdapter(Context activity) {
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.application.getSelectedImages().size();
    }

    public ImageData getItem(int pos) {
        ArrayList<ImageData> list = this.application.getSelectedImages();
        if (list.size() <= pos) {
            return new ImageData();
        }
        return (ImageData) list.get(pos);
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        holder.parent.setVisibility(View.VISIBLE);
        final ImageData data = getItem(pos);
        this.glide.load(data.imagePath).into(holder.ivThumb);
        if (getItemCount() <= 2) {
            holder.ivRemove.setVisibility(View.GONE);
        } else {
            holder.ivRemove.setVisibility(View.VISIBLE);
        }
        holder.textNumber.setText(String.valueOf(pos + 1));
        holder.ivRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImageEditAdapter.this.application.min_pos = Math.min(ImageEditAdapter.this.application.min_pos, Math.max(0, pos - 1));
                MyApplication.isBreak = true;
                ImageEditAdapter.this.application.removeSelectedImage(pos);
                if (ImageEditAdapter.this.clickListner != null) {
                    ImageEditAdapter.this.clickListner.onItemClick(v, data);
                }
                ImageEditAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        View v = this.inflater.inflate(R.layout.grid_selected_item, parent, false);
        Holder holder = new Holder(v);
        if (getItemViewType(pos) == 1) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
        return holder;
    }

    public synchronized void swap(int fromPosition, int toPosition) {
        Collections.swap(this.application.getSelectedImages(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
