package com.threechon.yeonwookang0702;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Parser.JsonParser;
import com.threechon.yeonwookang0702.Parser.NetworkTask;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyPageActivity extends AppCompatActivity {

    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    TextView tvemail;
    String loginEmail;

    CheckBox farm;
    CheckBox making;
    CheckBox food;
    CheckBox tradition2;
    CheckBox nature2;
    CheckBox health;
    CheckBox life;
    CheckBox etc;

    ArrayList<Integer> checkedList;

    Button edit_btn;

    private FirebaseAuth mAuth;
    private static final String TAG_RESULTS = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ImageView userimage = findViewById(R.id.userimage);
        Glide.with(this).load(R.drawable.usericon).into(userimage);

        // 파베 인스턴스 가져오기
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }

        // 현재 로그인 이메일
        loginEmail = mAuth.getCurrentUser().getEmail();

        tvemail=findViewById(R.id.mypage_email);
        tvemail.setText("이메일: "+ loginEmail);

         farm = findViewById(R.id.farm);
         making = findViewById(R.id.making);
         food = findViewById(R.id.food);
         tradition2 = findViewById(R.id.tradition2);
         nature2 = findViewById(R.id.nature2);
         health = findViewById(R.id.health);
         life = findViewById(R.id.life);
         etc = findViewById(R.id.etc);

        // 데이터베이스에서 체크박스 상태 가져오기
        // 제이슨 객체 생성
        String myJSON = "";
        JSONArray checks = null;
        JsonParser jsonParser= new JsonParser("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/mypage_select.php?email="+loginEmail);
        try {
            myJSON = jsonParser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject jsonObj = null;
        checkedList = new ArrayList<Integer>();
        try {
            jsonObj = new JSONObject(myJSON);
            checks = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < checks.length(); i++) {

                JSONObject c = checks.getJSONObject(i);

                checkedList.add(0, c.getInt("farm"));
                checkedList.add(1, c.getInt("making"));
                checkedList.add(2, c.getInt("food"));
                checkedList.add(3, c.getInt("tradition"));
                checkedList.add(4, c.getInt("nature"));
                checkedList.add(5, c.getInt("health"));
                checkedList.add(6, c.getInt("life"));
                checkedList.add(7, c.getInt("etc"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(checkedList.size() > 0 ) {

            if(checkedList.get(0)==1)
                farm.setChecked(true);
            else
                farm.setChecked(false);

            if(checkedList.get(1)==1)
                making.setChecked(true);
            else
                making.setChecked(false);

            if(checkedList.get(2)==1)
                food.setChecked(true);
            else
                food.setChecked(false);

            if(checkedList.get(3)==1)
                tradition2.setChecked(true);
            else
                tradition2.setChecked(false);

            if(checkedList.get(4)==1)
                nature2.setChecked(true);
            else
                nature2.setChecked(false);

            if(checkedList.get(5)==1)
                health.setChecked(true);
            else
                health.setChecked(false);

            if(checkedList.get(6)==1)
                life.setChecked(true);
            else
                life.setChecked(false);

            if(checkedList.get(7)==1)
                etc.setChecked(true);
            else
                etc.setChecked(false);
        }

         edit_btn = findViewById(R.id.edit_btn2);
         edit_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 ArrayList<Integer> checkedList2 = new ArrayList<>();

                 if(farm.isChecked())
                     checkedList2.add(0, 1);
                 else
                     checkedList2.add(0, 0);

                 if(making.isChecked())
                     checkedList2.add(1, 1);
                 else
                     checkedList2.add(1, 0);

                 if(food.isChecked())
                     checkedList2.add(2, 1);
                 else
                     checkedList2.add(2, 0);

                 if(tradition2.isChecked())
                     checkedList2.add(3, 1);
                 else
                     checkedList2.add(3, 0);

                 if(nature2.isChecked())
                     checkedList2.add(4, 1);
                 else
                     checkedList2.add(4, 0);

                 if(health.isChecked())
                     checkedList2.add(5, 1);
                 else
                     checkedList2.add(5, 0);

                 if(life.isChecked())
                     checkedList2.add(6, 1);
                 else
                     checkedList2.add(6, 0);

                 if(etc.isChecked())
                     checkedList2.add(7, 1);
                 else
                     checkedList2.add(7, 0);

                 String url = "http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/mypage_insert.php?email=" + loginEmail + "&fam_act=" + checkedList2.get(0)
                         + "&mak_act=" + checkedList2.get(1) + "&foo_act=" + checkedList2.get(2) + "&tra_act=" + checkedList2.get(3) + "&nat_act=" + checkedList2.get(4) + "&hea_act="
                         + checkedList2.get(5) + "&lif_act=" + checkedList2.get(6) + "&etc_act=" + checkedList2.get(7);
                 NetworkTask networkTask = new NetworkTask(url);
                 networkTask.execute();
                  Log.d("수정", url);
                 Toast.makeText(getApplicationContext(), "취향 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
             }
         });

        setNavBtnListener();
    }

    public void btnlogoutclick(View view) {//로그아웃 버튼
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
