package cn.ucai.fulicenter.activity;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startMillis = System.currentTimeMillis();
                // 后台耗时操作
                long costMills = System.currentTimeMillis() - startMillis;
                if (costMills - splashTimes < 0) {
                    try {
                        // 如果耗时操作低于闪屏设置的时间线程等待
                        Thread.sleep(splashTimes - costMills);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 跳转到主界面
                MFGT.gotoMainActivity(SplashActivity.this);
                // 从activity栈中剔除闪屏界面
                MFGT.finish(SplashActivity.this);
            }
        }).start();
    }
}
