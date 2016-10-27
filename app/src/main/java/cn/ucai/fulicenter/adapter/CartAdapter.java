package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;


public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CartBean> mList;
    Context mContext;

    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mList = list;
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
        final CartBean bean = mList.get(position);
        GoodsDetailsBean goodsBean = bean.getGoods();

        CartItemViewHolder viewHolder = (CartItemViewHolder) holder;
        ImageLoader.downloadImg(mContext, viewHolder.mCartItemGoodsImageView
                , goodsBean.getGoodsThumb());
        viewHolder.mCartItemGoodsTitleTextView.setText(goodsBean.getGoodsName());
        viewHolder.mCartItemGoodsPriceTextView.setText(goodsBean.getCurrencyPrice());
        viewHolder.mCartItemCountTextView.setText(String.valueOf(bean.getCount()));
        viewHolder.mCartItemCheckBox.setChecked(false);
        viewHolder.mCartItemCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // 改变购物车中数据的选中状态
                        bean.setChecked(isChecked);
                        // 发送广播提醒外部修改数据
                        mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                    }
                }
        );
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void initList(ArrayList<CartBean> newList) {
        mList = newList;
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