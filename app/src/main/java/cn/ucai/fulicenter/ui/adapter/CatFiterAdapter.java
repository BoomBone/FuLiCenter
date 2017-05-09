package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.CategoryChildBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;

/**
 * Created by Administrator on 2017/5/9.
 */

public class CatFiterAdapter extends BaseAdapter {
    Context context;

    public CatFiterAdapter(Context context, List<CategoryChildBean> list) {
        this.context = context;
        this.list = list;
    }

    List<CategoryChildBean> list;

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CatFiterViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cat_fiter, null);
            holder = new CatFiterViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CatFiterViewHolder) convertView.getTag();
        }
        holder.bind(position);
        return convertView;
    }

    class CatFiterViewHolder {
        @BindView(R.id.ivCategoryChildThumb)
        ImageView ivCategoryChildThumb;
        @BindView(R.id.tvCategoryChildName)
        TextView tvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        CatFiterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            CategoryChildBean bean = list.get(position);
            ImageLoader.downloadImg(context, ivCategoryChildThumb, bean.getImageUrl());
            tvCategoryChildName.setText(bean.getName());
        }
    }
}
