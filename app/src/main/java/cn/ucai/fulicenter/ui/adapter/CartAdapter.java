package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CartBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.GoodsDetailActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;

/**
 * Created by Administrator on 2017/5/16.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    List<CartBean> list;
    CompoundButton.OnCheckedChangeListener cbkListener;
    View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }









    public void setCbkListener(CompoundButton.OnCheckedChangeListener cbkListener) {
        this.cbkListener = cbkListener;
    }



    public CartAdapter(Context context, List<CartBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CartViewHolder holder = new CartViewHolder(View.inflate(context, R.layout.item_cart, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cb_cart_selected)
        CheckBox cbCartSelected;
        @BindView(R.id.iv_cart_thumb)
        ImageView ivCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView tvCartGoodName;
        @BindView(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @BindView(R.id.tv_cart_count)
        TextView tvCartCount;
        @BindView(R.id.iv_cart_del)
        ImageView ivCartDel;
        @BindView(R.id.tv_cart_price)
        TextView tvCartPrice;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            cbCartSelected.setOnCheckedChangeListener(null);
            final CartBean bean = list.get(position);
            if(bean!=null){
                GoodsDetailsBean goods = bean.getGoods();
                if(goods!=null){
                    ImageLoader.downloadImg(context, ivCartThumb, goods.getGoodsThumb());
                    tvCartGoodName.setText(goods.getGoodsName());
                    tvCartPrice.setText(goods.getCurrencyPrice());
                }
                tvCartCount.setText("(" + bean.getCount() + ")");
                cbCartSelected.setChecked(bean.isChecked());
                ivCartThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)context).startActivityForResult(new Intent(context, GoodsDetailActivity.class)
                        .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId()),0);
                    }
                });
                cbCartSelected.setOnCheckedChangeListener(cbkListener);
                cbCartSelected.setTag(position);

                ivCartAdd.setOnClickListener(clickListener);
                ivCartAdd.setTag(position);

                ivCartDel.setOnClickListener(clickListener);
                ivCartDel.setTag(position);
            }
        }
    }
}
