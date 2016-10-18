package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.dao.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;

public class NewGoodsFragment extends Fragment {

    @BindView(R.id.newgoods_refresh_text_view)
    TextView mNewgoodsRefreshTextView;
    @BindView(R.id.newgoods_recycler_view)
    RecyclerView mNewgoodsRecyclerView;
    @BindView(R.id.newgoods_srl)

    SwipeRefreshLayout mNewgoodsSrl;
    NewGoodsAdapter mNewGoodsAdapter;
    ArrayList<NewGoodsBean> mList;
    private int mPageId = 1;

    public NewGoodsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);

        ButterKnife.bind(this, layout);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        NetDao.downloadNewGoods(getContext(), mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mNewGoodsAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> newGoodsBeanArrayList =
                            ConvertUtils.array2List(result);
                    // 如果每次请求数据小于10条就设置没有更多
                    mNewGoodsAdapter.setMore(newGoodsBeanArrayList.size() >= I.PAGE_SIZE_DEFAULT);
                    mNewGoodsAdapter.initList(newGoodsBeanArrayList);
                } else {
                    mNewGoodsAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast("网络访问异常，请检查网络设置。");
                L.e("ERROR:"+error);
            }
        });
    }

    private void initView() {
        // 设置SwipeRefreshLayout刷新样式
        mNewgoodsSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)

        );
        // 新建一个GridLayoutManager设置列数为2列
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(), I.COLUM_NUM);
        mNewgoodsRecyclerView.setLayoutManager(gridLayoutManager);
        mList = new ArrayList<>();
        mNewGoodsAdapter = new NewGoodsAdapter(mList, getContext());
        mNewgoodsRecyclerView.setAdapter(mNewGoodsAdapter);
        // 是否自动修复大小
        mNewgoodsRecyclerView.setHasFixedSize(true);
    }


}
