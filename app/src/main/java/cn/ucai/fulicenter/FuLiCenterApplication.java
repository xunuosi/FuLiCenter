package cn.ucai.fulicenter;

import android.app.Application;


public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication application;
    public static String username;// 设置全局变量用于判断用户是否登录

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

    public static FuLiCenterApplication getInstance() {
       return FuLiInstance.instance;
    }
}
