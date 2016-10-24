package cn.ucai.fulicenter.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.CatChildFilterButton;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class CategoryChildActivity extends BaseActivity {

    int catId;
    String groupName;
    ArrayList<CategoryChildBean> mChildBeanList;
    NewGoodsAdapter mNewGoodsAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager gridLayoutManager;

    private int mPageId = 1;
    private boolean priceAsc = false;
    private boolean timeAsc = false;// 默认上架时间降序排序

    @BindView(R.id.btnCatChildFilter)
    CatChildFilterButton mBtnCatChildFilter;
    @BindView(R.id.title_back_imageView)
    ImageView mTitleBackImageView;
    @BindView(R.id.category_child_activity_price)
    Button mCategoryChildActivityPrice;
    @BindView(R.id.category_child_activity_time)
    Button mCategoryChildActivityTime;
    @BindView(R.id.newgoods_refresh_text_view)
    TextView mNewgoodsRefreshTextView;
    @BindView(R.id.newgoods_recycler_view)
    RecyclerView mNewgoodsRecyclerView;
    @BindView(R.id.newgoods_srl)
    SwipeRefreshLayout mNewgoodsSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        if (catId == 0) {
            finish();
        }
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        mChildBeanList = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        L.e("xns",mChildBeanList.size()+"");

        // 初始化筛选器
        mBtnCatChildFilter.setOnCatFilterClickListener(groupName, mChildBeanList);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
        pullDownListener();
        pullUpListener();
    }

    private void pullUpListener() {
        mNewgoodsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * 滑动状态改变时回调的方法
             * @param recyclerView
             * @param newState
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 记录当前数据的位置最多为11 数据请求+页脚
                int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                if (lastPosition >= mNewGoodsAdapter.getItemCount() - 1
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mNewGoodsAdapter.isMore()) {
                    mPageId++;
                    downLoadData(I.ACTION_PULL_UP);
                }
            }

            /**
             * 正在被滑动时回调的方法
             * @param recyclerView
             * @param dx
             * @param dy
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void pullDownListener() {
        mNewgoodsSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewgoodsSrl.setRefreshing(true);
                mNewgoodsRefreshTextView.setVisibility(View.VISIBLE);
                mPageId = 1;
                downLoadData(I.ACTION_PULL_DOWN);
            }
        });
    }

    @Override
    protected void initData() {
        downLoadData(I.ACTION_DOWNLOAD);
    }

    private void downLoadData(final int action) {
        NetDao.findGoodsDetails(this, catId, mPageId,
                new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        mNewGoodsAdapter.setMore(true);
                        // 获取到数据后修正SwipRefresh的状态
                        mNewgoodsSrl.setRefreshing(false);
                        mNewgoodsRefreshTextView.setVisibility(View.GONE);
                        if (result != null && result.length > 0) {
                            ArrayList<NewGoodsBean> newGoodsBeanArrayList =
                                    ConvertUtils.array2List(result);
                            // 如果每次请求数据小于10条就设置没有更多
                            mNewGoodsAdapter.setMore
                                    (newGoodsBeanArrayList.size() >= I.PAGE_SIZE_DEFAULT);
                            if (action != I.ACTION_PULL_UP) {
                                mNewGoodsAdapter.initList(newGoodsBeanArrayList);
                            } else {
                                mNewGoodsAdapter.addList(newGoodsBeanArrayList);
                            }

                        } else {
                            mNewGoodsAdapter.setMore(false);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        mNewgoodsRefreshTextView.setVisibility(View.GONE);
                        mNewgoodsSrl.setRefreshing(false);
                        mNewGoodsAdapter.setMore(false);
                        CommonUtils.showLongToast("网络访问异常，请检查网络设置。");
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
        // 新建一个GridLayoutManager设置列数为2列
        gridLayoutManager =
                new GridLayoutManager(this, I.COLUM_NUM);
        mNewgoodsRecyclerView.setLayoutManager(gridLayoutManager);
        mList = new ArrayList<>();
        mNewGoodsAdapter = new NewGoodsAdapter(mList, this);
        mNewgoodsRecyclerView.setAdapter(mNewGoodsAdapter);
        // 是否自动修复大小
        mNewgoodsRecyclerView.setHasFixedSize(true);
        // 设置Item之间的边距 上下左右边距都12px
        mNewgoodsRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
    }

    // 设置上方回退键的监听
    @OnClick(R.id.title_back_imageView)
    public void onBackIamgeViewClick() {
        MFGT.finish(this);
    }

    @OnClick({R.id.category_child_activity_price, R.id.category_child_activity_time})
    public void onClick(View view) {
        // 获取箭头的对象
        Drawable arrow;
        switch (view.getId()) {
            case R.id.category_child_activity_price:
                if (priceAsc) {
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_up);
                    mNewGoodsAdapter.setSortBy(I.SORT_BY_PRICE_ASC);
                    // 前两个参数为控件左上角的坐标,后两个参数为控件的宽度和高度
                    // getIntrinsicWidth()返回固有的宽度
                    arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                    // 设置控件上下左右出现的图标
                    mCategoryChildActivityPrice
                            .setCompoundDrawablesWithIntrinsicBounds(null, null, arrow, null);
                } else {
                    // 得到向下箭头
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_down);
                    arrow.setBounds(0, 0
                            , arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                    mCategoryChildActivityPrice
                            .setCompoundDrawablesWithIntrinsicBounds(null, null, arrow, null);
                    mNewGoodsAdapter.setSortBy(I.SORT_BY_PRICE_DESC);
                }
                // 每次点击都要取反更新标志变量
                priceAsc = !priceAsc;
                break;
            case R.id.category_child_activity_time:
                if (timeAsc) {
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_up);
                    arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                    mCategoryChildActivityTime
                            .setCompoundDrawablesWithIntrinsicBounds(null, null, arrow, null);
                    mNewGoodsAdapter.setSortBy(I.SORT_BY_ADDTIME_ASC);
                } else {
                    arrow = getResources().getDrawable(R.mipmap.arrow_order_down);
                    arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                    mCategoryChildActivityTime
                            .setCompoundDrawablesWithIntrinsicBounds(null, null, arrow, null);
                    mNewGoodsAdapter.setSortBy(I.SORT_BY_ADDTIME_DESC);
                }
                timeAsc = !timeAsc;
                break;
        }
    }
}
