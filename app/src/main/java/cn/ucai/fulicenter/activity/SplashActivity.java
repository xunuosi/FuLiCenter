package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private long splashTimes = 2000;
    private SplashActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 该方法执行指定时长的线程
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 如果内存中有user数据直接读取
                UserBean user = FuLiCenterApplication.getUser();
                if (user != null) {
                    L.e(TAG, "FuLiCenterApplication"+user);
                } else {// 否则数据库中读取数据
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser("xns1987");
                    L.e(TAG, "dataBase:"+user);
                }

                MFGT.gotoMainActivity(SplashActivity.this);
                finish();
            }
        }, splashTimes);
    }
}
