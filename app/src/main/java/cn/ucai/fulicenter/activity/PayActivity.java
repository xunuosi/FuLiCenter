package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.pay_name_editText)
    EditText mPayNameEditText;
    @BindView(R.id.pay_phone_editText)
    EditText mPayPhoneEditText;
    @BindView(R.id.pay_address_editText)
    EditText mPayAddressEditText;
    @BindView(R.id.pay_city_spinner)
    Spinner mPayCitySpinner;
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

    @OnClick(R.id.settlement_button)
    public void onClickPay() {
        String receiver = mPayNameEditText.getText().toString();
        String phone = mPayPhoneEditText.getText().toString();
        String city = mPayCitySpinner.getSelectedItem().toString();
        String address = mPayAddressEditText.getText().toString();

        if (TextUtils.isEmpty(receiver)) {
            mPayNameEditText.requestFocus();
            mPayNameEditText.setError(getString(R.string.receiver_name_isEmpty));
            return;
        } else if (TextUtils.isEmpty(phone)) {
            mPayPhoneEditText.requestFocus();
            mPayPhoneEditText.setError(getString(R.string.phone_isEmpty));
            return;
        } else if (!phone.matches("[\\d]{11}")) {
            mPayPhoneEditText.requestFocus();
            mPayPhoneEditText.setError(getString(R.string.phone_valid));
            return;
        } else if (TextUtils.isEmpty(city)) {
            CommonUtils.showShortToast(R.string.city_isEmpty);
            return;
        } else if (TextUtils.isEmpty(address)) {
            mPayAddressEditText.requestFocus();
            mPayAddressEditText.setError(getString(R.string.address_isEmpty));
            return;
        }
        toPay();
    }

    private void toPay() {
        L.e(TAG,"支付了");
    }
}
