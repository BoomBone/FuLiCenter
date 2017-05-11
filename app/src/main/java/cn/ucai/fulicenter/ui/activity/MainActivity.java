package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.fragment.BotiqueFragment;
import cn.ucai.fulicenter.ui.fragment.CategoryFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.fragment.PersonalFragment;

public class MainActivity extends AppCompatActivity {
    String TAG="MainActivity";
    NewGoodsFragment mNewGoodsFragment;
    BotiqueFragment mBotiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonalFragment mPersonalFragment;
    Fragment[] mFragments;
    RadioButton[] mRadioButton;
    int currentIndex, index;
    @BindView(R.id.layout_new_good)
    RadioButton mLayoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton mLayoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton mLayoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton mLayoutCart;
    @BindView(R.id.layout_person)
    RadioButton mLayoutPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initRadioButton();
        showFragment();
    }

    private void initRadioButton() {
        mRadioButton = new RadioButton[5];
        mRadioButton[0] = mLayoutNewGood;
        mRadioButton[1] = mLayoutBoutique;
        mRadioButton[2] = mLayoutCategory;
        mRadioButton[3] = mLayoutCart;
        mRadioButton[4] = mLayoutPerson;
    }

    private void initFragment() {
        mNewGoodsFragment = new NewGoodsFragment();
        mBotiqueFragment = new BotiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mPersonalFragment = new PersonalFragment();
        mFragments = new Fragment[5];
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBotiqueFragment;
        mFragments[2] = mCategoryFragment;
        mFragments[4] = mPersonalFragment;
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
                if (FuLiCenterApplication.getInstance().getCurrentUser() == null) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                            I.REQUEST_CODE_LOGIN
                            );
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        L.e("setFragment....index="+index+",currentIndex="+currentIndex);
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()) {
                ft.add(R.id.fragment_container, mFragments[index]);
            }
            //ft.replace(R.id.fragment_container, mFragments[index]);
            ft.show(mFragments[index]);
            L.e("setFragment....ft="+ft);
            ft.commitAllowingStateLoss();
            currentIndex = index;
            L.e("setFragment....index="+index+",currentIndex="+currentIndex);
        }
        setRadioButton();
    }

    private void setRadioButton() {
        for(int i=0;i<mRadioButton.length;i++){
            mRadioButton[i].setChecked(i==index?true:false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==I.REQUEST_CODE_LOGIN){
            index = 4;
            setFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG,"onResume,index"+index);
        if(index==4&&FuLiCenterApplication.getInstance().getCurrentUser()==null){
            index = 0;
            setFragment();
        }
    }
}
