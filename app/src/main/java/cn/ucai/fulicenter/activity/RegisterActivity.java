package cn.ucai.fulicenter.activity;

import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        DisplayUtils.initBack(this);
        DisplayUtils.initBackWithTitle(this, "用户注册");
    }
}
