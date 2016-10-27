package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.utils.MFGT;


public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CartBean> mList;
    Context mContext;

    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new CartItemViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_cart, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartBean bean = mList.get(position);

            /*((CartItemViewHolder) holder).mBoutiqueTitleTextView
                    .setText(boutiqueBean.getTitle());
            ((CartItemViewHolder) holder).mBoutiqueNameTextView
                    .setText(boutiqueBean.getName());
            ((CartItemViewHolder) holder).mBoutiqueDescriptionTextView
                    .setText(boutiqueBean.getDescription());

            ImageLoader.downloadImg(mContext,
                    ((CartItemViewHolder) holder).mBoutiqueImageView
                    , boutiqueBean.getImageurl());
            // 将解析出的每个CartBean存放在父容器的Tag属性中
            ((CartItemViewHolder) holder).mBoutiqueContentLayout
                    .setTag(boutiqueBean);*/
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void initList(ArrayList<CartBean> newList) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(newList);
        notifyDataSetChanged();
    }
    
    class CartItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cart_item_checkBox)
        CheckBox mCartItemCheckBox;
        @BindView(R.id.cart_item_goods_imageView)
        ImageView mCartItemGoodsImageView;
        @BindView(R.id.cart_item_goodsTitle_textView)
        TextView mCartItemGoodsTitleTextView;
        @BindView(R.id.cart_item_add_imageView)
        ImageView mCartItemAddImageView;
        @BindView(R.id.cart_item_count_textView)
        TextView mCartItemCountTextView;
        @BindView(R.id.cart_item_del_imageView)
        ImageView mCartItemDelImageView;
        @BindView(R.id.cart_item_goodsPrice_textView)
        TextView mCartItemGoodsPriceTextView;
        @BindView(R.id.cart_item_layout)
        RelativeLayout mCartItemLayout;

        CartItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}