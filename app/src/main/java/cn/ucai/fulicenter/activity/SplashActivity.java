package cn.ucai.fulicenter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.shared_prefs.SharedPreferencesUtils;
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
                // 判断首选项是否读取到用户名
                String username = SharedPreferencesUtils.getInstance(mContext).getUser();
                L.e(TAG, "fulicenter,username=" + username);
                if (user == null && username != null) {
                    // 数据库中读取信息
                    UserDao dao = new UserDao(mContext);
                    user = dao.getUser(username);
                    L.e(TAG, "dataBase:" + user);
                    if (user != null) {
                        FuLiCenterApplication.setUser(user);
                    }
                }

                MFGT.gotoMainActivity(SplashActivity.this);
                finish();
            }
        }, splashTimes);
    }
}
