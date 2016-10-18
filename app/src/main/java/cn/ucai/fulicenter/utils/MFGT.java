package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;


public class MFGT {
    /**
     * 补间动画关闭当前activity
     * @param activity
     */
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    /**
     * 跳转开启的界面
     * @param context
     */
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }

    /**
     * 重写开启界面的方法
     * @param context
     * @param cls
     */
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    /**
     * 重写开启界面的方法
     * @param context
     * @param cls
     * @param mIntent
     */
    public static void startActivity(Activity context,Class<?> cls,Intent mIntent){
        Intent intent = mIntent;
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
