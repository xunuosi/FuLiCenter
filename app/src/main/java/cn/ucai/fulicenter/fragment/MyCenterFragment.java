package cn.ucai.fulicenter.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

public class MyCenterFragment extends BaseFragment {


    Context mContext;
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;

    public MyCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_center, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, layout);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

        UserBean user = FuLiCenterApplication.getUser();
        if (user != null) {
            mTvUserName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)
                    , mContext, mIvUserAvatar);

        } else {
            MFGT.gotoMainActivity((Activity) mContext);
        }
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tv_center_settings, R.id.iv_user_avatar, R.id.tv_user_name, R.id.iv_user_qrcode})
    public void onClick() {
        MFGT.gotoSettingActivity(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新回到界面刷新数据
        initData();
    }
}
