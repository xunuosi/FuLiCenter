package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rb_newGoods)
    RadioButton mRbNewGoods;
    @BindView(R.id.rb_boutique)
    RadioButton mRbBoutique;
    @BindView(R.id.rb_category)
    RadioButton mRbCategory;
    @BindView(R.id.rb_cart)
    RadioButton mRbCart;
    @BindView(R.id.cart_count_textView)
    TextView mCartCountTextView;
    @BindView(R.id.rb_myCenter)
    RadioButton mRbMyCenter;

    int index;// 记录被点击RadioButton的下标索引
    RadioButton[] mRadioButtons;
    Fragment[] mFragments;// 用于管理页脚导航存放的Fragment
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initFragment();
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[5];
        mFragments[0] = new NewGoodsFragment();

        mFragmentManager.beginTransaction()
                .add(R.id.show_frameLayout, mFragments[0])
                .show(mFragments[0])
                .commit();

    }

    private void initView() {
        mRadioButtons = new RadioButton[5];
        mRadioButtons[0] = mRbNewGoods;
        mRadioButtons[1] = mRbBoutique;
        mRadioButtons[2] = mRbCategory;
        mRadioButtons[3] = mRbCart;
        mRadioButtons[4] = mRbMyCenter;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rb_newGoods:
                index = 0;
                break;
            case R.id.rb_boutique:
                index = 1;
                break;
            case R.id.rb_category:
                index = 2;
                break;
            case R.id.rb_cart:
                index = 3;
                break;
            case R.id.rb_myCenter:
                index = 4;
                break;
        }
        updateRadioButtonView();
    }

    private void updateRadioButtonView() {
        L.i("xns", index + "");
        for (int i = 0; i < mRadioButtons.length; i++) {
            if (i == index) {
                mRadioButtons[i].setChecked(true);
                continue;
            }
            mRadioButtons[i].setChecked(false);
        }
    }
}
