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
import cn.ucai.fulicenter.data.bean.BoutiqueBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.BoutiqueHolder> {
    Context context;
    List<BoutiqueBean> list;

    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BoutiqueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoutiqueHolder(View.inflate(context, R.layout.item_boutique, null));
    }

    @Override
    public void onBindViewHolder(BoutiqueHolder holder, int position) {
        BoutiqueBean bean = list.get(position);
        holder.tvBoutiqueTitle.setText(bean.getTitle());
        holder.tvBoutiqueName.setText(bean.getName());
        holder.tvBoutiqueDescription.setText(bean.getDescription());
        ImageLoader.downloadImg(context,holder.imBoutiqueThumb,bean.getImageurl());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if(this.list!=null){
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    class BoutiqueHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imBoutiqueThumb)
        ImageView imBoutiqueThumb;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;

        BoutiqueHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
