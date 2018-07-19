package com.threechon.yeonwookang0702;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class SignupActivity extends AppCompatActivity {
    public static boolean isLogined = false; // 로그인 되었는지 체크 여부
    public final int SIGN_IN = 101; // 로그인 액티비티
    public final int MYPAGE = 102; // 메이페이지 액티비티

    // 네비게이션 바 메뉴 설정
    private ImageButton mytripBtn;
    private ImageButton scrapBtn;
    private ImageButton mainBtn;
    private ImageButton reviewBtn;
    private ImageButton mypageBtn;

    static  final String TAG="signup";
    TextView emailtxt;
    TextView pwtxt;
    TextView pwchecktxt;

    EditText id_signup;
    EditText pw_signup;
    EditText pwcheck;
    Button signupbtn;
    Button canclebtn;

    String email;
    String password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageView imageView = findViewById(R.id.logo3);
        Glide.with(this).load(R.drawable.icon_splash).into(imageView);

        ImageView imageView2 = findViewById(R.id.logo4);
        Glide.with(this).load(R.drawable.logo).into(imageView2);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            isLogined = true;
        } else {
            isLogined = false;
        }

        id_signup=findViewById(R.id.idEditTxt_signup);
        pw_signup=findViewById(R.id.pwEditTxt_signup);
        pwcheck=findViewById(R.id.pwCheckEditTxt_signup);
        signupbtn =findViewById(R.id.signupBtn_signup);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=id_signup.getText().toString();
                password=pw_signup.getText().toString();
                Log.d("signup", "btn click");
                if(email==null || email.equals(" ")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();

                }
                else if(password==null||password.equals(" ")) {
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if(password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "패스워드가 너무 짧습니다.", Toast.LENGTH_SHORT).show();

                } else {
                    if(password.equals(pwcheck.getText().toString())){
                            signup();
                            Log.d("signup", "signup");

                    }
                    else {
                        Log.d("signup", "reject");
                        Toast.makeText(getApplicationContext(), "정확한 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        canclebtn = findViewById(R.id.cancleBtn);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void signup(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            id_signup.setText("");
                            pw_signup.setText("");
                            pwcheck.setText("");
                            Toast.makeText(getApplicationContext(),"회원가입 처리가 완료되었습니다.\n로그인 해주세요.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"회원가입에 실패하였습니다.\n 네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
