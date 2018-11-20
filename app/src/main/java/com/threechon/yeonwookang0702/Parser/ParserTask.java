package com.threechon.yeonwookang0702.Parser;


import android.os.AsyncTask;
import android.util.Log;

import com.threechon.yeonwookang0702.Classes.Village;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// 체험 마을 정보
public class ParserTask extends AsyncTask {
    public final static String TAG="main";
    public final static String ROAD_URL = "http://openapi.ekr.or.kr/openapi/service/rest/FrhlExprnVilageService/";
    public final static String KEY = "---------------";
    public final static String KEY2 = "------------------";
    public String vilcode=null;//마을 코드
    private String searchmode;//검색모드
    public String newurl;
    @Override
    protected Object doInBackground(Object[] objects) {
        newurl=ROAD_URL+objects[2]+"?";
        Log.d(TAG,"style:"+objects[2]+"");

        // 마을 객체들을 담을 배열
        ArrayList<Village> list = new ArrayList<Village>();
        // 마을 객체 생성
        Village village = new Village();

        searchmode= (String) objects[1];//1번째 인자 받아오기
        //objects 크기 2 중 0번째는 검색어, 1번째는 지역인지 이름인지
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

                    if(tag.equals("vilageNm")){//마을 명
                        //Log.d(TAG,innerlist.get(0));
                        //innerlist.add(xpp.getText());
                        //nm = xpp.getText();

                        village.setName(xpp.getText());

                        Log.d(TAG,"name"+xpp.getText().toString());
                    }else if(tag.equals("returnReasonCode")){//에러 코드
                        nm = xpp.getText();
                        Log.d(TAG,"name"+nm);
                    }else if(tag.equals("vilageId")){//마을 코드

                        village.setId(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("prcafsManNm")){//실무자명

                        village.setManagerName(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("vilageRprsntvTlphonNo")){//실무자 이메일

                        village.setManagerEmail(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("adres1")){//주소 1

                        village.setAddress1(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("adres2")){//주소 2

                        village.setAddress2(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("fnctNm")){//기능명

                        village.setFunction(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("thumbUrlCours1")){//이미지

                        village.setImage(xpp.getText());

                        //innerlist.add(xpp.getText());
                    }else if(tag.equals("themaNm")){// 여행분류

                        village.setTag(xpp.getText());

                        //innerlist.add(xpp.getText());
                    } else if(tag.equals("vilageIntrcn")) { //마을소개

                        village.setIntro(xpp.getText());

                    } else if(tag.equals("vilageHmpgUrl")) { //마을 홈페이지
                        village.setHomepage(xpp.getText());
                    }
                    else if(tag.equals("returnAuthMsg")){//에러 이유
                        nm = xpp.getText();
                        Log.d(TAG,nm);
                    }
                } else if (event_type == XmlPullParser.END_TAG) {
                    tag = xpp.getName();
                    if (tag.equals("item")) {
                        Log.d("main","item 들어옴");
                      list.add(village);
                        village = new Village();
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
        if(searchmode.equals("village")) {
            if (search != null) {
                System.out.println("search=" + search);
                Log.d(TAG, search + "이름!!!!!!!!!!!");
                url = url + "&vilageNm=" + search;

            } else {
                Log.d(TAG, "search is null");
                url = url;
                //검색값 없으면 그냥 빈칸
            }
        }  else if(searchmode.equals("region")){//주소//address 맞나?
            if (search != null) {
                System.out.println("search=" + search);
                Log.d(TAG, search + "주소!!!!!!!!!!!");
                url = url + "&adres1=" + search;

            } else {
                Log.d(TAG, "search is null");
                url = url;
                //검색값 없으면 그냥 빈칸
            }
        } else if(searchmode.equals("vilId")){//주소//address 맞나?
            if (search != null) {
                System.out.println("search=" + search);
                Log.d(TAG, search + "주소!!!!!!!!!!!");
                url = url + "&vilageId=" + search;

            } else {
                Log.d(TAG, "search is null");
                url = url;
                //검색값 없으면 그냥 빈칸
            }
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
