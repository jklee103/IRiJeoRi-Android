package com.threechon.yeonwookang0702;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Classes.Village;
import com.threechon.yeonwookang0702.Parser.JsonParser;
import com.threechon.yeonwookang0702.Parser.ParserTask;
import com.threechon.yeonwookang0702.RecyclerViewAdapters.RecyclerViewListAdapter;
import com.threechon.yeonwookang0702.RecyclerViewItems.SearchItem;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyTripActivity extends AppCompatActivity {

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    JSONArray items = null;

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    private FirebaseAuth mAuth;

    // 리사이클러뷰
    private List<SearchItem> recommendedList = new ArrayList<>(); // 취향 추천 마을 리스트
    private RecyclerView recommendedView;
    private RecyclerViewListAdapter recommendedListAdapter;

    private ImageView cloudImage;
    private String loginEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);
        ImageView imageView = findViewById(R.id.trip_icon);
        Glide.with(this).load(R.drawable.mytrip2).into(imageView);

        // 추천 리사이클러뷰 생성
        recommendedView = findViewById(R.id.recommended_list);
        //recommendedView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL));
        recommendedListAdapter = new RecyclerViewListAdapter(recommendedList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MyTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recommendedView.setLayoutManager(horizontalLayoutManager);
        recommendedView.setAdapter(recommendedListAdapter);

        // 워드클라우드 이미지
        cloudImage = findViewById(R.id.cloudImg);

        // 파베 인스턴스 가져오기
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
            loginEmail = mAuth.getCurrentUser().getEmail();
            setCloudImage();
            ArrayList<String> recoVillageList = new ArrayList<String>();
            recoVillageList = getRecoVillageList();
            getScrapListItem2(recoVillageList);

        } else {
            isLogined = false;
        }

        setNavBtnListener();

    }

    // 마을 아이디 리스트로  마을 항목들을 가져오는 메소드
    public void getScrapListItem2(ArrayList<String> idList){
        for(int i=0; i < idList.size(); i++) {
            String[] array = new String[3];
            array[0] = idList.get(i);
            array[1] = "vilId";

            array[2]="getExprnTour";
            getVillages2(array);
            array[2]="getNatureTour";
            getVillages2(array);
            array[2]="getTrditClturTour";
            getVillages2(array);
            array[2]="getWellBeingTour";
            getVillages2(array);
        }

    }

    private ArrayList<String> getRecoVillageList() {
        // 추천 5개 목록 가져오기
        String myJSON = "";
        ArrayList<String> villageLists = new ArrayList<String>();

        try {
            try {
                // 웹 문자열 가져옴
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/recommend_vils.php?email=" + loginEmail);
                myJSON = jsonParser.execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // 제이슨 객체 생성
            JSONObject jsonObj = new JSONObject(myJSON);
            items = jsonObj.getJSONArray("result");

            for (int i = 0; i < 1; i++) {
                JSONObject c = items.getJSONObject(i);
                villageLists.add(c.getString("vil1"));
                Log.d("1번째 마을", c.getString("vil1"));
                villageLists.add(c.getString("vil2"));
                villageLists.add(c.getString("vil3"));
                villageLists.add(c.getString("vil4"));
                villageLists.add(c.getString("vil5"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return villageLists;

    }

    // 추천 마을 가져오기
    private void getVillages2(String[] array) {
        try {
            ArrayList<Village> parserarray= (ArrayList<Village>) new ParserTask().execute(array).get();

            for(int i = 0; i < parserarray.size(); i++) {
                Village village = parserarray.get(i);
                recommendedList.add(new SearchItem(village.getName(), village.getAddress1(),village.getFunction(), village.getTag(),village.getImage(), village.getIntro(), village.getId(), village.getManagerName(), village.getManagerEmail(), village.getHomepage()));
            }
            recommendedListAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // 이미지 반환
    private void setCloudImage() {

        String myJSON = null;

        JSONArray images = null;
        String imageString = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();

            try {
                try {
                    // 웹 문자열 가져옴
                    JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/image_select.php?email="+loginEmail);
                    myJSON = jsonParser.execute().get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                // 제이슨 객체 생성
                JSONObject jsonObj = new JSONObject(myJSON);
                images = jsonObj.getJSONArray("result");

                for (int i = 0; i < 1; i++) {
                    JSONObject c = images.getJSONObject(i);
                    imageString = c.getString("image");
                    Log.d("이미지코드", imageString);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(imageString != null) {
                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                cloudImage.setImageBitmap(decodedImage);
            }
    }

    private void setNavBtnListener() {
        mytripBtn = findViewById(R.id.mytrip_btn);
        scrapBtn = findViewById(R.id.scrap_btn);
        mainBtn = findViewById(R.id.main_btn);
        reviewBtn = findViewById(R.id.review_btn);
        mypageBtn = findViewById(R.id.mypage_btn);

        // 각 버튼 클릭 리스너 추가
        // 마이 트립 버튼
        mytripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 마이 트립 액티비티 시작 시키기 (로그인 되어있을 때만 가능)
                if(isLogined) {
                    Intent intent = new Intent(getApplicationContext(), MyTripActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
                }
            }
        });

        // 스크랩 버튼
        scrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스크랩 액티비티 시작 시키기 (로그인 되어있을 때만 가능)
                if(isLogined) {
                    Intent intent = new Intent(getApplicationContext(), MyScrapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);

                }
            }
        });

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메인 액티비티 맨 위로 가져오기
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리뷰 액티비티 시작 시키기 (로그인 되어있을 때만 가능)
                if(isLogined) {
                    Intent intent = new Intent(getApplicationContext(), MyReviewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    //intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }
        });

        mypageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 마이페이지 액티비티 시작 시키기 (로그인 되어있을 때만 가능)
                if(isLogined) {
                    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                    //intent.putExtra("userId", userId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent, MYPAGE);

                } else {
                    // 로그인 되어있지 않으면 로그인 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent, SIGN_IN);
                }

            }
        });
    }
}
