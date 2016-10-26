package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

public class GoodsDetailActivity extends BaseActivity {
    private static final String TAG = GoodsDetailActivity.class.getSimpleName();
    // 定义广播标志
    public static final String BROAD_CAST = "cn.ucai.fulicenter.activity.GoodsDetailActivity";
    // 传入广播的信息
    private static final int ADD_COLLECT = 1;
    private static final int DEL_COLLECT = 2;

    @BindView(R.id.title_back_imageView)
    ImageView mTitleBackImageView;
    @BindView(R.id.goodsDetail_goodsEngTitle_textView)
    TextView mGoodsDetailGoodsEngTitleTextView;
    @BindView(R.id.goodsDetail_goodsChiTitle_textView)
    TextView mGoodsDetailGoodsChiTitleTextView;
    @BindView(R.id.goodsDetail_shopPrice_textView)
    TextView mGoodsDetailShopPriceTextView;
    @BindView(R.id.goodsDetail_cuurentPrice_textView)
    TextView mGoodsDetailCuurentPriceTextView;
    @BindView(R.id.goodsDetail_showGoods_SlideAutoLoopView)
    SlideAutoLoopView mGoodsDetailShowGoodsSlideAutoLoopView;
    @BindView(R.id.goodsDetail_FlowIndicator)
    FlowIndicator mGoodsDetailFlowIndicator;
    @BindView(R.id.goodsDetail_description_webView)
    WebView mGoodsDetailDescriptionWebView;
    @BindView(R.id.goodsDetail_collect_imageView)
    ImageView mGoodsDetailCollectImageView;

    int goodsId;
    UserBean user;
    GoodsDetailActivity mContext;
    // 标志变量标识是否收藏了这个宝贝
    boolean isCollect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        mContext = GoodsDetailActivity.this;
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("test:" + goodsId);
        if (goodsId == 0) {
            finish();
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void initData() {
        NetDao.findGoodDetails(this, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodTitle(result);
                    recyclerShowImage(result);
                    showWebView(result);

                } else {
                    // 获取数据异常显示错误信息并关闭界面
                    Toast.makeText(GoodsDetailActivity.this,
                            I.INTERNET_ERROR, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                L.e("findGoodDetails,ERROR:" + error);
                Toast.makeText(GoodsDetailActivity.this,
                        I.INTERNET_ERROR, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void initView() {

    }

    private void showWebView(GoodsDetailsBean result) {
        // webView的数据显示
        mGoodsDetailDescriptionWebView
                .loadDataWithBaseURL(null, result.getGoodsBrief()
                        , I.TEXT_HTML, I.UTF_8, null);
    }

    private void recyclerShowImage(GoodsDetailsBean result) {
        // 设置轮播图片
        if (result.getProperties() != null
                && result.getProperties().length > 0) {
            // 得到轮播图片的数组
            AlbumsBean[] albums = result.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                mGoodsDetailShowGoodsSlideAutoLoopView
                        .startPlayLoop(mGoodsDetailFlowIndicator
                                , getAlbumImgUrl(albums)
                                , getAlbumImgCount(albums));
            }
        } else {
            // 获取数据异常显示错误信息并关闭界面
            Toast.makeText(GoodsDetailActivity.this,
                    I.INTERNET_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private int getAlbumImgCount(AlbumsBean[] albums) {
        return albums.length;
    }

    private String[] getAlbumImgUrl(AlbumsBean[] albums) {
        String[] urls = new String[]{};
        for (int i = 0; i < albums.length; i++) {
            urls = new String[albums.length];
            urls[i] = albums[i].getImgUrl();
        }
        return urls;
    }


    private void showGoodTitle(GoodsDetailsBean result) {
        // 设置商品标题显示
        mGoodsDetailGoodsEngTitleTextView.
                setText(result.getGoodsEnglishName());
        mGoodsDetailGoodsChiTitleTextView
                .setText(result.getGoodsName());
        mGoodsDetailShopPriceTextView
                .setText(result.getShopPrice());
        // 设置删除线
        mGoodsDetailShopPriceTextView
                .getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mGoodsDetailCuurentPriceTextView
                .setText(result.getCurrencyPrice());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.finish(this);
    }

    @OnClick(R.id.title_back_imageView)
    public void onClick() {
        MFGT.finish(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            checkCollectStatus();
        }
    }

    private void checkCollectStatus() {
        NetDao.isCollect(mContext, user.getMuserName(), goodsId,
                new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = true;
                            changeCollectStatus();
                        } else {
                            changeCollectStatus();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "ERROR:" + error);
                        CommonUtils.showShortToast(error);
                        changeCollectStatus();
                    }
                });
    }

    private void changeCollectStatus() {
        if (isCollect) {
            mGoodsDetailCollectImageView.setImageResource(R.mipmap.bg_collect_out);
        } else {
            mGoodsDetailCollectImageView.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.goodsDetail_collect_imageView)
    public void onClickChangeCollectStatus() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            // 调用检查方法改变标志变量
            checkCollectStatus();
            if (isCollect) {
                deleteCollectGoods();
            } else {
                addCollectGoods();
            }
        } else {
            MFGT.gotoLoginActivity(mContext);
        }
    }

    private void deleteCollectGoods() {
        NetDao.deleteCollect(mContext, user.getMuserName(), goodsId,
                new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = !isCollect;
                            // 根据状态改变显示
                            checkCollectStatus();
                            CommonUtils.showShortToast(R.string.delete_success);
                            // 发出广播用于正确显示收藏宝贝界面数据
                            Intent intent = new Intent(BROAD_CAST);
                            intent.putExtra(TAG, DEL_COLLECT);
                            // 发送广播
                            sendBroadcast(intent);
                        } else {
                            CommonUtils.showShortToast(R.string.delete_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "ERROR:" + error);
                        CommonUtils.showShortToast(error);
                    }
                });
    }

    private void addCollectGoods() {
        NetDao.addCollect(mContext, user.getMuserName(), goodsId,
                new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = !isCollect;
                            // 根据状态改变显示
                            checkCollectStatus();
                            CommonUtils.showShortToast(R.string.collect_success);
                            // 发出广播用于正确显示收藏宝贝界面数据
                            Intent intent = new Intent(BROAD_CAST);
                            intent.putExtra(TAG, ADD_COLLECT);
                            // 发送广播
                            sendBroadcast(intent);
                        } else {
                            CommonUtils.showShortToast(R.string.delete_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "ERROR:" + error);
                        CommonUtils.showShortToast(error);
                    }
                });
    }

    @OnClick(R.id.goodsDetail_share_imageView)
    public void onClickShare() {
        showShare();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
