package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.shared_prefs.SharedPreferencesUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.DisplayUtils;

public class SettingsActivity extends BaseActivity {
    SettingsActivity mContext;

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
        UserBean user = FuLiCenterApplication.getUser();
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
                break;
            case R.id.person_setting_account_layout:
                CommonUtils.showShortToast(R.string.account_not_change);
                break;
            case R.id.person_setting_nick_layout:
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.change_nick_title)
                        .setView(LayoutInflater.from(mContext).inflate(R.layout.nick_changed, null))
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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

}
