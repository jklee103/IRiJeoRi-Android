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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.R;
import com.threechon.yeonwookang0702.Classes.Village;
import com.threechon.yeonwookang0702.Parser.ParserTask;
import com.threechon.yeonwookang0702.RecyclerViewAdapters.RecyclerViewListAdapter4;
import com.threechon.yeonwookang0702.RecyclerViewItems.SearchItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    private TextView keyWord;
    private TextView textView;

    // 리사이클러 뷰
    private List<SearchItem> searchList = new ArrayList<>(); // 검색 결과 리스트
    private RecyclerView searchView;
    private RecyclerViewListAdapter4 searchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageView imageView = findViewById(R.id.search_icon);
        Glide.with(this).load(R.drawable.logo).into(imageView);

         FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }

        // 이전 액티비티에서 값을 전달 받음
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword"); // 검색 키워드
        String tag = intent.getStringExtra("tag"); // 지역 or 마을
        Log.d("검색태그", tag);
        String oper = intent.getStringExtra("operation"); // 체험, 자연, 전통문화, ...
        Log.d("검색오퍼", oper);
        String[] array = new String[3];
        array[0] = keyword;
        array[1] = tag;
        array[2] = oper;

        keyWord = findViewById(R.id.search_word);

        keyWord.setText(keyword); // 검색 키워드

        textView = findViewById(R.id.text_view);
        if(tag.equals("village"))
            textView.setText("' 포함된 체험 마을");

        // 검색 리사이클러뷰 생성
        searchView = findViewById(R.id.search_item_list);
        //activityView.addItemDecoration(new DividerItemDecoration(DetailActivity.this, LinearLayoutManager.HORIZONTAL));
        searchListAdapter = new RecyclerViewListAdapter4(searchList, getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
        searchView.setLayoutManager(verticalLayoutManager);
        searchView.setAdapter(searchListAdapter);

        setNavBtnListener();

        // 샘플 데이터 가져오기
        //getSampleListItem();

        // 검색리스트
        if(oper.equals("all")){//전체
            array[2]="getExprnTour";
            getVillages(array);
            array[2]="getNatureTour";
            getVillages(array);
            array[2]="getTrditClturTour";
            getVillages(array);
            array[2]="getWellBeingTour";
            getVillages(array);
        }else if(oper.equals("exp")){//체험여행
            array[2]="getExprnTour";
            getVillages(array);
        }else if(oper.equals("nat")){//자연여행
            array[2]="getNatureTour";
            getVillages(array);
        }else if(oper.equals("tra")){//전통여행
            array[2]="getTrditClturTour";
            getVillages(array);
        }else if(oper.equals("wel")){//웰빙여행
            array[2]="getWellBeingTour";
            getVillages(array);
        }

    }

    private void getVillages(String[] array) {
        try {
            ArrayList<Village> parserarray= (ArrayList<Village>) new ParserTask().execute(array).get();
            Log.d("파서어레이", parserarray.size()+"");
            for(int i = 0; i < parserarray.size(); i++) {
                Village village = parserarray.get(i);
                searchList.add(new SearchItem(village.getName(), village.getAddress1(),village.getFunction(), village.getTag(),village.getImage(), village.getIntro(), village.getId(), village.getManagerName(), village.getManagerEmail(), village.getHomepage()));
            }

            searchListAdapter.notifyDataSetChanged();
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
