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
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.shared_prefs.SharedPreferencesUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.views.DisplayUtils;

public class SettingsActivity extends BaseActivity {
    SettingsActivity mContext;
    UserBean user;
    OnSetAvatarListener mAvatarListener;

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
        mContext = this;
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
                // 初始化上传头像的监听对象
                mAvatarListener = new OnSetAvatarListener(mContext,
                        R.id.activity_settings,
                        user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
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
                                    updateNick();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.person_setting_quit_button:
                // 清除首选项中的数据
                SharedPreferencesUtils.getInstance(mContext).removeUser();
                // 清楚内存中的用户数据
                FuLiCenterApplication.setUser(null);
                MFGT.gotoLoginActivity(mContext);
                finish();
                break;
        }
    }

    private void updateNick() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 初始化相片的框架对象
        if (resultCode != RESULT_OK) {
            return;
        }
        mAvatarListener.setAvatar(requestCode, data, mPersonSettingAvatarImageView);

        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    private void updateAvatar() {

    }
}
