package cn.ucai.fulicenter;

import android.app.Application;


public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication application;

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
