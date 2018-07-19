package com.threechon.yeonwookang0702;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.splashRelative);
//        layout.setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.background_splash)));
        ImageView imageView2 = findViewById(R.id.imageView2);
        Glide.with(this).load(R.drawable.icon_splash).into(imageView2);
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(), 2400);
    }

    /* 로딩 화면 스플래시 핸들러 */
    private class SplashHandler implements Runnable {
        public void run() {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    /* 메모리 관리를 위한 리사이클 함수*/
    private void recycleView(View view) {
        if (view != null) {
            Drawable bg = view.getBackground();
            if (bg != null) {
                bg.setCallback(null);
                ((BitmapDrawable) bg).getBitmap().recycle();
                view.setBackgroundDrawable(null);
            }
        }
    }
}
