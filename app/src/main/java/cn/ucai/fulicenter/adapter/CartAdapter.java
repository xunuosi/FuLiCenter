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
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;


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
        // 传入下标获取集合中的对象
        viewHolder.mCartItemLayout.setTag(position);
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

        @OnClick(R.id.cart_item_add_imageView)
        public void addCart() {
            final CartBean bean = mList.get((Integer) mCartItemLayout.getTag());
            if (bean != null) {
                final int count = bean.getCount() + 1;
                NetDao.updateCart(mContext, bean.getId(), count,
                        new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                // 如果服务器成功更新刷新集合中的数据
                                bean.setCount(count);
                                // 发送广播更新数据显示
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                                // 更新数据数量显示
                                mCartItemCountTextView.setText(String.valueOf(count));
                            }

                            @Override
                            public void onError(String error) {
                                CommonUtils.showShortToast(R.string.internet_error);
                            }
                        });
            }
        }

        @OnClick(R.id.cart_item_del_imageView)
        public void delCart() {
            final CartBean bean = mList.get((Integer) mCartItemLayout.getTag());
            if (bean != null && bean.getCount() > 1) {
                final int count = bean.getCount() - 1;
                NetDao.updateCart(mContext, bean.getId(), count,
                        new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                // 如果服务器成功更新刷新集合中的数据
                                bean.setCount(count);
                                // 发送广播更新数据显示
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                                // 更新数据数量显示
                                mCartItemCountTextView.setText(String.valueOf(count));
                            }

                            @Override
                            public void onError(String error) {
                                CommonUtils.showShortToast(R.string.internet_error);
                            }
                        });
            }
        }

        CartItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}