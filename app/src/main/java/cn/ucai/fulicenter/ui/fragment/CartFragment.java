package cn.ucai.fulicenter.ui.fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CartBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.activity.LoginActivity;
import cn.ucai.fulicenter.ui.activity.OrderActivity;
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
    @BindView(R.id.layout_new_good)
    RelativeLayout layoutNewGood;
    @BindView(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @BindView(R.id.tv_cart_buy)
    TextView tvCartBuy;
    @BindView(R.id.layout_cart)
    RelativeLayout layoutCart;
    int sumPrice;
    int savePrice;

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
        //过滤和注册
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        getContext().registerReceiver(mReceiver, filter);

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
        if (FuLiCenterApplication.getInstance().isLogined()) {
            User user = FuLiCenterApplication.getInstance().getCurrentUser();
            model.loadCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    pd.dismiss();
                    setLayoutVisibily(false);
                    setListVisibility(true, false);
                    list.clear();

                    if (result != null) {
                        list.addAll(ResultUtils.array2List(result));
                        updataUI();
                        if (list.size() == 0) {
                            setListVisibility(false, false);
                        }
                    } else {
                        setListVisibility(false, false);
                    }
                    sumPrice();
                }

                @Override
                public void onError(String error) {
                    pd.dismiss();
                    setLayoutVisibily(false);
                    list.clear();
                    setListVisibility(false, true);
                    sumPrice();
                }
            });
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 0);
        }

    }

    private void updataUI() {
        if (adapter == null) {
            adapter = new CartAdapter(getContext(), list);
            adapter.setCbkListener(cbkListener);
            adapter.setClickListener(clickListener);
            rvGoods.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    //刷新可见，不刷新看不到
    void setLayoutVisibily(boolean visibily) {
        srf.setRefreshing(visibily);
        tvRefresh.setVisibility(visibily ? View.VISIBLE : View.GONE);
    }

    //刷新页面能看见，加载页面就看不见
    void setListVisibility(boolean visibility, boolean isError) {
        tvNomore.setText(isError ? R.string.reload_data : R.string.order_nothing);
        srf.setVisibility(visibility ? View.VISIBLE : View.GONE);
        tvNomore.setVisibility(visibility ? View.GONE : View.VISIBLE);
        layoutCart.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.tv_nomore)
    public void reload() {
        pd.show();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            unbinder.unbind();
        }
    }

    private void sumPrice() {
        sumPrice = 0;
        savePrice = 0;
        if (list.size() > 0) {
            for (CartBean bean : list) {
                if (bean.isChecked()) {
                    GoodsDetailsBean goods = bean.getGoods();
                    if (goods != null) {
                        sumPrice += getPrice(goods.getCurrencyPrice()) * bean.getCount();
                        savePrice += (getPrice(goods.getCurrencyPrice()) - getPrice(goods.getRankPrice())) * bean.getCount();
                    }
                }

            }
        } else {
            sumPrice = 0;
            savePrice = 0;
        }
        tvCartSumPrice.setText("合计：￥" + sumPrice);
        tvCartSavePrice.setText("节省：￥" + savePrice);

    }

    private int getPrice(String currentPrice) {
        String price = currentPrice.substring(currentPrice.indexOf("￥") + 1);
        return Integer.parseInt(price);
    }

    CompoundButton.OnCheckedChangeListener cbkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            L.e(TAG, "OnCheckedChangeListener.position=" + position + ",isChecked=" + isChecked);
            CartBean bean = list.get(position);
            bean.setChecked(isChecked);
            sumPrice();
        }
    };
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_cart_add:
                    final int position = (int) v.getTag();
                    updateCart(position, 1);
                    break;
                case R.id.iv_cart_del:
                    final int delPosition = (int) v.getTag();
                    L.e(TAG, "View.OnClickListener.delPosition=" + delPosition);
                    CartBean bean = list.get(delPosition);
                    if (bean.getCount() > 1) {
                        updateCart(delPosition, -1);
                    } else if (bean.getCount() == 1) {
                        model.removeCart(getContext(), bean.getId(), new OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                list.remove(delPosition);
                                adapter.notifyDataSetChanged();
                                sumPrice();
                                if (list.size() == 0) {
                                    setListVisibility(false, false);
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }
                    break;
            }
        }
    };


    private void updateCart(final int position, final int count) {
        final CartBean bean = list.get(position);
        model.updateCart(getContext(), bean.getId(), bean.getCount() + count, false, new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    list.get(position).setCount(bean.getCount() + count);
                    adapter.notifyDataSetChanged();
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    UpdateCartBroadcastReceiver mReceiver = new UpdateCartBroadcastReceiver();

    @OnClick(R.id.tv_cart_buy)
    public void onCartBuyClicked() {
        if(sumPrice>0){
            startActivity(new Intent(getContext(),OrderActivity.class)
            .putExtra(I.Cart.PAY_PRICE,sumPrice-savePrice));
        }else{
            CommonUtils.showLongToast(R.string.order_nothing);
        }
    }

    class UpdateCartBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            GoodsDetailsBean bean = (GoodsDetailsBean) intent.getSerializableExtra(I.Cart.class.toString());
            updateCart(bean);
        }
    }

    private void updateCart(GoodsDetailsBean bean) {
        boolean isHas = false;
        //如果相同数据相加
        if (bean != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getGoodsId() == bean.getGoodsId()) {
                    list.get(i).setCount(list.get(i).getCount() + 1);
                    adapter.notifyItemChanged(i);
                    sumPrice();
                    isHas = true;
                    return;
                }
            }
        }

        L.e(TAG, "updateCart.isHas=" + isHas);
        if (!isHas) {
            CartBean cart = new CartBean();
            cart.setCount(1);
            cart.setGoodsId(bean.getGoodsId());
            cart.setChecked(true);
            cart.setUserName(FuLiCenterApplication.getInstance().getCurrentUser().getMuserName());
            cart.setGoods(bean);
            list.add(cart);
        }
        adapter.notifyDataSetChanged();
        sumPrice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }
}
