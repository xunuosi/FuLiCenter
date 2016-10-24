package cn.ucai.fulicenter.activity;

import android.content.Intent;
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
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.MyCenterFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

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
        mFragments[2] = new CategoryFragment();
        mFragments[4] = new MyCenterFragment();
        // .show()可以指定显示哪个fragment显示你可以add很多
        mFragmentManager.beginTransaction()
                .add(R.id.show_frameLayout, mFragments[0])
                .add(R.id.show_frameLayout, mFragments[1])
                .add(R.id.show_frameLayout, mFragments[2])
                .hide(mFragments[1])
                .hide(mFragments[2])
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

                if (FuLiCenterApplication.getUser() == null) {
                    MFGT.gotoLoginActivity(this);
                }else {
                    index = 4;
                }
                break;
        }
        setFragment();
        updateRadioButtonView();
    }

    /**
     * 设置点击菜单显示指定Fragment
     */
    private void setFragment() {
        L.i(TAG, "index:" + index);
        L.i(TAG, "current:" + currentIndex);
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
        L.e("xns","MainActivity,onBackPressed()");
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG, "MainActitity,onResume");
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG, "onActivityResult,requestCode:" + requestCode);
        // 判断是否由登录界面登录成功后跳转回来
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUser() != null) {
            index = 4;
            // 更新下方按钮的显示
            updateRadioButtonView();
        }
    }
}
