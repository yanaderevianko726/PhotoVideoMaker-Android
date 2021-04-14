package com.producevideos.crearvideosconfotosymusicaytextoeditor.util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Genres;
import android.provider.MediaStore.Audio.Media;

import java.util.HashMap;

public class SongMetadataReader {
    public Uri GENRES_URI = Genres.EXTERNAL_CONTENT_URI;
    public Activity mActivity = null;
    public String mAlbum = "";
    public String mArtist = "";
    public int mDuration;
    public String mFilename = "";
    public String mGenre = "";
    public String mTitle = "";
    public int mYear = -1;

    public SongMetadataReader(Activity activity, String filename) {
        this.mActivity = activity;
        this.mFilename = filename;
        this.mTitle = getBasename(filename);
        try {
            ReadMetadata();
        } catch (Exception e) {
        }
    }

    private void ReadMetadata() {
        HashMap<String, String> genreIdMap = new HashMap();
        Cursor c = this.mActivity.getContentResolver().query(this.GENRES_URI, new String[]{"_id", "name"}, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            genreIdMap.put(c.getString(0), c.getString(1));
            c.moveToNext();
        }
        this.mGenre = "";
        for (String genreId : genreIdMap.keySet()) {
            if (this.mActivity.getContentResolver().query(makeGenreUri(genreId), new String[]{"_data"}, "_data LIKE \"" + this.mFilename + "\"", null, null).getCount() != 0) {
                this.mGenre = (String) genreIdMap.get(genreId);
                break;
            }
        }
        Uri uri = Media.getContentUriForPath(this.mFilename);
        c = this.mActivity.getContentResolver().query(uri, new String[]{"_id", "title", "artist", "album", "year", "duration", "_data"}, "_data LIKE \"" + this.mFilename + "\"", null, null);
        if (c.getCount() == 0) {
            this.mTitle = getBasename(this.mFilename);
            this.mArtist = "";
            this.mAlbum = "";
            this.mYear = -1;
            return;
        }
        c.moveToFirst();
        this.mTitle = getStringFromColumn(c, "title");
        if (this.mTitle == null || this.mTitle.length() == 0) {
            this.mTitle = getBasename(this.mFilename);
        }
        this.mArtist = getStringFromColumn(c, "artist");
        this.mAlbum = getStringFromColumn(c, "album");
        this.mYear = getIntegerFromColumn(c, "year");
        this.mDuration = getIntegerFromColumn(c, "duration");
    }

    private Uri makeGenreUri(String genreId) {
        return Uri.parse(this.GENRES_URI.toString() + "/" + genreId + "/members");
    }

    private String getStringFromColumn(Cursor c, String columnName) {
        String value = c.getString(c.getColumnIndexOrThrow(columnName));
        return (value == null || value.length() <= 0) ? null : value;
    }

    private int getIntegerFromColumn(Cursor c, String columnName) {
        Integer value = Integer.valueOf(c.getInt(c.getColumnIndexOrThrow(columnName)));
        if (value != null) {
            return value.intValue();
        }
        return -1;
    }

    private String getBasename(String filename) {
        return filename.substring(filename.lastIndexOf(47) + 1, filename.lastIndexOf(46));
    }
}
