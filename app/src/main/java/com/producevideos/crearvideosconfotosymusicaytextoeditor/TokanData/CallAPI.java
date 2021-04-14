package com.producevideos.crearvideosconfotosymusicaytextoeditor.TokanData;

import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper.Callback;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CallAPI {
    static final int CONNECTION_TIME_OUT = 20000;
    public static String strURL = "http://fotoglobalsolution.com/androtech/service/app_link";
    private static IOException e3;

    public interface ResultCallBack {
        void onCancelled();

        void onFailure(int i, String str);

        void onSuccess(int i, String str);
    }

    private static String getResponseText(InputStream is) {
        IOException e;
        Throwable th;
        StringBuilder error;
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    int line = br.read();
                    if (line == -1) {
                        break;
                    }
                    sb.append((char) line);
                } catch (IOException e2) {
                    e = e2;
                    bufferedReader = br;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = br;
                }
            }
            if (br != null) {
                try {
                    br.close();
                    is.close();
                    bufferedReader = br;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    error = new StringBuilder();
                    error.append("Message = ").append(e3.getMessage()).append("Cause = ").append(e3.getCause());
                    sb = error;
                    bufferedReader = br;
                }
            }
        }
        return sb.toString();
    }

    public static void callGet(String token, String methodName, boolean isJWTNeeded, ResultCallBack resultCallBack) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strURL + "/" + methodName);
            Log.i("CallAPI", "callGet: " + methodName);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            if (isJWTNeeded) {
                urlConnection.setRequestProperty("Authorization", "bearer " + token);
            }
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();
            String response;
            if (responseCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                response = getResponseText(urlConnection.getInputStream());
                urlConnection.disconnect();
                resultCallBack.onSuccess(responseCode, response);
                return;
            }
            response = getResponseText(urlConnection.getErrorStream());
            urlConnection.disconnect();
            resultCallBack.onFailure(responseCode, response);
        } catch (Exception e) {
            e.printStackTrace();
            resultCallBack.onFailure(0, getResponseText(urlConnection.getErrorStream()));
        }
    }

    public static void callPost(String token, String methodName, String rawData, boolean isJWTNeeded, ResultCallBack resultCallBack) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(strURL + "/" + methodName).openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            if (isJWTNeeded) {
                urlConnection.setRequestProperty("Authorization", "bearer " + token);
            }
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Content-Language", "en-US");
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(rawData);
            writer.flush();
            writer.close();
            outputStream.flush();
            outputStream.close();
            int responseCode = urlConnection.getResponseCode();
            String response;
            if (responseCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                response = getResponseText(urlConnection.getInputStream());
                urlConnection.disconnect();
                resultCallBack.onSuccess(responseCode, response);
                return;
            }
            response = getResponseText(urlConnection.getErrorStream());
            urlConnection.disconnect();
            resultCallBack.onFailure(responseCode, response);
        } catch (Exception e) {
            e.printStackTrace();
            resultCallBack.onFailure(0, getResponseText(urlConnection.getErrorStream()));
        }
    }
}
