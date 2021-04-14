package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

class FFmpegExecuteAsyncTask extends AsyncTask<Void, String, CommandResult> {
    private final String[] cmd;
    private final FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler;
    private String output = "";
    private Process process;
    private final ShellCommand shellCommand;
    private long startTime;
    private final long timeout;

    FFmpegExecuteAsyncTask(String[] cmd, long timeout, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) {
        this.cmd = cmd;
        this.timeout = timeout;
        this.ffmpegExecuteResponseHandler = ffmpegExecuteResponseHandler;
        this.shellCommand = new ShellCommand();
    }

    protected void onPreExecute() {
        this.startTime = System.currentTimeMillis();
        if (this.ffmpegExecuteResponseHandler != null) {
            this.ffmpegExecuteResponseHandler.onStart();
        }
    }

    protected CommandResult doInBackground(Void... params) {
        CommandResult dummyFailureResponse;
        try {
            this.process = this.shellCommand.run(this.cmd);
            if (this.process == null) {
                dummyFailureResponse = CommandResult.getDummyFailureResponse();
            } else {
                Log.m20d("Running publishing updates method");
                checkAndUpdateProcess();
                dummyFailureResponse = CommandResult.getOutputFromProcess(this.process);
                Util.destroyProcess(this.process);
                Util.destroyProcess(this.process);
            }
        } catch (TimeoutException e) {
            Log.m21e("FFmpeg timed out", e);
            dummyFailureResponse = new CommandResult(false, e.getMessage());
        } catch (Exception e2) {
            Log.m21e("Error running FFmpeg", e2);
            dummyFailureResponse = CommandResult.getDummyFailureResponse();
        } finally {
            Util.destroyProcess(this.process);
        }
        return dummyFailureResponse;
    }

    protected void onProgressUpdate(String... values) {
        if (values != null && values[0] != null && this.ffmpegExecuteResponseHandler != null) {
            this.ffmpegExecuteResponseHandler.onProgress(values[0]);
        }
    }

    protected void onPostExecute(CommandResult commandResult) {
        if (this.ffmpegExecuteResponseHandler != null) {
            this.output += commandResult.output;
            if (commandResult.success) {
                this.ffmpegExecuteResponseHandler.onSuccess(this.output);
            } else {
                this.ffmpegExecuteResponseHandler.onFailure(this.output);
            }
            this.ffmpegExecuteResponseHandler.onFinish();
        }
    }

    private void checkAndUpdateProcess() throws TimeoutException, InterruptedException {
        while (!Util.isProcessCompleted(this.process) && !Util.isProcessCompleted(this.process)) {
            if (this.timeout == Long.MAX_VALUE || System.currentTimeMillis() <= this.startTime + this.timeout) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
                    while (true) {
                        String line = reader.readLine();
                        if (line != null) {
                            if (!isCancelled()) {
                                this.output += line + "\n";
                                publishProgress(new String[]{line});
                            } else {
                                return;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new TimeoutException("FFmpeg timed out");
            }
        }
    }

    boolean isProcessCompleted() {
        return Util.isProcessCompleted(this.process);
    }
}
