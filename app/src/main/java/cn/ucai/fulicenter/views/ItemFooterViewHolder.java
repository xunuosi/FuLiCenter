package cn.ucai.fulicenter.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.ucai.fulicenter.R;


public class ItemFooterViewHolder extends RecyclerView.ViewHolder {
    public TextView mFooterTextView;

    public ItemFooterViewHolder(View itemView) {
        super(itemView);
        mFooterTextView = (TextView) itemView.findViewById(R.id.footer_text_view);
    }
}
