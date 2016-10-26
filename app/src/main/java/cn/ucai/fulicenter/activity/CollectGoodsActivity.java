package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.views.DisplayUtils;

public class CollectGoodsActivity extends BaseActivity {
    private static final String TAG = CollectGoodsActivity.class.getSimpleName();

    CollectGoodsActivity mContext;
    @BindView(R.id.white_title_image_relativlayout)
    RelativeLayout mWhiteTitleImageRelativlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collect_goods);
        ButterKnife.bind(this);
        mContext = CollectGoodsActivity.this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.collect_title));
    }
}
