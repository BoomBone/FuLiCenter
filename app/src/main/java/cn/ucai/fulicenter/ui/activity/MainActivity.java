package cn.ucai.fulicenter.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.fragment.BotiqueFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
    NewGoodsFragment mNewGoodsFragment;
    BotiqueFragment mBotiqueFragment;
    Fragment[] mFragments;
    int currentInndex, index;

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
        mFragments = new Fragment[5];
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBotiqueFragment;
    }

    private void showFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .add(R.id.fragment_container, mBotiqueFragment)
                .show(mNewGoodsFragment)
                .hide(mBotiqueFragment)
                .commit();
    }

    public void onCheckedChange(View view) {
       switch (view.getId()){
           case R.id.layout_new_good:
               index = 0;
               break;
           case R.id.layout_boutique:
               index = 1;
               break;
       }
       setFragment();
    }

    private void setFragment() {
        if(index!=currentInndex){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, mFragments[index]);
            ft.commit();
            currentInndex=index;
        }
    }


}
