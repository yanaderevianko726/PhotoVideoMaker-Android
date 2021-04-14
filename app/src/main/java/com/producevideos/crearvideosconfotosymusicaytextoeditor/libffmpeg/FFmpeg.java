package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.content.Context;
import android.text.TextUtils;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.lang.reflect.Array;
import java.util.Map;

public class FFmpeg implements FFmpegInterface {
    private static int[] $SWITCH_TABLE$com$videolib$libffmpeg$CpuArch = null;
    private static final long MINIMUM_TIMEOUT = 10000;
    private static FFmpeg instance = null;
    private final Context context;
    private FFmpegExecuteAsyncTask ffmpegExecuteAsyncTask;
    private FFmpegLoadLibraryAsyncTask ffmpegLoadLibraryAsyncTask;
    private long timeout = Long.MAX_VALUE;

    static int[] $SWITCH_TABLE$com$videolib$libffmpeg$CpuArch() {
        int[] iArr = $SWITCH_TABLE$com$videolib$libffmpeg$CpuArch;
        if (iArr == null) {
            iArr = new int[CpuArch.values().length];
            try {
                iArr[CpuArch.ARMv7.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CpuArch.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CpuArch.x86.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$videolib$libffmpeg$CpuArch = iArr;
        }
        return iArr;
    }

    private FFmpeg(Context context) {
        this.context = context.getApplicationContext();
        Log.setDEBUG(Util.isDebug(this.context));
    }

    public static FFmpeg getInstance(Context context) {
        if (instance == null) {
            instance = new FFmpeg(context);
        }
        return instance;
    }

    public void loadBinary(FFmpegLoadBinaryResponseHandler ffmpegLoadBinaryResponseHandler) throws FFmpegNotSupportedException {
        String cpuArchNameFromAssets = null;
        Log.m20d(CpuArchHelper.getCpuArch());
        switch ($SWITCH_TABLE$com$videolib$libffmpeg$CpuArch()[CpuArchHelper.getCpuArch().ordinal()]) {
            case 1:
                Log.m23i("Loading FFmpeg for x86 CPU");
                cpuArchNameFromAssets = "x86";
                break;
            case 2:
                Log.m23i("Loading FFmpeg for armv7 CPU");
                cpuArchNameFromAssets = "armeabi-v7a";
                break;
            case 3:
                throw new FFmpegNotSupportedException("Device not supported");
        }
        if (TextUtils.isEmpty(cpuArchNameFromAssets)) {
            throw new FFmpegNotSupportedException("Device not supported");
        }
        this.ffmpegLoadLibraryAsyncTask = new FFmpegLoadLibraryAsyncTask(this.context, cpuArchNameFromAssets, ffmpegLoadBinaryResponseHandler);
        this.ffmpegLoadLibraryAsyncTask.execute(new Void[0]);
    }

    public void execute(Map<String, String> environvenmentVars, String[] cmd, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) throws FFmpegCommandAlreadyRunningException {
        if (this.ffmpegExecuteAsyncTask != null && !this.ffmpegExecuteAsyncTask.isProcessCompleted()) {
            throw new FFmpegCommandAlreadyRunningException("FFmpeg command is already running, you are only allowed to run single command at a time");
        } else if (cmd.length != 0) {
            this.ffmpegExecuteAsyncTask = new FFmpegExecuteAsyncTask((String[]) concatenate(new String[]{FileUtils.getFFmpeg(this.context, environvenmentVars)}, cmd), this.timeout, ffmpegExecuteResponseHandler);
            this.ffmpegExecuteAsyncTask.execute(new Void[0]);
        } else {
            throw new IllegalArgumentException("shell command cannot be empty");
        }
    }

    public <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Object[] c = (Object[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return (T[]) c;
    }

    public void execute(String[] cmd, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) throws FFmpegCommandAlreadyRunningException {
        execute(null, cmd, ffmpegExecuteResponseHandler);
    }

    public String getDeviceFFmpegVersion() throws FFmpegCommandAlreadyRunningException {
        CommandResult commandResult = new ShellCommand().runWaitFor(new String[]{FileUtils.getFFmpeg(this.context), "-version"});
        if (commandResult.success) {
            return commandResult.output.split(" ")[2];
        }
        return "";
    }

    public String getLibraryFFmpegVersion() {
        return "n2.4.2";
    }

    public boolean isFFmpegCommandRunning() {
        return (this.ffmpegExecuteAsyncTask == null || this.ffmpegExecuteAsyncTask.isProcessCompleted()) ? false : true;
    }

    public boolean killRunningProcesses() {
        return Util.killAsync(this.ffmpegLoadLibraryAsyncTask) || Util.killAsync(this.ffmpegExecuteAsyncTask);
    }

    public void setTimeout(long timeout) {
        if (timeout >= MINIMUM_TIMEOUT) {
            this.timeout = timeout;
        }
    }
}
