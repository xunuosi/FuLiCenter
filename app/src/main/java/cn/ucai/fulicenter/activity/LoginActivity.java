package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.login_account_editText)
    EditText mLoginAccountEditText;
    @BindView(R.id.login_password_editText)
    EditText mLoginPasswordEditText;

    String username;
    String password;
    LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
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
        DisplayUtils.initBackWithTitle(this,"账户登录");
    }

    @OnClick({R.id.login_button, R.id.register_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                checkLogin();
                break;
            case R.id.register_button:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }

    private void checkLogin() {
        username = mLoginAccountEditText.getText().toString().trim();
        password = mLoginPasswordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            CommonUtils.showShortToast(R.string.login_username_isempty);
            return;
        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            return;
        }
        login();
    }

    private void login() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.loging));
        pd.show();
        NetDao.login(mContext, username, password
                , new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String json) {
                        L.e(TAG, json);
                        Result result = ResultUtils.getResultFromJson(json, UserBean.class);
                        if (result.getRetData() != null) {
                            CommonUtils.showShortToast(R.string.login_success);
                            UserBean user = (UserBean) result.getRetData();
                            L.e(TAG, user.toString());
                            // 储存用户登录信息
                            UserDao dao = new UserDao(mContext);
                            boolean isSuccess = dao.addUser(user);
                            if (isSuccess) {
                                FuLiCenterApplication.setUser(user);
                                MFGT.finish(mContext);
                            } else {
                                CommonUtils.showShortToast(R.string.user_save_error);
                            }
                        } else if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                            CommonUtils.showShortToast(R.string.login_nouser);
                        } else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                            CommonUtils.showShortToast(R.string.login_password_error);
                        } else {
                            CommonUtils.showShortToast(R.string.login_fail);
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        L.e(TAG, "ERROR:" + error);
                        CommonUtils.showShortToast(error);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            } else {
                username = data.getStringExtra(I.User.USER_NAME);
                mLoginAccountEditText.setText(username);
                mLoginPasswordEditText.requestFocus();
            }
        }
    }
}
