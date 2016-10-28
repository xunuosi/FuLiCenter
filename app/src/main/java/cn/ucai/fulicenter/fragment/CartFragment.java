package cn.ucai.fulicenter.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.UserBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class CartFragment extends BaseFragment {
    private static final String TAG = CartFragment.class.getSimpleName();

    @BindView(R.id.newgoods_refresh_text_view)
    TextView mNewgoodsRefreshTextView;
    @BindView(R.id.newgoods_recycler_view)
    RecyclerView mNewgoodsRecyclerView;
    @BindView(R.id.newgoods_srl)
    SwipeRefreshLayout mNewgoodsSrl;

    LinearLayoutManager mLinearLayoutManager;
    MainActivity mContext;
    ArrayList<CartBean> mList;
    CartAdapter mAdapter;
    UserBean user = null;
    @BindView(R.id.blank_textView)
    TextView mBlankTextView;
    @BindView(R.id.total_textView)
    TextView mTotalTextView;
    @BindView(R.id.save_textView)
    TextView mSaveTextView;
    @BindView(R.id.cart_fragment_settlement_layout)
    RelativeLayout mCartFragmentSettlementLayout;

    UpdateCartReceiver mReceiver;
    StringBuilder sb;// 用于拼接多个购物车id

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 复用新品的Fragment
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        mReceiver = new UpdateCartReceiver();
        mContext.registerReceiver(mReceiver, filter);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {
        pullDownListener();
    }


    private void pullDownListener() {
        mNewgoodsSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewgoodsSrl.setRefreshing(true);
                mNewgoodsRefreshTextView.setVisibility(View.VISIBLE);
                downLoadData2();
            }
        });
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            //downLoadData();
            downLoadData2();
        }
    }

    private void downLoadData() {
        NetDao.findCarts(mContext, user.getMuserName(),
                new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        // 获取到数据后修正SwipRefresh的状态
                        mNewgoodsSrl.setRefreshing(false);
                        mNewgoodsRefreshTextView.setVisibility(View.GONE);
                        if (result != null && result.length > 0) {
                            ArrayList<CartBean> mList =
                                    ConvertUtils.array2List(result);
                            mAdapter.initList(mList);
                        }

                    }

                    @Override
                    public void onError(String error) {
                        mNewgoodsRefreshTextView.setVisibility(View.GONE);
                        mNewgoodsSrl.setRefreshing(false);
                        CommonUtils.showLongToast(I.INTERNET_ERROR);
                        L.e("ERROR:" + error);
                    }
                });
    }

    private void downLoadData2() {
        NetDao.findCarts2(mContext, user.getMuserName(),
                new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String json) {
                        // 获取到数据后修正SwipRefresh的状态
                        mNewgoodsSrl.setRefreshing(false);
                        mNewgoodsRefreshTextView.setVisibility(View.GONE);
                        if (json != null && json.length() > 0) {
                            ArrayList<CartBean> list = ResultUtils
                                    .getListCartBeanFromJson(json);
                            mList = list;
                            mAdapter.initList(mList);
                            if (list == null || list.size() == 0) {
                                changeView(false);
                                return;
                            }
                            changeView(true);
                        } else {
                            changeView(false);
                            CommonUtils.showShortToast(R.string.internet_error);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        changeView(false);
                        mNewgoodsRefreshTextView.setVisibility(View.GONE);
                        mNewgoodsSrl.setRefreshing(false);
                        CommonUtils.showLongToast(I.INTERNET_ERROR);
                        L.e("ERROR:" + error);
                    }
                });
    }

    @Override
    protected void initView() {
        // 设置SwipeRefreshLayout刷新样式
        mNewgoodsSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)

        );

        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mNewgoodsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mList);
        mNewgoodsRecyclerView.setAdapter(mAdapter);
        // 是否自动修复大小
        mNewgoodsRecyclerView.setHasFixedSize(true);
        // 设置Item之间的边距 上下左右边距都12px
        mNewgoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        changeView(false);
    }

    /**
     * 根据购物车情况显示视图的方法
     * @param hasCart
     */
    private void changeView(boolean hasCart) {
        mBlankTextView.setVisibility(hasCart ? View.GONE : View.VISIBLE);
        mCartFragmentSettlementLayout.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        mNewgoodsRecyclerView.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        settlementAccount();
    }

    /**
     * 计算合计的方法
     */
    private void settlementAccount() {
        double sumPrice = 0;
        double savePrice = 0;
        sb = new StringBuilder();
        if (mList != null && mList.size() > 0) {
            for (CartBean bean : mList) {
                if (bean.isChecked()) {
                    sb.append(bean.getId() + ",");
                    sumPrice += getPrice(bean.getGoods().getCurrencyPrice()) * bean.getCount();
                    savePrice += getPrice(bean.getGoods().getRankPrice()) * bean.getCount();
                }
            }
            L.e(TAG, "sumPrice:" + sumPrice + ",savePrice:" + savePrice);
            mTotalTextView.setText("合计:" + String.valueOf(sumPrice) + "￥");
            mSaveTextView.setText("节省:" + String.valueOf(savePrice) + "￥");
        } else {
            L.e(TAG, "sumPrice:" + sumPrice + ",savePrice:" + savePrice);
            mTotalTextView.setText("合计:" + String.valueOf(sumPrice) + "￥");
            mSaveTextView.setText("节省:" + String.valueOf(savePrice) + "￥");
        }

    }

    /**
     * 将价格字符串转换成数字
     * @param price
     */
    private double getPrice(String price) {
        L.e("xns", price.substring(price.indexOf("￥") + 1));
        return Double.valueOf(price.substring(price.indexOf("￥") + 1));
    }
    @OnClick(R.id.settlement_button)
    public void onClickSettleAccount() {
        // 判断是否有商品被选中
        if (sb != null && sb.length() > 0) {
            MFGT.gotoPayActivity(mContext, sb.toString());
        } else {
            CommonUtils.showShortToast(R.string.noGoods_choosed);
        }
    }

    class UpdateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            settlementAccount();
            if (mList == null || mList.size() == 0) {
                changeView(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
