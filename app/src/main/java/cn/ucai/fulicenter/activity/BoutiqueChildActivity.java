package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.dao.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class BoutiqueChildActivity extends BaseActivity {
    BoutiqueBean mBean;
    @BindView(R.id.newgoods_refresh_text_view)
    TextView mNewgoodsRefreshTextView;
    @BindView(R.id.title_show_message_textView)
    TextView mTitleShowMessageTextView;
    @BindView(R.id.newgoods_recycler_view)
    RecyclerView mNewgoodsRecyclerView;
    @BindView(R.id.newgoods_srl)
    SwipeRefreshLayout mNewgoodsSrl;
    @BindView(R.id.title_back_imageView)
    ImageView mTitleBackImageView;

    NewGoodsAdapter mNewGoodsAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager gridLayoutManager;
    int mPageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        // 得到传递的对象
        mBean = (BoutiqueBean) getIntent().getSerializableExtra(I.Boutique.CAT_ID);
        L.e("BoutiqueChildActivity:" + mBean.getId());
        if (mBean == null) {
            finish();
        }
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
        NetDao.downloadNewGoods(this, mBean.getId(),mPageId,
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
                        L.e("ERROR:"+error);
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
}
