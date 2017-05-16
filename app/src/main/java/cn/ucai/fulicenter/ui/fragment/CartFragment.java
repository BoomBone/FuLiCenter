package cn.ucai.fulicenter.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.BoutiqueBean;
import cn.ucai.fulicenter.data.bean.CartBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.activity.GoodsDetailActivity;
import cn.ucai.fulicenter.ui.activity.LoginActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;
import cn.ucai.fulicenter.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    Unbinder unbinder;

    IUserModel model;
    LinearLayoutManager llm;
    CartAdapter adapter;
    ArrayList<CartBean> list = new ArrayList<>();

    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, null);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new UserModel();
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
        if(FuLiCenterApplication.getInstance().isLogined()){
            User user = FuLiCenterApplication.getInstance().getCurrentUser();
            model.loadCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    pd.dismiss();
                    setLayoutVisibily(false);
                    setListVisibility(true);
                    list.clear();

                    if (result != null) {
                        list .addAll(ResultUtils.array2List(result)) ;
                        updataUI();
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
                    list.clear();
                    if(adapter==null||adapter.getItemCount()==1){
                        setListVisibility(false);
                    }
                }
            });
        }else{
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 0);
        }

    }

    private void updataUI() {
        if(adapter==null){
            adapter = new CartAdapter(getContext(), list);
            rvGoods.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
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
        if(adapter!=null){
            unbinder.unbind();
        }
    }
}
