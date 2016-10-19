package cn.ucai.fulicenter.activity;

import android.os.Bundle;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.L;

public class BoutiqueChildActivity extends BaseActivity {
    BoutiqueBean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        // 得到传递的对象
        mBean = (BoutiqueBean) getIntent().getSerializableExtra(I.Boutique.CAT_ID);
        L.e("BoutiqueChildActivity:" + mBean.getId());
        if (mBean == null) {
            finish();
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
