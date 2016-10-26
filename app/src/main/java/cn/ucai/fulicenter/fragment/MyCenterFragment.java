package cn.ucai.fulicenter.fragment;


import android.app.Activity;
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
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

public class MyCenterFragment extends BaseFragment {


    Context mContext;
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;

    UserBean user;

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

        user = FuLiCenterApplication.getUser();
        if (user != null) {
            mTvUserName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)
                    , mContext, mIvUserAvatar);

        } else {
            // 没有得到用户继续登录一次
            MFGT.gotoLoginActivity(mContext);
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
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            mTvUserName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)
                    , mContext, mIvUserAvatar);
            // 再次回到用户中心界面刷新数据
            syncUserInfo();
        }
    }

    /**
     * 同步个人信息的方法
     */
    private void syncUserInfo() {
        NetDao.findUserByUserName(mContext, user.getMuserName()
                , new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String jsonStr) {
                        Result result = ResultUtils.getResultFromJson(jsonStr, UserBean.class);
                        if (result != null) {
                            UserBean u = (UserBean) result.getRetData();
                            // 判断如果用户有更新就进行同步
                            if (!u.equals(user)) {
                                // 更新用户数据库
                                UserDao dao = new UserDao(mContext);
                                boolean b = dao.addUser(u);
                                if (b) {
                                    // 更新内存中的用户
                                    FuLiCenterApplication.setUser(u);
                                    // 更新局部变量
                                    user = u;
                                    ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)
                                            , mContext, mIvUserAvatar);
                                    mTvUserName.setText(user.getMuserNick());
                                }

                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showShortToast("ERROR:" + error);
                    }
                });
    }
}
