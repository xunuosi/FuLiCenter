package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.DisplayUtils;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_account_editText)
    EditText mLoginAccountEditText;
    @BindView(R.id.login_password_editText)
    EditText mLoginPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
        DisplayUtils.initBackWithTitle(this,"账户登录");
    }

    @OnClick({R.id.login_button, R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                break;
            case R.id.register_button:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }


}
