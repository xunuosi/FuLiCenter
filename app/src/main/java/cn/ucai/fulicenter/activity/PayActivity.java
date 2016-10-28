package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class PayActivity extends BaseActivity {
    private static final String TAG = PayActivity.class.getSimpleName();

    String cartIds;
    @BindView(R.id.total_textView)
    TextView mTotalTextView;
    private Activity mContext;
    UserBean user;
    String[] cartIdArr;
    ArrayList<CartBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
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
        user = FuLiCenterApplication.getUser();
        cartIdArr = cartIds.split(",");
        if (user != null && cartIdArr.length > 0) {
            NetDao.findCarts2(mContext, user.getMuserName(),
                    new OkHttpUtils.OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String json) {
                            if (json != null && json.length() > 0) {
                                ArrayList<CartBean> list = ResultUtils
                                        .getListCartBeanFromJson(json);
                                mList = list;
                                settlementAccount();
                            } else {
                                CommonUtils.showShortToast(I.INTERNET_ERROR);
                                finish();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showShortToast(error);
                            finish();
                        }
                    });
        } else {
            finish();
        }
    }

    /**
     * 计算合计的方法
     */
    private void settlementAccount() {
        double savePrice = 0;
        for (CartBean bean : mList) {
            for (String carId : cartIdArr) {
                if (carId.equals(String.valueOf(bean.getId()))) {
                    savePrice += getPrice(bean.getGoods().getRankPrice()) * bean.getCount();
                }
            }
        }
        mTotalTextView.setText("合计:" + String.valueOf(savePrice) + "￥");
    }

    /**
     * 将价格字符串转换成数字
     *
     * @param price
     */
    private double getPrice(String price) {
        L.e("xns", price.substring(price.indexOf("￥") + 1));
        return Double.valueOf(price.substring(price.indexOf("￥") + 1));
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.pay_title));
    }
}
