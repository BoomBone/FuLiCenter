package cn.ucai.fulicenter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CategoryChildBean;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.CatFiterCategoryButton;


public class CategoryChildActivity extends AppCompatActivity {
    String TAG = "CategoryChildActivity";
    NewGoodsFragment fragment;
    int sortBy = I.SORT_BY_ADDTIME_ASC;
    boolean priceAsc, addTimeAsc;
    Unbinder unbinder;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;
    @BindView(R.id.cat_fiter)
    CatFiterCategoryButton catFiter;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        unbinder = ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, I.CAT_ID);
        fragment = new NewGoodsFragment(catId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        String groupName = getIntent().getStringExtra(I.CategoryChild.NAME);
        ArrayList<CategoryChildBean> list = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        catFiter.initView(groupName, list);
        L.e(TAG, "groupName=" + groupName);
        L.e(TAG, "list=" + list.toString());

    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime,R.id.backClickArea})
    public void onViewClicked(View view) {
        Drawable end;
        L.e(TAG, "onViewClicked" + view.getId());
        switch (view.getId()) {
            case R.id.btn_sort_price:
                priceAsc = !priceAsc;
                sortBy = priceAsc ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;

                end = getDrawable(priceAsc ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.btn_sort_addtime:
                addTimeAsc = !addTimeAsc;
                sortBy = addTimeAsc ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                end = getDrawable(addTimeAsc ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                btnSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.backClickArea:
                finish();
                break;
        }
        fragment.sortGoood(sortBy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (catFiter != null) {
            catFiter.release();
        }
    }


}
