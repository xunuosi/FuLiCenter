package cn.ucai.fulicenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    private long splashTimes = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 该方法执行指定时长的线程
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MFGT.gotoMainActivity(SplashActivity.this);
                finish();
            }
        }, splashTimes);
    }
}
