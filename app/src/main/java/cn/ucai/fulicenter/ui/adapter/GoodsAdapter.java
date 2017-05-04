package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/4.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {
    List<NewGoodsBean> list;
    Context context;

    boolean isMore=true;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public GoodsAdapter(List<NewGoodsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public GoodsAdapter.GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(View.inflate(context, R.layout.item_goods, null));
    }

    @Override
    public void onBindViewHolder(GoodsAdapter.GoodsViewHolder holder, int position) {
        NewGoodsBean bean = list.get(position);
        holder.tvGoodsName.setText(bean.getGoodsName());
        holder.tvGoodsPrice.setText(bean.getCurrencyPrice());
        ImageLoader.downloadImg(context,holder.tvGoodsThumb,bean.getGoodsThumb());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void initNewGoods(ArrayList<NewGoodsBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addNewGoods(ArrayList<NewGoodsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGoodsThumb)
        ImageView tvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
