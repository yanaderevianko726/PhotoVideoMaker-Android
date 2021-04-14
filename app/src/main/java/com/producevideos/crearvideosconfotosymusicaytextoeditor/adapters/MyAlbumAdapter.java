package com.producevideos.crearvideosconfotosymusicaytextoeditor.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog.Builder;

import com.bumptech.glide.Glide;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.Album;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

import java.io.File;
import java.util.ArrayList;

public class MyAlbumAdapter extends BaseAdapter {
    ArrayList<Album> albumArrayList;
    View list_item_video_overlay;
    ImageView list_item_video_thumb;
    Context mContext;
    ImageView timeline_play;

    class C08403 implements OnClickListener {
        C08403() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public MyAlbumAdapter(ArrayList<Album> albumArrayList, Context mContext) {
        this.albumArrayList = albumArrayList;
        this.mContext = mContext;
    }

    public int getCount() {
        return this.albumArrayList.size();
    }

    public Object getItem(int position) {
        return this.albumArrayList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.my_album_item, null);
        }
        Glide.with(this.mContext).load(new File(((Album) this.albumArrayList.get(position)).strImagePath)).into(this.list_item_video_thumb);
        return convertView;
    }

    public void shareVideo(final String title, String path) {
        MediaScannerConnection.scanFile(this.mContext, new String[]{path}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("video/*");
                shareIntent.putExtra("android.intent.extra.TEXT", title);
                shareIntent.putExtra("android.intent.extra.STREAM", uri);
                shareIntent.addFlags(524288);
                MyAlbumAdapter.this.mContext.startActivity(Intent.createChooser(shareIntent, "Share Video"));
            }
        });
    }

    private void deleteFile(final String strPath, final int position) {
        Builder builder = new Builder(this.mContext);
        builder.setTitle((CharSequence) "Confirmation !");
        builder.setMessage((CharSequence) "Are you sure you want to delete this file ?");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) "Yes", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(strPath);
                if (file.exists() && file.delete()) {
                    MyAlbumAdapter.this.albumArrayList.remove(position);
                    MyAlbumAdapter.this.notifyDataSetChanged();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "No", new C08403());
        builder.create();
        builder.show();
    }
}
