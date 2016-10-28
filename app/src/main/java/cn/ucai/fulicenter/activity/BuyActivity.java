package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;


public class BuyActivity extends BaseActivity implements PaymentHandler {
    private static final String TAG = BuyActivity.class.getSimpleName();
    private static String URL = "http://218.244.151.190/demo/charge";

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
    double savePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        mContext = BuyActivity.this;
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        L.e(TAG, "cartIds:" + cartIds);
        super.onCreate(savedInstanceState);
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
        savePrice = 0;
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
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 计算总金额（以分为单位）
        int amount = (int) (savePrice * 100);

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            /**
             * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
             * error_msg：支付结果信息
             */
            int code = data.getExtras().getInt("code");
            L.e(TAG, "code:" + code);
            if (code == 1) {
                CommonUtils.showShortToast(R.string.pingpp_title_activity_pay_sucessed);
                paySuccess();
            } else if (code == -1) {
                CommonUtils.showShortToast(getString(R.string.pingpp_pay_failed));
                finish();
            }
        }
    }

    private void paySuccess() {
        // 支付成功删除购物车的信息
        for (int i=0;i<cartIdArr.length;i++) {
            NetDao.deleteCart(mContext, Integer.valueOf(cartIdArr[i])
                    , new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            L.e(TAG,"result"+result);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
            finish();
        }
    }
}
