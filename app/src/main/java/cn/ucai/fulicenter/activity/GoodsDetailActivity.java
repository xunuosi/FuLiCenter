package cn.ucai.fulicenter.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.dao.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class GoodsDetailActivity extends AppCompatActivity {

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

    int goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("test:" + goodsId);
        initData();
    }

    private void initData() {
        NetDao.findGoodDetails(this, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    // 设置商品标题显示
                    mGoodsDetailGoodsEngTitleTextView.
                            setText(result.getGoodsEnglishName());
                    mGoodsDetailGoodsChiTitleTextView
                            .setText(result.getGoodsName());
                    mGoodsDetailShopPriceTextView
                            .setText(result.getShopPrice());
                    mGoodsDetailShopPriceTextView
                            .getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mGoodsDetailCuurentPriceTextView
                            .setText(result.getCurrencyPrice());

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
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.finish(this);
    }

    @OnClick(R.id.title_back_imageView)
    public void onClick() {
        MFGT.finish(this);
    }
}
