package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.shared_prefs.SharedPreferencesUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class SettingsActivity extends BaseActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    SettingsActivity mContext;
    UserBean user;

    // class variables
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    @BindView(R.id.person_setting_avatar_imageView)
    ImageView mPersonSettingAvatarImageView;
    @BindView(R.id.person_setting_account_textView)
    TextView mPersonSettingAccountTextView;
    @BindView(R.id.person_setting_nick_textView)
    TextView mPersonSettingNickTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mContext = SettingsActivity.this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(getResources().getString(R.string.now_refresh));
        dialog.show();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)
                    , mContext, mPersonSettingAvatarImageView);
            mPersonSettingAccountTextView.setText(user.getMuserName());
            mPersonSettingNickTextView.setText(user.getMuserNick());
            dialog.dismiss();
        } else {
            finish();
        }
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.person_info));

    }

    @OnClick({R.id.person_setting_avatar_layout, R.id.person_setting_account_layout, R.id.person_setting_nick_layout, R.id.person_setting_quit_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_setting_avatar_layout:
                initImageUtils();
                break;
            case R.id.person_setting_account_layout:
                CommonUtils.showShortToast(R.string.account_not_change);
                break;
            case R.id.person_setting_nick_layout:
                // 解析布局
                View layout = LayoutInflater.from(mContext).inflate(R.layout.nick_changed, null);
                final EditText ed = (EditText) layout.findViewById(R.id.change_nick_editText);
                ed.setText(user.getMuserNick());
                ed.setSelectAllOnFocus(true);

                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.change_nick_title)
                        .setView(layout)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nick = ed.getText().toString().trim();
                                if (TextUtils.isEmpty(ed.getText())) {
                                    CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
                                } else if (nick.equals(user.getMuserNick())) {
                                    CommonUtils.showShortToast(R.string.nick_no_change);
                                } else {
                                    // 更新昵称的方法
                                    L.e(TAG, "editText:" + ed.getText().toString());
                                    updateNick(ed.getText().toString());

                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.person_setting_quit_button:
                L.e(TAG,"quit_button");
                // 清除首选项中的数据
                SharedPreferencesUtils.getInstance(mContext).removeUser();
                // 清楚内存中的用户数据
                FuLiCenterApplication.setUser(null);
                MFGT.gotoLoginActivity(mContext);
                finish();
                break;
        }
    }

    private void initImageUtils() {
        // start multiple photos selector
        Intent intent = new Intent(SettingsActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void updateNick(String newNick) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.load));
        pd.show();
        NetDao.updateNick(mContext, user.getMuserName(), newNick
                , new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String json) {
                        L.e(TAG, json);
                        Result result = ResultUtils.getResultFromJson(json, UserBean.class);
                        if (result == null) {
                            CommonUtils.showLongToast(R.string.update_fail);
                        } else if (result.isRetMsg()) {
                            user = (UserBean) result.getRetData();
                            L.e(TAG, "user:" + user);
                            FuLiCenterApplication.setUser(user);
                            L.e(TAG, "userNick:" + user.getMuserNick());
                            // 修改昵称显示
                            mPersonSettingNickTextView.setText(user.getMuserNick());
                        } else {
                            if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                                CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
                            } else if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                CommonUtils.showLongToast(R.string.update_fail);
                            } else {
                                CommonUtils.showLongToast(R.string.update_fail);
                            }
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
    }

}
