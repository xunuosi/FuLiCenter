package cn.ucai.fulicenter;

import android.app.Application;

import cn.ucai.fulicenter.bean.UserBean;


public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication application;
    public static UserBean user;// 设置全局变量用于判断用户是否登录

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public FuLiCenterApplication() {

    }

    private static class FuLiInstance{
        static final FuLiCenterApplication instance = new FuLiCenterApplication();
    }

    public static UserBean getUser() {
        return user;
    }

    public static void setUser(UserBean user) {
        FuLiCenterApplication.user = user;
    }

    public static FuLiCenterApplication getInstance() {
       return FuLiInstance.instance;
    }
}
