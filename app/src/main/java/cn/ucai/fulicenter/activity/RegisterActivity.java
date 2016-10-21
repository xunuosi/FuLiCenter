package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.dao.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {
    // 获取类名
    private static String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.register_account_editText)
    EditText mRegisterAccountEditText;
    @BindView(R.id.register_nick_editText)
    EditText mRegisterNickEditText;
    @BindView(R.id.register_password_editText)
    EditText mRegisterPasswordEditText;
    @BindView(R.id.register_repassword_editText)
    EditText mRegisterRepasswordEditText;

    String username;
    String password;
    String repassword;
    String nick;

    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
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
        DisplayUtils.initBackWithTitle(this, "用户注册");
    }

    @OnClick(R.id.register_button)
    public void checkData() {
        username = mRegisterAccountEditText.getText().toString().trim();
        password = mRegisterPasswordEditText.getText().toString().trim();
        repassword = mRegisterRepasswordEditText.getText().toString().trim();
        nick = mRegisterNickEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            mRegisterAccountEditText.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            CommonUtils.showShortToast(R.string.illegal_user_name);
            mRegisterAccountEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(nick)) {
            L.e(TAG + "NICK");
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            mRegisterNickEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            mRegisterPasswordEditText.requestFocus();
            return;
        }else if (TextUtils.isEmpty(repassword)) {
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            mRegisterRepasswordEditText.requestFocus();
            return;
        }else if (!password.equals(repassword)) {
            CommonUtils.showShortToast(R.string.two_input_password);
            mRegisterRepasswordEditText.requestFocus();
            return;
        }

        startRegister();
    }

    private void startRegister() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(getResources().getString(R.string.registering));
        dialog.show();
        NetDao.register(mContext, username, nick, password
                , new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        dialog.dismiss();
                        if (result.isRetMsg()) {
                            CommonUtils.showLongToast(R.string.register_success);
                            // 将注册成功的username返回给登录界面
                            setResult(RESULT_OK
                                    , new Intent().putExtra(I.User.USER_NAME, username));
                            MFGT.finish(mContext);
                        } else {
                            if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                                CommonUtils.showShortToast(R.string.register_fail_exists);
                                mRegisterAccountEditText.requestFocus();
                            } else {
                                CommonUtils.showShortToast(R.string.register_fail);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        dialog.dismiss();
                        L.e(TAG + ",ERROR:" + error);
                        CommonUtils.showLongToast("ERROR:"+error);
                    }
                });
    }
}
