package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;


public class CategoryChildActivity extends AppCompatActivity {
    String TAG="CategoryChildActivity";
    NewGoodsFragment fragment;
    int sortBy = I.SORT_BY_ADDTIME_ASC;
    boolean priceAsc, addTimeAsc;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        unbinder=ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, I.CAT_ID);
        fragment = new NewGoodsFragment(catId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void onViewClicked(View view) {
        L.e(TAG,"onViewClicked"+view.getId());
        switch (view.getId()) {
            case R.id.btn_sort_price:
                priceAsc = !priceAsc;
                sortBy = priceAsc ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                break;
            case R.id.btn_sort_addtime:
                addTimeAsc = !addTimeAsc;
                sortBy = addTimeAsc ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                break;
        }
        fragment.sortGoood(sortBy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
        }
    }
}
