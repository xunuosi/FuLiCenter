package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.dao.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class CategoryFragment extends BaseFragment {

    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    MainActivity mContext;
    CategoryAdapter mAdapter;

    @BindView(R.id.category_fragment_parent)
    RelativeLayout mCategoryFragmentParent;
    @BindView(R.id.elv)
    ExpandableListView mElv;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {
        setOnClickExpandableListViewItemListener();
    }


    private void setOnClickExpandableListViewItemListener() {
        mElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v
                    , int groupPosition, int childPosition, long id) {

                CategoryGroupBean gBean = mAdapter.getGroup(groupPosition);
                CategoryChildBean cBean = mAdapter.getChild(groupPosition, childPosition);

                MFGT.gotoCategoryChildActivity(mContext, cBean.getId(), gBean.getName(),mChildList);
                return true;
            }
        });
    }


    @Override
    protected void initData() {
        downloadCategoryGroupData();
    }

    private void downloadCategoryGroupData() {
        NetDao.findCategoryGroup(mContext
                , new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {

                    @Override
                    public void onSuccess(CategoryGroupBean[] result) {
                        L.e("downloadCategoryGroupData(),result:" + result.length);
                        if (result != null && result.length > 0) {
                            ArrayList<CategoryGroupBean> gBeanList = ConvertUtils.array2List(result);
                            // 请求数据装入Adapter指定的数组中
                            if (mGroupList != null && mGroupList.size() > 0) {
                                mGroupList.clear();
                            }
                            mGroupList.addAll(gBeanList);
                            // 清理之前数据
                            mChildList.clear();
                            // 初始化指定数量的ChildList集合，为后续指定赋值做准备
                            // 每个父分类都需要加载小分类
                            for (int i = 0; i < mGroupList.size(); i++) {
                                mChildList.add(new ArrayList<CategoryChildBean>());
                                CategoryGroupBean gBean = mGroupList.get(i);
                                downloadCategoryChildData(gBean.getId(), i);
                            }

                        } else {
                            CommonUtils.showShortToast(I.INTERNET_ERROR);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("downloadCategoryGroupData()，ERROR:" + error);
                        CommonUtils.showShortToast(I.INTERNET_ERROR);
                    }
                });
    }

    private void downloadCategoryChildData(int parentId, final int groupIndex) {
        NetDao.findCategoryChildren(mContext, parentId
                , new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        L.e("downloadCategoryChildData(),result:" + result.length);
                        L.e("downloadCategoryChildData(),groupIndex:" + groupIndex);
                        if (result != null && result.length > 0) {
                            ArrayList<CategoryChildBean> cBeanList = ConvertUtils.array2List(result);
                            // 请求数据装入Adapter指定的数组中
                            mChildList.set(groupIndex, cBeanList);
                            if (groupIndex >= mGroupList.size() - 1) {
                                mAdapter.initList(mGroupList, mChildList);
                            }
                        } else {
                            CommonUtils.showShortToast(I.INTERNET_ERROR);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("downloadCategoryChildData()，ERROR:" + error);
                        CommonUtils.showShortToast(I.INTERNET_ERROR);
                    }
                });
    }

    @Override
    protected void initView() {
        mContext = (MainActivity) getContext();
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(mContext, mGroupList, mChildList);

        mElv.setAdapter(mAdapter);
        // 去除默认样式的箭头
        mElv.setGroupIndicator(null);

    }


}
