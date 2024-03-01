package com.du.driverunison.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {
    private final String[] httpReqParamKeys = new String[]{"maker", "model", "shape", "years"};
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
        StringBuilder urlBuffer = new StringBuilder("http://192.168.0.10:8080/image?");
        urlBuffer.append(httpReqParamKeys[0]).append("=").append(args[0]);
        for (int i = 1; i < args.length; i++) {
            urlBuffer.append("&").append(httpReqParamKeys[i]).append("=").append(args[i]);
        }

        try {
            InputStream ins = new URL(urlBuffer.toString()).openStream();
            bitmap = BitmapFactory.decodeStream(ins);
        } catch (Exception e) {
//            Log.e(“Error”, e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

        //The JSON we will get back as a response from the server
//        JSONObject jsonResponse = null;

        //Http connections and data streams
//        URL url;
//        HttpURLConnection httpURLConnection = null;
////        OutputStreamWriter outputStreamWriter = null;
//
//        try {
//
//            //open connection to the server
//            url = new URL("http://192.168.0.10:8080/image?name=" + fileName);
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//
//            //set request properties
//            httpURLConnection.setDoOutput(false); //when true - defaults request method to POST
//            httpURLConnection.setDoInput(true);  //allow input to this HttpURLConnection
//            httpURLConnection.setRequestProperty("Content-Type", "application/json"); //header params
//            httpURLConnection.setRequestProperty("Accept", "application/json"); //header params
////            httpURLConnection.setFixedLengthStreamingMode(jsonToSend.toString().getBytes().length); //header param "content-length"
//
//            //open output stream and POST our JSON data to server
////            outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
////            outputStreamWriter.write(jsonToSend.toString());
////            outputStreamWriter.flush(); //flush the stream when we're finished writing to make sure all bytes get to their destination
//
//            //prepare input buffer and get the http response from server
////            StringBuilder stringBuilder = new StringBuilder();
//            int responseCode = httpURLConnection.getResponseCode();
//
//            //Check to make sure we got a valid status response from the server,
//            //then get the server JSON response if we did.
//            if(responseCode == HttpURLConnection.HTTP_OK) {
////                retVal = httpURLConnection.getInputStream().readAllBytes();
//
//                //read in each line of the response to the input buffer
////                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
//
//                InputStream inputStream = httpURLConnection.getInputStream();
////                int nextByte;
////                while ((nextByte = inputStream.read()) != -1) {
////
////                }
//                retVal = new byte[inputStream.available()];
//                int bytesRead = inputStream.read(retVal);
//
////                String line;
////                while ((line = bufferedReader.readLine()) != null) {
////                    stringBuilder.append(line).append("\n");
////                }
////
////                bufferedReader.close(); //close out the input stream
//
////                try {
//////                    Copy the JSON response to a local JSONObject
////                    jsonResponse = new JSONObject(stringBuilder.toString());
////                } catch (JSONException je) {
////                    je.printStackTrace();
////                }
//            }
//
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } finally {
//            if(httpURLConnection != null) {
//                httpURLConnection.disconnect(); //close out our http connection
//            }
//
////            if(outputStreamWriter != null) {
////                try {
////                    outputStreamWriter.close(); //close our output stream
////                } catch (IOException ioe) {
////                    ioe.printStackTrace();
////                }
////            }
//        }
//
//        //Return the JSON response from the server.
////        if (jsonResponse != null)
////            retVal = jsonResponse.toString().getBytes();
//        return retVal;
//    }

    public interface FetchImageTaskCallback {
        void onResultReceived(Bitmap result);
    }
}