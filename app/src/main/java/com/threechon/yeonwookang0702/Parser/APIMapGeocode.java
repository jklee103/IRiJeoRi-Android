package com.threechon.yeonwookang0702.Parser;

import android.os.AsyncTask;
import android.util.Log;

import com.nhn.android.maps.maplib.NGeoPoint;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class APIMapGeocode extends AsyncTask{
    NGeoPoint npoint;
    String[] points;
    @Override
    protected Object doInBackground(Object[] objects) {
        String clientId = "---------------";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "-----------------";//애플리케이션 클라이언트 시크릿값";

        ArrayList<String> arr1=new ArrayList<>();
        ArrayList<String> arr2=new ArrayList<>();
        try {
            String addr = URLEncoder.encode((String) objects[0], "UTF-8");
            //String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
            String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
/////////////////////////

            try {
                InputStream inputStream=fromStringBuffer(response);
                Log.d("geocode","stream");
                //url정상출력
                XmlPullParserFactory factory =XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = null;
                xpp = factory.newPullParser();
                xpp.setInput(inputStream, "UTF-8");
                String tag = null;
                int event_type = xpp.getEventType();

                while (event_type != XmlPullParser.END_DOCUMENT) {
                    if (event_type == XmlPullParser.START_TAG) {
                        tag = xpp.getName();
                        Log.d("geo",tag);
                    } else if (event_type == XmlPullParser.TEXT) {

                        if(tag.equals("x")){//x
                            arr1.add(xpp.getText());
                            //mp.setLon(xpp.getText());
                            Log.d("geo lon:",xpp.getText());
                        }else if(tag.equals("y")){//y
                            arr2.add(xpp.getText());
                            //mp.setLat(xpp.getText());
                            Log.d("geo lat:",xpp.getText());
                        }else if(tag.equals("returnReasonCode")){//에러 코드

                            Log.d("geo","name"+xpp.getText());
                        }
                    } else if (event_type == XmlPullParser.END_TAG) {
                        tag = xpp.getName();
                        if (tag.equals("item")) {

                        }
                    }
                    event_type = xpp.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.d("geo result", arr1.get(0)+ arr2.get(0));
        npoint =new NGeoPoint(Double.parseDouble(arr1.get(0)), Double.parseDouble(arr2.get(0)));
        return npoint;
    }
    public static InputStream fromStringBuffer(StringBuffer buf) {
        return new ByteArrayInputStream(buf.toString().getBytes(StandardCharsets.UTF_8));
    }
}
