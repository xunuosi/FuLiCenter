package cn.ucai.fulicenter;

import android.app.Application;


public class FuLiCenterApplication extends Application {

    public FuLiCenterApplication() {

    }

    private static class FuLiInstance{
        static final FuLiCenterApplication instance = new FuLiCenterApplication();
    }

    public static FuLiCenterApplication getInstance() {
       return FuLiInstance.instance;
    }
}
