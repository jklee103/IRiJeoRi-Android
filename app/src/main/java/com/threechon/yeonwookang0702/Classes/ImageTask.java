package com.threechon.yeonwookang0702.Classes;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageTask extends AsyncTask {
   Bitmap bitmap=null;
    protected Object doInBackground(Object[] objects) {
        //Log.d("imagetask", (String) objects[0]);
        bitmap=getmap((String) objects[0]);

        return bitmap;
    }


    public Bitmap getmap(String url){
        Bitmap bm=null;
        InputStream in=null;
        try {
            in=OpenHttpConn(url);
            bm= BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    private InputStream OpenHttpConn(String u) throws Exception{
        InputStream in=null;
        int response =-1;
        URL url =new URL(u);
        Log.d("imagetask","openhttp임: "+u);
        URLConnection conn=url.openConnection();
        HttpURLConnection httpcon= (HttpURLConnection) conn;
        httpcon.setAllowUserInteraction(false);
        httpcon.setInstanceFollowRedirects(true);
        httpcon.setRequestMethod("GET");
        httpcon.connect();
        response= httpcon.getResponseCode();
        if(response==HttpURLConnection.HTTP_OK)
            in=httpcon.getInputStream();
        return in;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}