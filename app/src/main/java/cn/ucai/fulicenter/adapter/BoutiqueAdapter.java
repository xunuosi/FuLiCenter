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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;


public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<BoutiqueBean> mList;
    Context mContext;

    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new BoutiqueItemViewHolder(LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_boutique, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
            // 将解析出的每个BoutiqueBean存放在父容器的Tag属性中
            ((BoutiqueItemViewHolder) holder).mBoutiqueContentLayout
                    .setTag(boutiqueBean);
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void initList(ArrayList<BoutiqueBean> newList) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(newList);
        notifyDataSetChanged();
    }


    class BoutiqueItemViewHolder extends RecyclerView.ViewHolder {
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

        // 设置单击监听
        @OnClick(R.id.boutique_content_layout)
        public void onBoutiqueItemClick() {
            BoutiqueBean bean = (BoutiqueBean) mBoutiqueContentLayout.getTag();
            MFGT.gotoBoutiqueChildActivity(mContext, bean);
        }

        public BoutiqueItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}