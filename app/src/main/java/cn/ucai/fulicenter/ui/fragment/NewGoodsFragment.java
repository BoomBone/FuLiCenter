package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
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
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.ConvertUtils;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;

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

    private static final int ACTION_LOAD_DATA=0;
    private static final int ACTION_PULL_DOWN=1;
    private static final int ACTION_PULL_UP=2;
    int pageId=1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newgoods, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        setListener();
        loadData(pageId,ACTION_LOAD_DATA);
    }

    private void initView() {
        model = new GoodsModel();
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        rvGoods.setLayoutManager(gm);
        newGoodList = new ArrayList<>();
        adapter = new GoodsAdapter(newGoodList,getContext());
        rvGoods.setAdapter(adapter);

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
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastPosition==adapter.getItemCount()-1&&adapter.isMore()){
                    pageId++;
                    loadData(pageId,ACTION_PULL_UP);
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
                pageId=1;
                loadData(pageId,ACTION_PULL_DOWN);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void loadData(int pageId, final int action){
        model.loadNewGoodsData(getContext(), 0, pageId, 10, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {

                if(result!=null){

                    //updateUI(list);
                    adapter.setMore(result!=null&&result.length>0);
                    if(!adapter.isMore()){
                        if(action==ACTION_PULL_UP){
                            adapter.setFooterText(getResources().getString(R.string.no_more));
                        }
                        return;
                    }
                    ArrayList<NewGoodsBean> list = ResultUtils.array2List(result);
                    adapter.setFooterText(getResources().getString(R.string.load_more));
                    switch (action){
                        case ACTION_LOAD_DATA:
                            adapter.initNewGoods(list);
                            break;
                        case ACTION_PULL_DOWN:
                            srf.setRefreshing(false);
                            tvRefresh.setVisibility(View.GONE);
                            adapter.initNewGoods(list);
                            break;
                        case ACTION_PULL_UP:
                            adapter.addNewGoods(list);
                            L.e("main","result:"+result);
                            break;
                    }
                }

            }

            @Override
            public void onError(String error) {

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
