package com.threechon.yeonwookang0702;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Classes.Village;
import com.threechon.yeonwookang0702.Parser.JsonParser;
import com.threechon.yeonwookang0702.Parser.ParserTask;
import com.threechon.yeonwookang0702.RecyclerViewAdapters.RecyclerViewListAdapter4;
import com.threechon.yeonwookang0702.RecyclerViewItems.SearchItem;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyScrapActivity extends AppCompatActivity {

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    // 리사이클러 뷰
    private List<SearchItem> scrapList = new ArrayList<>(); // 스크랩 리스트
    private RecyclerView scrapView;
    private RecyclerViewListAdapter4 scrapListAdapter;

    ArrayList<String> villageIdList;

    private FirebaseAuth mAuth;
    String loginEmail = null;

    SearchItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scrap);
        ImageView imageView = findViewById(R.id.scrap_icon);
        Glide.with(this).load(R.drawable.scrap_toggle_btn2).into(imageView);

        mAuth = FirebaseAuth.getInstance();//파베 인증
        loginEmail = mAuth.getCurrentUser().getEmail();

        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }

        // 스크랩 리사이클러뷰 생성
        scrapView = findViewById(R.id.scrap_item_list);
        //activityView.addItemDecoration(new DividerItemDecoration(DetailActivity.this, LinearLayoutManager.HORIZONTAL));
        scrapListAdapter = new RecyclerViewListAdapter4(scrapList, getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MyScrapActivity.this, LinearLayoutManager.VERTICAL, false);
        scrapView.setLayoutManager(verticalLayoutManager);
        scrapView.setAdapter(scrapListAdapter);

        // 샘플 데이터 가져오기
        //getSampleListItem();

        villageIdList = new ArrayList<String>();
        villageIdList = getVillageIdList();
        //for(int i = 0 ; i < villageIdList.size(); i++)
        //    Log.d("스크랩목록", villageIdList.get(i));

        getScrapListItem(villageIdList);

        setNavBtnListener();

    }

    // 샘플 데이터 호출 메소드
    private void getSampleListItem() {
        //SearchItem item1 = new SearchItem(R.drawable.townsampleimage,"샘플마을1","강원도 샘플리", "#체험 #워크샵", "체험마을");
        //SearchItem item2 = new SearchItem(R.drawable.townsampleimage,"샘플마을2","강원도 샘플리", "#먹거리 #전통문화", "전통문화마을");

        //scrapList.add(item1);
        //scrapList.add(item2);

        //scrapListAdapter.notifyDataSetChanged();
    }

    // 마을 아이디 리스트를 가져 오는 메소드
    private ArrayList<String> getVillageIdList() {
        ArrayList<String> villageIdList = new ArrayList<String>();

        String myJSON = null;

        JSONArray reviews = null;

        try {
            try {
                // 웹 문자열 가져옴
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/scrap_select_all.php?email="+loginEmail);
                myJSON = jsonParser.execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // 제이슨 객체 생성
            JSONObject jsonObj = new JSONObject(myJSON);
            reviews = jsonObj.getJSONArray("result");

            for (int i = 0; i < reviews.length(); i++) {
                JSONObject c = reviews.getJSONObject(i);
                String villageId = c.getString("villageId");

                villageIdList.add(villageId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return villageIdList;
    }

    // 마을 아이디 리스트로 스크랩 목록 항목들을 가져오는 메소드
    public void getScrapListItem(ArrayList<String> idList){
        for(int i=0; i < idList.size(); i++) {
            String[] array = new String[3];
            array[0] = idList.get(i);
            array[1] = "vilId";

            array[2]="getExprnTour";
            array[2]="getExprnTour";
            getVillages(array);
            array[2]="getNatureTour";
            getVillages(array);
            array[2]="getTrditClturTour";
            getVillages(array);
            array[2]="getWellBeingTour";
            getVillages(array);
        }

    }

    private void getVillages(String[] array) {

        try {
            ArrayList<Village> parserarray= (ArrayList<Village>) new ParserTask().execute(array).get();
            item = new SearchItem();

            for(int i = 0; i < parserarray.size(); i++) {
                Village village = parserarray.get(i);
                //searchList.add(new SearchItem(village.getName(), village.getAddress1(),village.getFunction(), village.getTag(),village.getImage(), village.getIntro(), village.getId(), village.getManagerName(), village.getManagerEmail(), village.getHomepage()));

                scrapList.add(new SearchItem(village.getName(), village.getAddress1(),village.getFunction(), village.getTag(),village.getImage(), village.getIntro(), village.getId(), village.getManagerName(), village.getManagerEmail(), village.getHomepage()));

            }
            //searchListAdapter.notifyDataSetChanged();
            scrapListAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
