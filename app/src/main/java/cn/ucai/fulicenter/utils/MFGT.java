package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.activity.GoodsDetailActivity;
import cn.ucai.fulicenter.activity.LoginActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.RegisterActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;


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
    public static void startActivity(Context context, Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
    }

    /**
     * 重载开启界面方法
     * @param context
     * @param intent
     */
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 调转到商品详情页的方法
     * @param context
     * @param goodsId
     */
    public static void gotoGoodsActivity(Context context, int goodsId) {
        Intent intent = new Intent();
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId);
        intent.setClass(context, GoodsDetailActivity.class);
        startActivity(context,intent);
    }

    /**
     * 调转到精品二级页的方法
     * @param context
     * @param bean
     */
    public static void gotoBoutiqueChildActivity(Context context, BoutiqueBean bean) {
        Intent intent = new Intent();
        intent.putExtra(I.Boutique.CAT_ID, bean);
        intent.setClass(context, BoutiqueChildActivity.class);
        startActivity(context,intent);
    }

    /**
     * 调转到分类二级详情页的方法
     * @param context
     * @param catId
     * @param groupName
     * @param mList
     */
    public static void gotoCategoryChildActivity(Context context, int catId
            , String groupName, ArrayList<CategoryChildBean> mList) {
        Intent intent = new Intent();
        intent.putExtra(I.CategoryChild.CAT_ID, catId);
        intent.putExtra(I.CategoryGroup.NAME, groupName);
        intent.putExtra(I.CategoryChild.ID, mList);
        intent.setClass(context, CategoryChildActivity.class);
        startActivity(context, intent);
    }

    public static void gotoLoginActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        startActivityForResult((Activity) context,intent,I.REQUEST_CODE_LOGIN);
    }

    public static void gotoRegisterActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        startActivityForResult((Activity) context, intent, I.REQUEST_CODE_REGISTER);
    }

    public static void startActivityForResult(Activity context, Intent intent, int requesCode) {
        context.startActivityForResult(intent,requesCode);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
