package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CollectBean;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.GoodsDetailActivity;

/**
 * Created by Administrator on 2017/5/15.
 */

public class CollectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CollectBean> list;
    Context context;
    String footerText;

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    boolean isMore = true;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectListAdapter(List<CollectBean> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == I.TYPE_FOOTER) {
            return new CollectListAdapter.FooterTextHolder(View.inflate(context, R.layout.item_footer, null));
        }
        return new CollectListAdapter.GoodsViewHolder(View.inflate(context, R.layout.item_collect_list, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentholder, int position) {
        if(I.TYPE_FOOTER==getItemViewType(position)){
            FooterTextHolder holder = (FooterTextHolder) parentholder;
            holder.tvFooter.setText(footerText);
            return;
        }
        final CollectBean bean = list.get(position);
        GoodsViewHolder holder = (GoodsViewHolder) parentholder;
        holder.tvGoodsName.setText(bean.getGoodsName());
        ImageLoader.downloadImg(context, holder.tvGoodsThumb, bean.getGoodsThumb());
        holder.tvGoodsThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GoodsDetailActivity.class)
                        .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId())
                );

            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initNewGoods(ArrayList<CollectBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addNewGoods(ArrayList<CollectBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGoodsThumb)
        ImageView tvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;


        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FooterTextHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer)
        TextView tvFooter;

        FooterTextHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
