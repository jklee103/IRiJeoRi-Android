package com.threechon.yeonwookang0702;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {
    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    private static final String TAG="signin";
    // 로그인 테스트를 위한 샘플 계정
    public final String sampleId = "tester";
    public final String samplePw = "1234";

    private String userId;
    private String userPw;

    private EditText idEditText;
    private EditText pwEditText;
    private Button signinBtn;
    private Button signupBtn;
    private FirebaseAuth mAuth;

    private TextView passTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ImageView imageView = findViewById(R.id.icon1);
        Glide.with(this).load(R.drawable.icon_splash).into(imageView);

        ImageView imageView2 = findViewById(R.id.icon2);
        Glide.with(this).load(R.drawable.logo).into(imageView2);

        mAuth = FirebaseAuth.getInstance();//파베 인증
        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }

        idEditText = findViewById(R.id.idEditTxt);
        pwEditText = findViewById(R.id.pwEditTxt);

        signinBtn = findViewById(R.id.signinBtn);
        signupBtn = findViewById(R.id.signupBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = idEditText.getText().toString();
                userPw = pwEditText.getText().toString();

                if(userId.equals("") || userId == null) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if(userPw.equals("") || userPw == null) {
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(userId.equals("") || userId == null) {
                        Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();

                    } else {
                        signin(userId,userPw);
                        if(mAuth.getCurrentUser()!=null){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }
                    }

                }

//                if(!userId.equals("")) {
//
//                    if(!userPw.equals("")) {
//
//                        if(userId.equals(sampleId)) {
//
//                            if(userPw.equals(samplePw)) {
//
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                setResult(Activity.RESULT_OK,intent);
//                                finish();
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
//                            }
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), "가입된 정보가 없는 이메일입니다.", Toast.LENGTH_LONG).show();
//                            //Log.d("UserId: ", userId);
//                            //Log.d("SampleId: ",sampleId);
//                        }
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_LONG).show();
//                    }
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "이메일 입력해주세요.", Toast.LENGTH_LONG).show();
//                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        passTxt = findViewById(R.id.findIdTxt);
        passTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickfind();
            }
        });

    }

    private void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(getApplicationContext(),"로그인 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                        // ...
                    }
                });
    }

    void clickfind(){ //비밀번호 찾는 메일
        final AutoCompleteTextView email=new AutoCompleteTextView(this);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(email).setTitle("가입한 이메일 입력")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String emailaddress=String.valueOf(email.getText());
                        if(!emailaddress.equals("")){
                            FirebaseAuth auth=FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(emailaddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"전송 완료", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"전송 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(getApplicationContext(),"이메일 입력", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }


}
