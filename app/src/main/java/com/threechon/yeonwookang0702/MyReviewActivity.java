package com.threechon.yeonwookang0702;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Classes.Village;
import com.threechon.yeonwookang0702.Parser.JsonParser;
import com.threechon.yeonwookang0702.Parser.ParserTask;
import com.threechon.yeonwookang0702.RecyclerViewAdapters.RecyclerViewListAdapter5;
import com.threechon.yeonwookang0702.RecyclerViewItems.MyReviewItem;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyReviewActivity extends AppCompatActivity {

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    String myJSON = "";
    MyReviewItem item;

    private static final String TAG_RESULTS = "result";

    JSONArray reviews = null;

    private FirebaseAuth mAuth;

    ArrayList<HashMap<String, String>> reviewList;

    // 리사이클러 뷰
    private List<MyReviewItem> myReviewList = new ArrayList<>(); // 내가 작성한 리뷰 리스트
    private RecyclerView myReviewView;
    private RecyclerViewListAdapter5 myReviewListAdapter;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        ImageView imageView = findViewById(R.id.review_icon);
        Glide.with(this).load(R.drawable.review_write_btn).into(imageView);

        mAuth = FirebaseAuth.getInstance();//파베 인증
        email = mAuth.getCurrentUser().getEmail();

        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }

        // 리뷰 리사이클러뷰 생성
        myReviewView = findViewById(R.id.written_review_item_list);
        myReviewListAdapter = new RecyclerViewListAdapter5(myReviewList, getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MyReviewActivity.this, LinearLayoutManager.VERTICAL, false);
        myReviewView.setLayoutManager(verticalLayoutManager);
        myReviewView.setAdapter(myReviewListAdapter);

        showList();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void showList() {
        try {
            try {
                // 웹 문자열 가져옴
                Log.d("로그인 중인 Email", email);
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/reviews_user.php?email="+email);
                myJSON = jsonParser.execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // 제이슨 객체 생성
            JSONObject jsonObj = new JSONObject(myJSON);
            reviews = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < reviews.length(); i++) {
                JSONObject c = reviews.getJSONObject(i);
                String email = c.getString("email");
                String content = c.getString("content");
                String villageId = c.getString("villageId");
                String rating = c.getString("rating");
                String date = c.getString("date");
                String review_index = c.getString("review_index");

                item = new MyReviewItem(villageId, content, Float.parseFloat(rating), email, date);
                item.setReview_index(Integer.parseInt(review_index)); // 인덱스 추가

                // villageId로 이름, 이미지 가져오기
                String villaeName;
                String villageImageURL;

                String[] array = new String[3];
                array[0] = villageId;
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

                item = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setNavBtnListener();

    }
    private void getVillages(String[] array) {

        try {
            ArrayList<Village> parserarray= (ArrayList<Village>) new ParserTask().execute(array).get();

            for(int i = 0; i < parserarray.size(); i++) {
                Village village = parserarray.get(i);
                //searchList.add(new SearchItem(village.getName(), village.getAddress1(),village.getFunction(), village.getTag(),village.getImage(), village.getIntro(), village.getId(), village.getManagerName(), village.getManagerEmail(), village.getHomepage()));

                item.setVillageName(village.getName());
                item.setImageUrl(village.getImage());

                item.setVillageAddress(village.getAddress1());
                Log.d("마이리뷰 주소", village.getAddress1());
                //village.getFunction();
                item.setVillageIntro(village.getIntro());
                item.setVillageManager(village.getManagerName());
                item.setVillageEmail(village.getManagerEmail());
                item.setVillageHomepage(village.getHomepage());

                myReviewList.add(item);

            }
            //searchListAdapter.notifyDataSetChanged();
            myReviewListAdapter.notifyDataSetChanged();
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
