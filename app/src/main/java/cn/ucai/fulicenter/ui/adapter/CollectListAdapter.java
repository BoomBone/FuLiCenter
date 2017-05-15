package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CollectBean;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.CollectListActivity;
import cn.ucai.fulicenter.ui.activity.GoodsDetailActivity;

/**
 * Created by Administrator on 2017/5/15.
 */

public class CollectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CollectBean> list;
    Context context;
    String footerText;
    IUserModel model;


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
        model = new UserModel();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == I.TYPE_FOOTER) {
            return new FooterTextHolder(View.inflate(context, R.layout.item_footer, null));
        }
        return new GoodsViewHolder(View.inflate(context, R.layout.item_collect_list, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentholder, final int position) {
        if (I.TYPE_FOOTER == getItemViewType(position)) {
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
                //点击后会跳转到商品详情，把goodid传过去；
                ((CollectListActivity)context).startActivityForResult(new Intent(context, GoodsDetailActivity.class)
                        .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId()),I.REQUEST_CODE_GO_DETAIL);
            }
        });
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCollect(position);
            }
        });


    }

    private void removeCollect(final int position) {
        if(model!=null){
          model.removeCollect(context, String.valueOf(list.get(position).getGoodsId())
                  , FuLiCenterApplication.getInstance().getCurrentUser().getMuserName()
                  , new OnCompleteListener<MessageBean>() {
                      @Override
                      public void onSuccess(MessageBean result) {
                          CommonUtils.showLongToast(result.getMsg());
                          list.remove(position);
                          notifyDataSetChanged();
                      }

                      @Override
                      public void onError(String error) {

                      }
                  });
        }
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
        @BindView(R.id.iv_del)
        ImageView ivDel;

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
