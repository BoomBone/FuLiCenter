package cn.ucai.fulicenter.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.BoutiqueBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BotiqueFragment extends Fragment {
    private static final String TAG = "BoutiqueFragment";
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
    LinearLayoutManager llm;
    BoutiqueAdapter adapter;

    ProgressDialog pd;

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
        model = new GoodsModel();
        initDialog();
        initView();
        loadData();
        setListener();
    }

    private void setListener() {
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srf.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                loadData();
            }
        });
    }

    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        llm = new LinearLayoutManager(getContext());
        rvGoods.setLayoutManager(llm);

        rvGoods.addItemDecoration(new SpaceItemDecoration(12));
        //设置页脚数据的居中

        srf.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
    }

    private void loadData() {
        model.loadBoutiqueData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                L.e(TAG,"resule="+ Arrays.toString(result));
                pd.dismiss();
                setLayoutVisibily(false);
                setListVisibility(true);
                if (result != null) {
                    ArrayList<BoutiqueBean> list = ResultUtils.array2List(result);
                    updataUI(list);
                }else{
                    if(adapter==null||adapter.getItemCount()==1){
                        setListVisibility(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                setLayoutVisibily(false);

            }
        });
    }

    private void updataUI(ArrayList<BoutiqueBean> list) {
        if(adapter==null){
            adapter = new BoutiqueAdapter(getContext(), list);
            rvGoods.setAdapter(adapter);
        }else{
            adapter.initData(list);
        }
    }

    //刷新可见，不刷新看不到
    void setLayoutVisibily(boolean visibily) {
        srf.setRefreshing(visibily);
        tvRefresh.setVisibility(visibily?View.VISIBLE:View.GONE);
    }
    //刷新页面能看见，加载页面就看不见
    void setListVisibility(boolean visibility){
        srf.setVisibility(visibility ? View.VISIBLE : View.GONE);
        tvNomore.setVisibility(visibility?View.GONE:View.VISIBLE);
    }
    @OnClick(R.id.tv_nomore)
    public void reload(){
        pd.show();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
