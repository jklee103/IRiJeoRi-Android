package com.threechon.yeonwookang0702.Parser;


import android.os.AsyncTask;
import android.util.Log;

import com.threechon.yeonwookang0702.Classes.Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// 마을의 체험 활동 정보
public class Parser2Task extends AsyncTask {
    public final static String TAG="parser2";
    public final static String ROAD_URL = "http://openapi.ekr.or.kr/openapi/service/rest/ExprnProgrmService/";
    public final static String KEY = "---------------------";
    public final static String KEY2 = "--------------------";
    public String vilcode=null;//마을 코드

    public String newurl;
    @Override
    protected Object doInBackground(Object[] objects) {
        newurl=ROAD_URL+objects[2]+"?";//object2는 api종류
        Log.d(TAG,"style:"+objects[2]+"");
        ArrayList<Activity> list = new ArrayList<Activity>();
        Activity activity = new Activity();

        try {
            URL url=new URL(getURLParam((String) objects[0]));
            InputStream inputStream=url.openStream();
            Log.d(TAG,"url="+url);
            //url정상출력
            XmlPullParserFactory factory =XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = null;
            xpp = factory.newPullParser();
            xpp.setInput(inputStream, "UTF-8");
            String tag = null;
            int event_type = xpp.getEventType();

            String nm = null;
            while (event_type != XmlPullParser.END_DOCUMENT) {
                if (event_type == XmlPullParser.START_TAG) {
                    tag = xpp.getName();
                    Log.d(TAG,tag);
                } else if (event_type == XmlPullParser.TEXT) {

                    if(tag.equals("exprnProgrmNm")){//프로그램명

                        activity.setName(xpp.getText());
                        //innerlist.add(xpp.getText());
                        //nm = xpp.getText();
                        //Log.d(TAG,"name"+innerlist.get(0));

                    } else if (tag.equals("nmprCoMumm")) { // 참여 가능 인원 (최소)

                        activity.setMin(xpp.getText());

                    } else if (tag.equals("nmprCoMxmm")) { // 참여 가능 인원 (최대)

                        activity.setMax(xpp.getText());

                    } else if(tag.equals("pc")) { // 비용

                        activity.setPrice(xpp.getText());

                    } else if(tag.equals("exprnLiverStgDc")) { // 체험 간단 설명

                        activity.setIntro(xpp.getText());

                    } else if (tag.equals("thumbUrlCours1")) {// 썸네일

                        activity.setImage(xpp.getText());

                    } else if (tag.equals("exprnTySeNm")) { // 체험유형구분명

                        activity.setTag(xpp.getText());

                    } else if (tag.equals("vilageId")) { // 마을 ID

                        activity.setVillageId(xpp.getText());

                    } else if (tag.equals("exprnDstncId")) { // 체험 ID

                        activity.setId(xpp.getText());

                    } else if (tag.equals("exprnTySeCode")) { // 체험 유형 구분 코드

                        activity.setCode(xpp.getText());

                    } else if(tag.equals("returnReasonCode")){//에러 코드
                        nm = xpp.getText();
                        Log.d(TAG,"name"+nm);
                    }
                    else if(tag.equals("returnAuthMsg")){//에러 이유
                        nm = xpp.getText();
                        Log.d(TAG,nm);
                    }
                } else if (event_type == XmlPullParser.END_TAG) {
                    tag = xpp.getName();
                    if (tag.equals("item")) {
                        Log.d("main","item 들어옴");
                        list.add(activity);
                        activity = new Activity();
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
        //printList(list);
        //Log.d(TAG, "리턴때 첫 스트링값"+(String) list.get(0).get(0));
        return list;
    }
    private void printList(ArrayList<String> list){
        for(String entity : list){
            System.out.println(entity);
        }
    }
    private String getURLParam(String search){
        String url = newurl+"serviceKey="+KEY2;
        if (search != null) {
            System.out.println("search=" + search);
            Log.d(TAG, search + "이름!!!!!!!!!!!");
            url = url + "&vilageId=" + search;

        } else {
            Log.d(TAG, "search is null");
            url = url;
            //검색값 없으면 그냥 빈칸
        }
        return url;
    }
    private InputStream getInputStream(String para_url) {
        while (true) {
            try {
                URL url = new URL(para_url);
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                return is;
            } catch (Exception e) {
                Log.d("inputstream", e.getMessage());
            }
        }
    }
}
