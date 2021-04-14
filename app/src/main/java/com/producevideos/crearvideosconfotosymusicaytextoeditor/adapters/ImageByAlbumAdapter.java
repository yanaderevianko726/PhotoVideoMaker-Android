package com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.MyApplication;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.data.ImageData;

public class ImageByAlbumAdapter extends Adapter<ImageByAlbumAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;

    public class Holder extends ViewHolder {
        View clickableView;
        ImageView imageView;
        View parent;
        TextView textView;

        public Holder(View v) {
            super(v);
            this.parent = v;
            this.imageView = (ImageView) v.findViewById(R.id.imageView1);
            this.textView = (TextView) v.findViewById(R.id.textView1);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (ImageByAlbumAdapter.this.clickListner != null) {
                ImageByAlbumAdapter.this.clickListner.onItemClick(view, item);
            }
        }
    }

    public ImageByAlbumAdapter(Context activity) {
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.application.getImageByAlbum(this.application.getSelectedFolderId()).size();
    }

    public ImageData getItem(int pos) {
        return (ImageData) this.application.getImageByAlbum(this.application.getSelectedFolderId()).get(pos);
    }

    public void onBindViewHolder(final Holder holder, final int pos) {
        CharSequence charSequence;
        int i;
        final ImageData data = getItem(pos);
        holder.textView.setSelected(true);
        TextView textView = holder.textView;
        if (data.imageCount == 0) {
            charSequence = "";
        } else {
            charSequence = String.format("%02d", new Object[]{Integer.valueOf(data.imageCount)});
        }
        textView.setText(charSequence);
        this.glide.load(data.imagePath).into(holder.imageView);
        textView = holder.textView;
        if (data.imageCount == 0) {
            i = 0;
        } else {
            i = 1342177280;
        }
        textView.setBackgroundColor(i);
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (holder.imageView.getDrawable() == null) {
                    Toast.makeText(ImageByAlbumAdapter.this.application, "Image currpted or not support.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageByAlbumAdapter.this.application.addSelectedImage(data);
                ImageByAlbumAdapter.this.notifyItemChanged(pos);
                if (ImageByAlbumAdapter.this.clickListner != null) {
                    ImageByAlbumAdapter.this.clickListner.onItemClick(v, data);
                }
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items_by_folder, parent, false));
    }
}
