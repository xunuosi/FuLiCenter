package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;


public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<BoutiqueBean> mList;
    Context mContext;
    boolean isMore;

    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
        mContext = context;
        mList = list;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        // 提醒更新数据显示
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new NewGoodsAdapter
                    .ItemFooterViewHolder(LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new BoutiqueItemViewHolder(LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_boutique, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewGoodsAdapter.ItemFooterViewHolder) {
            ((NewGoodsAdapter.ItemFooterViewHolder) holder)
                    .mFooterTextView.setText(getFooterStr());
        }
        if (holder instanceof BoutiqueItemViewHolder) {
            BoutiqueBean boutiqueBean = mList.get(position);
            ((BoutiqueItemViewHolder) holder).mBoutiqueTitleTextView
                    .setText(boutiqueBean.getTitle());
            ((BoutiqueItemViewHolder) holder).mBoutiqueNameTextView
                    .setText(boutiqueBean.getName());
            ((BoutiqueItemViewHolder) holder).mBoutiqueDescriptionTextView
                    .setText(boutiqueBean.getDescription());

            ImageLoader.downloadImg(mContext,
                    ((BoutiqueItemViewHolder) holder).mBoutiqueImageView
                    , boutiqueBean.getImageurl());
        }
    }

    private int getFooterStr() {
        return isMore() == true ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList == null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    static class BoutiqueItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.boutique_imageView)
        ImageView mBoutiqueImageView;
        @BindView(R.id.boutique_title_textView)
        TextView mBoutiqueTitleTextView;
        @BindView(R.id.boutique_name_textView)
        TextView mBoutiqueNameTextView;
        @BindView(R.id.boutique_description_textView)
        TextView mBoutiqueDescriptionTextView;
        @BindView(R.id.boutique_linearLayout)
        LinearLayout mBoutiqueLinearLayout;
        @BindView(R.id.boutique_content_layout)
        RelativeLayout mBoutiqueContentLayout;

        public BoutiqueItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
