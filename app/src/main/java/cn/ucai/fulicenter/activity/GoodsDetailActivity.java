package cn.ucai.fulicenter.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    }
}
