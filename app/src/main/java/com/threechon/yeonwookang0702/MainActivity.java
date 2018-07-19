package com.threechon.yeonwookang0702;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView loginTxt;

    JSONArray items = null;

    // 로그인 된 사용자 정보
    public String userId = "";
    // FLAG
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부

    private FirebaseAuth mAuth;

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    // 검색 옵션 설정 스피너
    private List<String> spinnerList = new ArrayList<>(); // 스피너 목록 항목
    private Spinner optionSpinner;
    private ArrayAdapter<String> spinnerAdapter;

    // 라디오 버튼
    private RadioButton regionRadio;
    private RadioButton villageRadio;
    private String tagWord = "region";

    // 검색 창
    private EditText searchTxt;
    // 검색 버튼
    private Button searchBtn;

    // 리사이클러뷰
    private List<SearchItem> recommendedList = new ArrayList<>(); // 취향 추천 마을 리스트
    private RecyclerView recommendedView;
    private RecyclerViewListAdapter recommendedListAdapter;

    private List<SearchItem> bestreviewList = new ArrayList<>(); // 사용자 평점 베스트 마을 리스트
    private RecyclerView bestreviewView;
    private RecyclerViewListAdapter bestreviewListAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        // 네비게이션 바 버튼 생성 (함수 호출)
        setNavBtnListener();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.icon_splash).into(imageView);

        // 추천 리사이클러뷰 생성
        recommendedView = findViewById(R.id.recommended_list);
        //recommendedView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL));
        recommendedListAdapter = new RecyclerViewListAdapter(recommendedList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recommendedView.setLayoutManager(horizontalLayoutManager);
        recommendedView.setAdapter(recommendedListAdapter);

        // 베스트 리사이클러뷰 생성
        bestreviewView = findViewById(R.id.best_list);
        //bestreviewView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayout.HORIZONTAL));
        bestreviewListAdapter = new RecyclerViewListAdapter(bestreviewList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        bestreviewView.setLayoutManager(horizontalLayoutManager2); // 위에서 생성한 것 재할용
        bestreviewView.setAdapter(bestreviewListAdapter);

        // 파베 인스턴스 가져오기
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getEmail();
            isLogined = true;
            ArrayList<String> recoVillageList = new ArrayList<String>();
            recoVillageList = getRecoVillageList();
            getScrapListItem2(recoVillageList);
        } else {
            isLogined = false;
        }

        loginTxt = findViewById(R.id.logintxt);

        if(isLogined) {
            loginTxt.setText("");

        } else {
            loginTxt.setText("* 로그인 이후 사용 가능합니다.");
        }

        ArrayList<String> bestVillagesList = new ArrayList<String>();
        bestVillagesList = getBestVillageList();

        getScrapListItem(bestVillagesList);

        // 네비게이션 바 버튼 생성 (함수 호출)
        setNavBtnListener();

        // 스피너 생성
        optionSpinner = findViewById(R.id.option_spinner);
        // 스피너에 항목 추가
        spinnerList.add("전체"); spinnerList.add("체험"); spinnerList.add("자연"); spinnerList.add("전통"); spinnerList.add("웰빙");
        optionSpinner.setPrompt("검색 옵션을 선택해주세요.");
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, spinnerList);
        optionSpinner.setAdapter(spinnerAdapter);

        // 라디오 버튼 생성
        regionRadio = findViewById(R.id.region_radio);
        villageRadio = findViewById(R.id.town_radio);

        regionRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagWord = "region";
            }
        });

        villageRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagWord = "village";
            }
        });

        // 검색 창
        searchTxt = findViewById(R.id.search_text);

        // 검색 버튼
        searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchWord = searchTxt.getText().toString(); //검색어

                if(searchWord.equals("") || searchWord.equals(" ")) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d("tagword", tagWord);
                    String oper = optionSpinner.getSelectedItem().toString(); // 오퍼레이션 종류
                    String operWord = "";

                    if (oper.equals("전체")) {
                        operWord = "all";

                    } else if (oper.equals("체험")) {
                        operWord = "exp";

                    } else if (oper.equals("자연")) {
                        operWord = "nat";

                    } else if (oper.equals("전통")) {
                        operWord = "tra";

                    } else if (oper.equals("웰빙")) {
                        operWord = "wel";
                    }

                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    intent.putExtra("keyword", searchWord); // ex> 강원도, 거북이마을 (배열 0번)
                    intent.putExtra("tag", tagWord); // ex> 지역명, 마을명 (배열 1번)
                    intent.putExtra("operation", operWord); // ex> 상세 오퍼레이션, 체험, 전통문화 등 (배열 2번)
                    startActivity(intent);
                }
            }
        });

        if(!isLogined) {
            Toast.makeText(getApplicationContext(), "나의 여행 분석, 리뷰 남기기, 스크랩 등\n서비스 사용을 위해 로그인을 해주세요.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 로그인 액티비티에서 돌아온 결과
        if(requestCode == SIGN_IN) {
            if(resultCode == Activity.RESULT_OK) {// 로그인 성공이면
                isLogined = true;
                userId = mAuth.getCurrentUser().getEmail();
                loginTxt.setText(" ");
                ArrayList<String> recoVillageList = new ArrayList<String>();
                recoVillageList = getRecoVillageList();
                getScrapListItem2(recoVillageList);
                onResume();
                Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_LONG).show();

            }
        }
        else if(requestCode == MYPAGE){
            if(resultCode == Activity.RESULT_OK) {// 로그아웃이면
                isLogined = false;
                loginTxt.setText("* 로그인 이후 사용 가능합니다.");
                onResume();
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 마을 아이디 리스트로  마을 항목들을 가져오는 메소드
    public void getScrapListItem(ArrayList<String> idList){
        for(int i=0; i < idList.size(); i++) {
            String[] array = new String[3];
            array[0] = idList.get(i);
            array[1] = "vilId";

            array[2]="getExprnTour";
            getVillages1(array);
            array[2]="getNatureTour";
            getVillages1(array);
            array[2]="getTrditClturTour";
            getVillages1(array);
            array[2]="getWellBeingTour";
            getVillages1(array);
        }

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

    private ArrayList<String> getBestVillageList() {
        // 최고 5개 목록 가져오기
        String myJSON = "";
        ArrayList<String> villageLists = new ArrayList<String>();

        try {
            try {
                // 웹 문자열 가져옴
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/best_village.php");
                myJSON = jsonParser.execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // 제이슨 객체 생성
            JSONObject jsonObj = new JSONObject(myJSON);
            items = jsonObj.getJSONArray("result");

            for (int i = 0; i < 5; i++) {
                JSONObject c = items.getJSONObject(i);
                String villageId = c.getString("villageId");
                String rating = c.getString("average");
                villageLists.add(villageId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    return villageLists;

    }

    // 최고 평점 마을 가져오기
    private void getVillages1(String[] array) {
        try {
            ArrayList<Village> parserarray= (ArrayList<Village>) new ParserTask().execute(array).get();

            for(int i = 0; i < parserarray.size(); i++) {
                Village village = parserarray.get(i);
                bestreviewList.add(new SearchItem(village.getName(), village.getAddress1(),village.getFunction(), village.getTag(),village.getImage(), village.getIntro(), village.getId(), village.getManagerName(), village.getManagerEmail(), village.getHomepage()));
            }
            bestreviewListAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getRecoVillageList() {
        // 추천 5개 목록 가져오기
        String myJSON = "";
        ArrayList<String> villageLists = new ArrayList<String>();

        try {
            try {
                // 웹 문자열 가져옴
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/recommend_vils.php?email=" + userId);
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

    // 네비게이션 바 생성 후 버튼에 리스너 추가하는 메소드
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
                    intent.putExtra("userId", userId);
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
                    intent.putExtra("userId", userId);
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
