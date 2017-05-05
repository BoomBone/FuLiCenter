package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BoutiqueChilldActivity extends AppCompatActivity {
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        bind = ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID, I.CAT_ID);
        L.e("main","cat_id"+catId);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new NewGoodsFragment(catId));
        ft.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
