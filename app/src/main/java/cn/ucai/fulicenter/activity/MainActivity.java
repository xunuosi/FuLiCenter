package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends BaseActivity {
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
    int currentIndex = 0;
    RadioButton[] mRadioButtons;
    Fragment[] mFragments;// 用于管理页脚导航存放的Fragment
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        initFragment();
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[5];
        mFragments[0] = new NewGoodsFragment();
        mFragments[1] = new BoutiqueFragment();
        // .show()可以指定显示哪个fragment显示你可以add很多
        mFragmentManager.beginTransaction()
                .add(R.id.show_frameLayout, mFragments[0])
                .add(R.id.show_frameLayout, mFragments[1])
                .hide(mFragments[1])
                .show(mFragments[0])
                .commit();

    }

    @Override
    protected void initView() {
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
        setFragment();
        updateRadioButtonView();
    }

    /**
     * 设置点击菜单显示指定Fragment
     */
    private void setFragment() {
        // 还在点击当前按钮不响应
        if (index != currentIndex) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(mFragments[currentIndex]);
            // 判断是否已经添加到Activity中
            if (!mFragments[index].isAdded()) {
                transaction.add(R.id.show_frameLayout, mFragments[index]);
            }

            transaction.show(mFragments[index]).commit();
                // 更新currentIndex值
                currentIndex = index;

        }
    }

    private void updateRadioButtonView() {
        L.i("xns", "index:"+index);
        L.i("xns", "current:" + currentIndex);
        for (int i = 0; i < mRadioButtons.length; i++) {
            if (i == index) {
                mRadioButtons[i].setChecked(true);
                continue;
            }
            mRadioButtons[i].setChecked(false);
        }
    }

    /**
     * 主界面直接退出不需要动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
