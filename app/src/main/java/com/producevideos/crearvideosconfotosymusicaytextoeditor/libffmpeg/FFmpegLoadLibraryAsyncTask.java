package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

class FFmpegLoadLibraryAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final Context context;
    private final String cpuArchNameFromAssets;
    private final FFmpegLoadBinaryResponseHandler ffmpegLoadBinaryResponseHandler;

    FFmpegLoadLibraryAsyncTask(Context context, String cpuArchNameFromAssets, FFmpegLoadBinaryResponseHandler ffmpegLoadBinaryResponseHandler) {
        this.context = context;
        this.cpuArchNameFromAssets = cpuArchNameFromAssets;
        this.ffmpegLoadBinaryResponseHandler = ffmpegLoadBinaryResponseHandler;
    }

    protected Boolean doInBackground(Void... params) {
        File ffmpegFile = new File(FileUtils.getFFmpeg(this.context));
        if (ffmpegFile.exists() && isDeviceFFmpegVersionOld() && !ffmpegFile.delete()) {
            return Boolean.valueOf(false);
        }
        if (!ffmpegFile.exists() && FileUtils.copyBinaryFromAssetsToData(this.context, this.cpuArchNameFromAssets + File.separator + "ffmpeg", "ffmpeg")) {
            if (ffmpegFile.canExecute()) {
                Log.m20d("FFmpeg is executable");
                return Boolean.valueOf(true);
            }
            Log.m20d("FFmpeg is not executable, trying to make it executable ...");
            if (ffmpegFile.setExecutable(true)) {
                return Boolean.valueOf(true);
            }
        }
        return (ffmpegFile.exists() && ffmpegFile.canExecute()) ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        if (this.ffmpegLoadBinaryResponseHandler != null) {
            if (isSuccess.booleanValue()) {
                this.ffmpegLoadBinaryResponseHandler.onSuccess();
                this.ffmpegLoadBinaryResponseHandler.onSuccess(this.cpuArchNameFromAssets);
            } else {
                this.ffmpegLoadBinaryResponseHandler.onFailure();
                this.ffmpegLoadBinaryResponseHandler.onFailure(this.cpuArchNameFromAssets);
            }
            this.ffmpegLoadBinaryResponseHandler.onFinish();
        }
    }

    private boolean isDeviceFFmpegVersionOld() {
        return CpuArch.fromString(FileUtils.SHA1(FileUtils.getFFmpeg(this.context))).equals(CpuArch.NONE);
    }
}
