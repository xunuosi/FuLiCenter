package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageLoader;

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    List<CategoryGroupBean> mGroupList;
    List<List<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context context, List<CategoryGroupBean> groupList
            , List<List<CategoryChildBean>> childList) {
        mContext = context;
        mGroupList = new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList = new ArrayList<>();
        mChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null
                ? mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition)
                : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null
                ? mChildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView
            , ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_categroy_group, parent, false);
            holder = new GroupViewHolder(convertView);
            // holder保存在父容器转换的View中的Tag属性中
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        // 绑定holder数据
        CategoryGroupBean gBean = getGroup(groupPosition);
        if (gBean != null) {
            holder.mCategoryGroupTextView.setText(gBean.getName());
            ImageLoader.downloadImg(mContext, holder.mCategoryGroupGoodsimage
                    , gBean.getImageUrl());
            holder.mCategoryGroupArrowimage.setImageResource(
                    isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild
            , View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_categroy_child, parent, false);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        CategoryChildBean cBean = getChild(groupPosition, childPosition);
        if (cBean != null) {

            holder.mCategoryChildTextView.setText(cBean.getName());
            ImageLoader.downloadImg(mContext, holder.mCategoryChildGoodsImage
                    , cBean.getImageUrl());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        @BindView(R.id.category_group_goodsimage)
        ImageView mCategoryGroupGoodsimage;
        @BindView(R.id.category_group_textView)
        TextView mCategoryGroupTextView;
        @BindView(R.id.category_group_arrowimage)
        ImageView mCategoryGroupArrowimage;
        @BindView(R.id.category_group_parent)
        RelativeLayout mCategoryGroupParent;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.category_child_goodsImage)
        ImageView mCategoryChildGoodsImage;
        @BindView(R.id.category_child_textView)
        TextView mCategoryChildTextView;
        @BindView(R.id.category_child_parent)
        RelativeLayout mCategoryChildParent;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
