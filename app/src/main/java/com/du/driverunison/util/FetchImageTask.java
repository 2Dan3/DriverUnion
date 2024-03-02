package com.du.driverunison.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {
    private final String[] httpReqParamKeys = new String[]{"maker", "model", "shape", "years"};
    private final String IMAGE_URL = "http://192.168.0.31:8080/image?";
    FetchImageTaskCallback listener;

    public FetchImageTask(FetchImageTaskCallback listener) {
        this.listener = listener;
    }

    protected Bitmap doInBackground(String... args) {
        Bitmap bitmap = fetchImage(args);
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        listener.onResultReceived(result);
    }
    private Bitmap fetchImage(String... args){
        if (args.length == 0)
            return null;

        Bitmap bitmap = null;
        StringBuilder urlBuffer = new StringBuilder(IMAGE_URL);
        urlBuffer.append(httpReqParamKeys[0]).append("=").append(args[0]);
        for (int i = 1; i < args.length; i++) {
            urlBuffer.append("&").append(httpReqParamKeys[i]).append("=").append(args[i]);
        }

        try {
            InputStream ins = new URL(urlBuffer.toString()).openStream();
            bitmap = BitmapFactory.decodeStream(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public interface FetchImageTaskCallback {
        void onResultReceived(Bitmap result);
    }
}