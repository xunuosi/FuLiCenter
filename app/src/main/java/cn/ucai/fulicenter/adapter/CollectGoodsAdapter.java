package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.ItemFooterViewHolder;


public class CollectGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = CollectGoodsItemViewHolder.class.getSimpleName();

    List<CollectBean> mList;
    Context mContext;
    boolean isMore;

    public CollectGoodsAdapter(List<CollectBean> beanList, Context context) {
        // 通常采用这种方式
        mList = new ArrayList<>();
        mList = beanList;
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
            holder = new CollectGoodsItemViewHolder
                    (View.inflate(mContext, R.layout.item_collects, null));
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
        CollectBean bean = mList.get(position);
        CollectGoodsItemViewHolder viewHolder = (CollectGoodsItemViewHolder) holder;
        viewHolder.bindView(bean);

        // 使用LinearLayout.tag属性保存被每个Item的商品ID值
        viewHolder.mLayout
                .setTag(bean);
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
        return mList == null ? 1 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initList(ArrayList<CollectBean> newArrayList) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(newArrayList);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<CollectBean> newArrayList) {
        mList.addAll(newArrayList);
        notifyDataSetChanged();
    }

    class CollectGoodsItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.newgoods_image_view)
        ImageView mNewgoodsImageView;
        @BindView(R.id.newgoods_title_text_view)
        TextView mNewgoodsTitleTextView;
        @BindView(R.id.delete_imageView)
        ImageView mDelteImageView;
        @BindView(R.id.collect_item)
        RelativeLayout mLayout;

        // 使用BufferKnife设置LinearLayout的监听器
        @OnClick(R.id.collect_item)
        public void itemOnClick() {
            // 取出存放的GoodsId并发送给启动的Activity
            CollectBean bean = (CollectBean) mLayout.getTag();
            int goodsId = bean.getGoodsId();
            MFGT.gotoGoodsActivity(mContext,goodsId);
        }

        public CollectGoodsItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(CollectBean bean) {
            mNewgoodsTitleTextView.setText(bean.getGoodsName());
            // setImageView
            ImageLoader.downloadImg(mContext,mNewgoodsImageView,bean.getGoodsThumb());
        }

        @OnClick(R.id.delete_imageView)
        public void deleteGoods() {
            String username = FuLiCenterApplication.getUser().getMuserName();
            CollectBean bean = (CollectBean) mLayout.getTag();
            int goodsId = bean.getGoodsId();

            NetDao.deleteCollect(mContext, username, goodsId
                    , new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                // 删除List集合中的数据
                                CommonUtils.showShortToast(
                                        mContext.getResources().getString(R.string.delete_success));
                            } else {
                                CommonUtils.showShortToast(
                                        mContext.getResources().getString(R.string.delete_fail));
                            }
                        }

                        @Override
                        public void onError(String error) {
                            L.e(TAG, "ERROR:" + error);
                            CommonUtils.showShortToast(error);
                        }
                    });
        }

    }
}
