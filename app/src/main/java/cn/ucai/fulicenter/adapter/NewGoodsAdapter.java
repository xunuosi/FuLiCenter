package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;


public class NewGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<NewGoodsBean> mNewGoodsBeanList;
    Context mContext;

    public NewGoodsAdapter(List<NewGoodsBean> newGoodsBeanList, Context context) {
        // 通常采用这种方式
        mNewGoodsBeanList = new ArrayList<>();
        mNewGoodsBeanList = newGoodsBeanList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_ITEM) {
            holder = new NewGoodsItemViewHolder
                    (View.inflate(mContext, R.layout.item_newgoods, null));
        }else {
            holder = new ItemFooterViewHolder
                    (View.inflate(mContext, R.layout.item_footer, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            return;
        }
        NewGoodsBean newGoodsBean = mNewGoodsBeanList.get(position);
        NewGoodsItemViewHolder newGoodsItemViewHolder = (NewGoodsItemViewHolder) holder;
        newGoodsItemViewHolder.bindView(newGoodsBean);

    }

    @Override
    public int getItemCount() {
        return mNewGoodsBeanList == null ? 1 : mNewGoodsBeanList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initList(ArrayList<NewGoodsBean> newGoodsBeanArrayList) {
        if (mNewGoodsBeanList != null) {
            mNewGoodsBeanList.clear();
        }
        mNewGoodsBeanList.addAll(newGoodsBeanArrayList);
        notifyDataSetChanged();
    }

    public class NewGoodsItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.newgoods_image_view)
        ImageView mNewgoodsImageView;
        @BindView(R.id.newgoods_title_text_view)
        TextView mNewgoodsTitleTextView;
        @BindView(R.id.newgoods_priece_text_view)
        TextView mNewgoodsPrieceTextView;

        NewGoodsItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(NewGoodsBean newGoodsBean) {
            mNewgoodsTitleTextView.setText(newGoodsBean.getGoodsName());
            mNewgoodsPrieceTextView.setText(newGoodsBean.getCurrencyPrice());
            // setImageView
            ImageLoader.downloadImg(mContext,mNewgoodsImageView,newGoodsBean.getGoodsThumb());
        }

    }

    private class ItemFooterViewHolder extends RecyclerView.ViewHolder {

        public ItemFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
