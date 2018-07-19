package com.threechon.yeonwookang0702;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.threechon.yeonwookang0702.Classes.Activity;
import com.threechon.yeonwookang0702.Classes.ImageTask;
import com.threechon.yeonwookang0702.Nmap.NMapPOIflagType;
import com.threechon.yeonwookang0702.Nmap.NMapViewerResourceProvider;
import com.threechon.yeonwookang0702.Parser.APIMapGeocode;
import com.threechon.yeonwookang0702.Parser.JsonParser;
import com.threechon.yeonwookang0702.Parser.NetworkTask;
import com.threechon.yeonwookang0702.Parser.Parser2Task;
import com.threechon.yeonwookang0702.RecyclerViewAdapters.RecyclerViewListAdapter2;
import com.threechon.yeonwookang0702.RecyclerViewAdapters.RecyclerViewListAdapter3;
import com.threechon.yeonwookang0702.RecyclerViewItems.ActivityItem;
import com.threechon.yeonwookang0702.RecyclerViewItems.ReviewItem;
import com.google.firebase.auth.FirebaseAuth;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends NMapActivity {

    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mOverlayManager;

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    ArrayList<Float> allRating;

    //Json
    String villageId_url;
    String myJSON;
    JSONArray reviews = null;

    private String loginEmail = "";
    String CLIENT_ID = "pQeAzduTykoH7ucFsmxr";
    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    // 화면 요소들
    private ImageView thumbnail;
    private TextView nameTxt;
    private TextView introTxt;
    private TextView addressTxt;
    private TextView addressTxt2;
    private TextView managerTxt;

    private Button naverMapBtn;
    private Button bookingBtn;
    private Button emailBtn;
    private Button homepageBtn;

    private RatingBar avgRatingBar;

    // 스크랩 토글 버튼
    private ToggleButton scrapToggleBtn;

    // 리뷰 쓰기 버튼
    private ImageButton reviewWriteBtn;

    // 리사이클러뷰
    private List<ActivityItem> activityList = new ArrayList<>(); // 취향 추천 마을 리스트
    private RecyclerView activityView;
    private RecyclerViewListAdapter2 activityListAdapter;

    private List<ReviewItem> reviewList = new ArrayList<>(); // 리뷰 리스트
    private RecyclerView reviewView;
    private RecyclerViewListAdapter3 reviewListAdapter;

    private String villageId;

    private FirebaseAuth mAuth;

    private NGeoPoint npoint;
    private NMapView mMapView;
    private NMapController mMapController;

    String name;
    String intro;
    String address;
    String manager;
    String email;
    String homepage;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }
        // 이전 인텐트로부터 정보 가져오기
        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        intro = intent.getStringExtra("intro");
        address = intent.getStringExtra("address");
        manager = intent.getStringExtra("manager");
        email = intent.getStringExtra("email");
        homepage = intent.getStringExtra("homepage");
        villageId = intent.getStringExtra("id");
        image = intent.getStringExtra("image");

        intro = removeHtml(intro);

        String[] array = new String[3];
        array[0] = villageId; // 마을 아이디
        array[1] = "village"; // 모드????
        array[2] = ""; // 오퍼레이션 (밑에서 각각 호출)

        // 썸네일 설정
        thumbnail = findViewById(R.id.tonwImage);
        /*ImageTask imageTask = new ImageTask();
        Bitmap bitmap = null;
        try {
            bitmap = (Bitmap) imageTask.execute(image).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        thumbnail.setImageBitmap(bitmap);*/
        Glide.with(this).load(image).into(thumbnail);


        // 마을 이름
        nameTxt = findViewById(R.id.townName);
        nameTxt.setText(name);

        // 소개글
        introTxt = findViewById(R.id.detailIntro);
        introTxt.setText(intro);

        // 주소
        addressTxt = findViewById(R.id.detailAddressMap); // 지도 위쪽 주소
        addressTxt2 = findViewById(R.id.detailAddress);
        addressTxt.setText(address);
        addressTxt2.setText(address);

        // 담당자
        managerTxt = findViewById(R.id.detailManager);
        managerTxt.setText(manager);

        // 이메일

        // 홈페이지


        // 리뷰 쓰기 버튼
        reviewWriteBtn = findViewById(R.id.review_write_btn);

        // 스크랩 버튼
        scrapToggleBtn = findViewById(R.id.scrap_toggle_btn);
        // 로그인 안 되어있으면 스크랩, 리뷰 쓰기 불가능
        mAuth = FirebaseAuth.getInstance();//파베 인증
        if(mAuth.getCurrentUser() == null) {
            scrapToggleBtn.setEnabled(false);
            reviewWriteBtn.setEnabled(false);
            scrapToggleBtn.setVisibility(View.GONE);
            reviewWriteBtn.setVisibility(View.GONE);

        } else {
            loginEmail = mAuth.getCurrentUser().getEmail();
            scrapToggleBtn.setEnabled(true);
            reviewWriteBtn.setEnabled(true);
            scrapToggleBtn.setVisibility(View.VISIBLE);
            reviewWriteBtn.setVisibility(View.VISIBLE);
        }


        checkScrap();
        scrapToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrapToggleBtn.isChecked()){
                    scrapToggleBtn.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.scrap_toggle_btn2)
                    );
                    scrapAdd(loginEmail, villageId);
                    Toast.makeText(getApplicationContext(), "스크랩 되었습니다.", Toast.LENGTH_SHORT).show();

                }else{
                    scrapToggleBtn.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.scrap_toggle_btn1)
                    );

                    scrapDelete(loginEmail, villageId);
                    Toast.makeText(getApplicationContext(), "스크랩 해제 되었습니다.", Toast.LENGTH_SHORT).show();

                } // end if

            }
        });

        reviewWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ReviewDialog inputDialog = new ReviewDialog(DetailActivity.this);
                inputDialog.show();
                inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(inputDialog.getContent() != null) {

                            String content = inputDialog.getContent();
                            Float rating = inputDialog.getRating();

                            Log.d("내용", content);
                            Log.d("별점", rating+"");
                            Log.d("마을아이디", villageId); // villageId
                            Log.d("유저아이디", loginEmail); // loginEmail

                            // DB에 리뷰 넣기
                            NetworkTask networkTask = new NetworkTask("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/review_insert.php?email="+loginEmail+"&villageId="+villageId+"&content="+content+"&rating="+rating);
                            networkTask.execute();

                            reviewList.add(new ReviewItem(content, rating));
                            reviewListAdapter.notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(), "리뷰가 작성되었습니다. 작성된 리뷰는 '내가 작성한 리뷰 목록'에서 확인 가능합니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // 활동 리사이클러뷰 생성
        activityView = findViewById(R.id.detail_activity_list);
        //activityView.addItemDecoration(new DividerItemDecoration(DetailActivity.this, LinearLayoutManager.HORIZONTAL));
        activityListAdapter = new RecyclerViewListAdapter2(activityList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        activityView.setLayoutManager(horizontalLayoutManager);
        activityView.setAdapter(activityListAdapter);

        // 내비게이션바 세팅
        setNavBtnListener();

        array[2]="getFrcClvtExprn";
        getActivities(array);
        array[2]="getIdsrsExprn";
        getActivities(array);
        array[2]="getFdExprn";
        getActivities(array);
        array[2]="getTrditClturExprn";
        getActivities(array);
        array[2]="getNatureEclgyExprn";
        getActivities(array);
        array[2]="getHealthLeports";
        getActivities(array);
        array[2]="getFrhlLvlhExprn";
        getActivities(array);
        array[2]="getEtcExprn";
        getActivities(array);

        // 리뷰 리사이클러뷰 생성
        reviewView = findViewById(R.id.detail_review_list);
        reviewListAdapter = new RecyclerViewListAdapter3(reviewList, getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        reviewView.setLayoutManager(verticalLayoutManager);
        reviewView.setAdapter(reviewListAdapter);

        showList();

        // 평균 평점
        avgRatingBar = findViewById(R.id.ratingbar);
        Float sum = 0.0f;
        Float average;
        for(int i = 0; i < allRating.size(); i++)
            sum += allRating.get(i);

        average = sum / allRating.size();
        avgRatingBar.setRating(average);

        Drawable drawable = avgRatingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#F1C40F"), PorterDuff.Mode.SRC_ATOP);

        //주소->좌표 테스트
        npoint=new NGeoPoint(127.015771,37.651635);//못받아오면 덕성여대
        //new APIMapGeocode().execute(address);
        try {
            npoint= (NGeoPoint) new APIMapGeocode().execute(address).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //map
        setmap();

        emailBtn = findViewById(R.id.callBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnemailclick();
            }
        });

        homepageBtn = findViewById(R.id.homepageBtn);
        homepageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("detail","btnhome email, homepage"+email+homepage);
                if(homepage!=null){
                    Log.d("homepage","click");
                    Uri uri = Uri.parse(homepage);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(intent);
                } else {Toast.makeText(getApplicationContext(),"홈페이지가 없는 마을", Toast.LENGTH_SHORT).show();}
            }
        });

        naverMapBtn = findViewById(R.id.navermap_btn);
        naverMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.executeNaverMap();
            }
        });

        bookingBtn = findViewById(R.id.welchonBtn);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.welchon.com/web/index.do");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
            }
        });

        thumbnail.setFocusableInTouchMode(true);
        thumbnail.requestFocus();
    }

    public void btnemailclick() {
        Log.d("detail","btnemail email, homepage"+email+homepage);
        if(email!=null) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+email)));
        } else{
            Toast.makeText(getApplicationContext(), "대표자 전화가 없는 마을", Toast.LENGTH_SHORT).show();
        }
    }

    private void setmap(){
        mMapView = (NMapView)findViewById(R.id.map);
        mMapView.setClientId(CLIENT_ID);
        // initialize map view
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        mMapController = mMapView.getMapController();
        NMapView.LayoutParams lp = new NMapView.LayoutParams(NMapView.LayoutParams.WRAP_CONTENT,
                NMapView.LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
        mMapView.setBuiltInZoomControls(true, lp);
        mMapView.displayZoomControls(true);
        mMapController.setMapCenter(npoint, 11);
        mMapController.setZoomEnabled(true);
        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        int markerId = NMapPOIflagType.PIN;
        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(npoint.getLongitude(), npoint.getLatitude(), address, markerId, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.setEnableFocusedItemAnimation(false);
        mOverlayManager.setFocusedPOIitemHideCallout(false);
        mOverlayManager.setFocusedPOIitemVisibleExclusively(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getActivities(String[] array) {
        try {
            ArrayList<Activity> parserarray= (ArrayList<Activity>) new Parser2Task().execute(array).get();

            for(int i = 0; i < parserarray.size(); i++) {
                Activity activity = parserarray.get(i);
                ImageTask imageTask = new ImageTask(); // 이미지 가져오기
                Log.d("마을ID1", villageId);
                Log.d("마을ID2", activity.getVillageId());
                activityList.add(new ActivityItem(activity.getName(), activity.getTag(), activity.getIntro(), activity.getMin(), activity.getMax(), activity.getPrice(), activity.getImage(), activity.getId(), activity.getVillageId(), activity.getCode()));
            }
            activityListAdapter.notifyDataSetChanged();
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


    private String removeHtml(String html) {
        html = html.replaceAll("<(.*?)\\>","");
        html = html.replaceAll("<(.*?)\\\n","");
        html = html.replaceFirst("(.*?)\\>", " ");
        html = html.replaceAll("&nbsp;"," ");
        html = html.replaceAll("&(.*?);"," ");
        html = html.replaceAll("&amp;","&");

        return html;
    }

    protected void showList() {
        allRating = new ArrayList<Float>();

        try {
            try {
                // 웹 문자열 가져옴
                villageId_url = villageId;
                Log.d("현재 마을 아이디", villageId_url);
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/reviews_village.php?villageId="+villageId_url);
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
                String email = c.getString("email");
                String content = c.getString("content");
                String villageId = c.getString("villageId");
                String rating = c.getString("rating");
                allRating.add(Float.parseFloat(rating));

                ReviewItem item = new ReviewItem(content, Float.parseFloat(rating));
                reviewList.add(item);
                reviewListAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void checkScrap() {
        try {
            try {
                // 웹 문자열 가져옴
                villageId_url = villageId;
                Log.d("현재 마을 아이디", villageId_url);
                Log.d("현재 유저 아이디", loginEmail);
                JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/scrap_select.php?email=" + loginEmail + "&villageId="+villageId_url);
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
                String email = c.getString("email");
                String villageId = c.getString("villageId");

                if (email != null && villageId != null) {
                    scrapToggleBtn.setChecked(true);
                    scrapToggleBtn.setBackgroundResource(R.drawable.scrap_toggle_btn2);

                } else {
                    scrapToggleBtn.setChecked(false);
                    scrapToggleBtn.setBackgroundResource(R.drawable.scrap_toggle_btn1);

                } if(reviews.length() == 0) {
                    scrapToggleBtn.setChecked(false);
                    scrapToggleBtn.setBackgroundResource(R.drawable.scrap_toggle_btn1);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void scrapAdd(String email, String villageId) {
        String url = "http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/scrap_add.php?email=" + email + "&villageId=" + villageId;
        NetworkTask networkTask = new NetworkTask(url);
        networkTask.execute();
    }

    public void scrapDelete(String email, String villageId) {
        String url = "http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/scrap_delete.php?email=" + email + "&villageId=" + villageId;
        NetworkTask networkTask = new NetworkTask(url);
        networkTask.execute();
    }



}