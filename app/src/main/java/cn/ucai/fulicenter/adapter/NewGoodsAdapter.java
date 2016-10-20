package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.ItemFooterViewHolder;


public class NewGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<NewGoodsBean> mNewGoodsBeanList;
    Context mContext;
    boolean isMore;
    int sortBy=I.SORT_BY_ADDTIME_ASC;// 默认按添加时间升序排序

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        listSort();
        notifyDataSetChanged();
    }

    /**
     * 重新按指定条件进行排序
     *
     * 返回值如果大于0就进行指定排序
     */
    private void listSort() {
        Collections.sort(mNewGoodsBeanList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result = 0;
                switch (sortBy) {
                    case I.SORT_BY_PRICE_ASC:
                        result= (int) (getPrice(left) - getPrice(right));
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = (int) (getPrice(right) - getPrice(left));
                        break;
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int)
                                (Long.valueOf(left.getAddTime()) - Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int)
                                (Long.valueOf(right.getAddTime()) - Long.valueOf(left.getAddTime()));
                        break;
                }
                return result;
            }
        });
    }

    /**
     * 将价格字符串转换成数字
     * @param bean
     */
    private double getPrice(NewGoodsBean bean) {
        String price = bean.getCurrencyPrice();
        L.e("xns", price.substring(price.indexOf("￥") + 1));
        return Double.valueOf(price.substring(price.indexOf("￥") + 1));
    }

    public NewGoodsAdapter(List<NewGoodsBean> newGoodsBeanList, Context context) {
        // 通常采用这种方式
        mNewGoodsBeanList = new ArrayList<>();
        mNewGoodsBeanList = newGoodsBeanList;
        mContext = context;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        // 每次设置是否有更多时都应提醒Adapter更新数据
        notifyDataSetChanged();
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
            ItemFooterViewHolder footerHolder = (ItemFooterViewHolder) holder;
            footerHolder.mFooterTextView.setText(getFooterStr());
            return;
        }
        NewGoodsBean newGoodsBean = mNewGoodsBeanList.get(position);
        NewGoodsItemViewHolder newGoodsItemViewHolder = (NewGoodsItemViewHolder) holder;
        newGoodsItemViewHolder.bindView(newGoodsBean);

        // 使用LinearLayout.tag属性保存被每个Item的商品ID值
        newGoodsItemViewHolder.mNewgoodsLinearLayout
                .setTag(newGoodsBean.getGoodsId());
    }

    /**
     * 设置页脚显示文字的方法
     * @return 返回字符串资源ID
     */
    private int getFooterStr() {
        return isMore()?R.string.load_more:R.string.no_more;
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

    public void addList(ArrayList<NewGoodsBean> newGoodsBeanArrayList) {
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
        @BindView(R.id.newgoods_linear_layout)
        LinearLayout mNewgoodsLinearLayout;

        // 使用BufferKnife设置LinearLayout的监听器
        @OnClick(R.id.newgoods_linear_layout)
        public void newgoodsItemOnClick() {
            // 取出存放的GoodsId并发送给启动的Activity
            int goodsId= (int) mNewgoodsLinearLayout.getTag();
            MFGT.gotoGoodsActivity(mContext,goodsId);
        }

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



}
