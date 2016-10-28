package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.views.DisplayUtils;

public class PayActivity extends BaseActivity {
    private static final String TAG = PayActivity.class.getSimpleName();

    String cartIds;
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        mContext = PayActivity.this;
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        L.e(TAG, "cartIds:" + cartIds);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.pay_title));
    }

    @Override
    protected void initView() {

    }
}
