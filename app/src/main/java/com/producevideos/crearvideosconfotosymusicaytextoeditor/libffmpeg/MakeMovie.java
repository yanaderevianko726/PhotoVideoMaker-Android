package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class MakeMovie extends Service {
    private long timeout = Long.MAX_VALUE;

    class TaskCreateSingleImageVideo extends AsyncTask<Void, String, Void> {
        String[] cmdArr = new String[0];
        private String output;
        private Process process;
        private ShellCommand shellCommand = new ShellCommand();
        private long startTime;

        public TaskCreateSingleImageVideo(int imgNo) {
        }

        protected void onPreExecute() {
            this.startTime = System.currentTimeMillis();
        }

        protected Void doInBackground(Void... params) {
            this.process = this.shellCommand.run(this.cmdArr);
            if (this.process != null) {
                try {
                    checkAndUpdateProcess();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            return null;
        }

        private void checkAndUpdateProcess() throws TimeoutException, InterruptedException {
            while (!Util.isProcessCompleted(this.process) && !Util.isProcessCompleted(this.process)) {
                if (MakeMovie.this.timeout == Long.MAX_VALUE || System.currentTimeMillis() <= this.startTime + MakeMovie.this.timeout) {
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
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
