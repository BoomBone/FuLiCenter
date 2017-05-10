package cn.ucai.fulicenter.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.fragment.BotiqueFragment;
import cn.ucai.fulicenter.ui.fragment.CategoryFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
    NewGoodsFragment mNewGoodsFragment;
    BotiqueFragment mBotiqueFragment;
    CategoryFragment mCategoryFragment;
    Fragment[] mFragments;
    int currentIndex, index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        showFragment();
    }

    private void initFragment() {
        mNewGoodsFragment = new NewGoodsFragment();
        mBotiqueFragment = new BotiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mFragments = new Fragment[5];
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBotiqueFragment;
        mFragments[2] = mCategoryFragment;
    }

    private void showFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragments[0])
                .add(R.id.fragment_container, mFragments[1])
                .add(R.id.fragment_container, mFragments[2])
                .show(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_person:
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragments[currentIndex]);
            if(!mFragments[index].isAdded()){
                ft.add(R.id.fragment_container,mFragments[index]);
            }
            //ft.replace(R.id.fragment_container, mFragments[index]);
            ft.show(mFragments[index]);
            ft.commit();
            currentIndex = index;
        }
    }

}
