package cn.ucai.fulicenter.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/5/4.
 */

public class NewGoodsFragment extends Fragment {

    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    Unbinder unbinder;

    IGoodsModel model;
    GoodsAdapter adapter;
    GridLayoutManager gm;
    ArrayList<NewGoodsBean> newGoodList;
    int catId;
    private static final int ACTION_LOAD_DATA = 0;
    private static final int ACTION_PULL_DOWN = 1;
    private static final int ACTION_PULL_UP = 2;
    int pageId = 1;
    ProgressDialog pd;


    public NewGoodsFragment() {

    }

//    public NewGoodsFragment(int catId) {
//        this.catId = catId;
//    }
    //LinearLayoutManager lm;
    public static  NewGoodsFragment newInstance(int catId){
        NewGoodsFragment fragmentOne = new NewGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("catId", catId);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newgoods, null);
        unbinder = ButterKnife.bind(this, view);
        if(getArguments()!=null){
            catId = getArguments().getInt("catId");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog();
        initView();
        setListener();
        loadData(pageId, ACTION_LOAD_DATA);
    }

    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        model = new GoodsModel();
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        rvGoods.setLayoutManager(gm);
        newGoodList = new ArrayList<>();
        adapter = new GoodsAdapter(newGoodList, getContext());
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
    public void onDestroyView() {
        super.onDestroyView();
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

    public void sortGoood(int sortGoods) {
        if(adapter!=null){
            adapter.sortGoods(sortGoods);
        }
    }

    public void loadData(int pageId, final int action) {
        model.loadNewGoodsData(getContext(), catId, pageId, 10, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
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


                    ArrayList<NewGoodsBean> list = ResultUtils.array2List(result);
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
                pd.dismiss();
                setLayoutVisibily(false);
                //L.e("main","新品加载失败");

            }
        });
    }

  /*  private void updateUI(ArrayList<NewGoodsBean> list) {
        if(adapter==null){
            adapter = new GoodsAdapter(list,getContext());
            rvGoods.setAdapter(adapter);
        }
    }*/
}
