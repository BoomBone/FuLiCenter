package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CollectBean;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CollectListAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/5/15.
 */

public class CollectListActivity extends AppCompatActivity {
    private static final String TAG = "CollectListActivity";
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    int pageSize = I.PAGE_SIZE_DEFAULT;
    Unbinder unbinder;

    IUserModel model;
    CollectListAdapter adapter;
    GridLayoutManager gm;
    ArrayList<CollectBean> newGoodList;
    int catId;
    private static final int ACTION_LOAD_DATA = 0;
    private static final int ACTION_PULL_DOWN = 1;
    private static final int ACTION_PULL_UP = 2;
    int pageId = 1;
    ProgressDialog pd;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);
        unbinder = ButterKnife.bind(this);
        initDialog();
        initView();
        setListener();
        loadData(pageId, ACTION_LOAD_DATA);

    }


    private void initDialog() {
        pd = new ProgressDialog(CollectListActivity.this);
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        mTvCommonTitle.setText(R.string.collect_title);
        model = new UserModel();
        gm = new GridLayoutManager(CollectListActivity.this, I.COLUM_NUM);
        rvGoods.setLayoutManager(gm);
        newGoodList = new ArrayList<>();
        adapter = new CollectListAdapter(newGoodList, CollectListActivity.this);
        rvGoods.setAdapter(adapter);
        rvGoods.addItemDecoration(new SpaceItemDecoration(12));
        //设置页脚数据的居中
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter == null || position == adapter.getItemCount() - 1) {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });


        srf.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );

    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if (adapter != null && newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == adapter.getItemCount() - 1 && adapter.isMore()) {
                    pageId++;
                    loadData(pageId, ACTION_PULL_UP);
                }
            }
        });
    }

    private void setPullDownListener() {
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srf.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                loadData(pageId, ACTION_PULL_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            unbinder.unbind();
        }
    }


    //刷新可见，不刷新看不到
    void setLayoutVisibily(boolean visibily) {
        srf.setRefreshing(visibily);
        tvRefresh.setVisibility(visibily ? View.VISIBLE : View.GONE);
    }

    //刷新页面能看见，加载页面就看不见
    void setListVisibility(boolean visibility) {
        srf.setVisibility(visibility ? View.VISIBLE : View.GONE);
        tvNomore.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.tv_nomore)
    public void reload() {
        pd.show();
        loadData(pageId, ACTION_LOAD_DATA);
    }

    public void loadData(int pageId, final int action) {
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }
        model.loadCollect(CollectListActivity.this, user.getMuserName(), pageId, pageSize, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                pd.dismiss();
                setLayoutVisibily(false);
                setListVisibility(true);
                if (result != null && adapter != null) {
                    //updateUI(list);

                    adapter.setMore(result != null && result.length > 0);
                    if (!adapter.isMore()) {
                        if (action == ACTION_PULL_UP) {
                            adapter.setFooterText(getResources().getString(R.string.no_more));
                        }
                        return;
                    }

                    ArrayList<CollectBean> list = ResultUtils.array2List(result);
                    adapter.setFooterText(getResources().getString(R.string.load_more));
                    switch (action) {
                        case ACTION_LOAD_DATA:
                            adapter.initNewGoods(list);
                            break;
                        case ACTION_PULL_DOWN:
                            adapter.initNewGoods(list);
                            break;
                        case ACTION_PULL_UP:
                            adapter.addNewGoods(list);
                            L.e("main", "result:" + result);
                            break;

                    }
                } else {
                    if (adapter == null || adapter.getItemCount() == 1) {
                        setListVisibility(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (adapter == null || adapter.getItemCount() == 1) {
                    setListVisibility(false);
                }
            }
        });

    }

    @OnClick(R.id.backClickArea)
    public void backClicked() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==I.REQUEST_CODE_GO_DETAIL&&resultCode==RESULT_OK){
            int goodId = data.getIntExtra(I.Goods.KEY_GOODS_ID, 0);
            boolean isCollect = data.getBooleanExtra(I.Goods.KEY_IS_COLLECT, true);
            L.e(TAG,"goodId="+goodId+"  isCollect"+isCollect);
            if(!isCollect){
                newGoodList.remove(new CollectBean(goodId));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
