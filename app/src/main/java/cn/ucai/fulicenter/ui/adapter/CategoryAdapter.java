package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CategoryChildBean;
import cn.ucai.fulicenter.data.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.CategoryChildActivity;

/**
 * Created by Administrator on 2017/5/8.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    List<CategoryGroupBean> groupList;
    List<ArrayList<CategoryChildBean>> childList;
    Context context;

    public CategoryAdapter(List<CategoryGroupBean> groupList, List<ArrayList<CategoryChildBean>> childList, Context context) {
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
    }


    @Override
    public int getGroupCount() {
        return groupList != null ? groupList.size() : null;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && childList.get(groupPosition) != null ? childList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList != null ? groupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childList != null && childList.get(groupPosition) != null ? childList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.bind(groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.bind(groupPosition, childPosition);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView ivGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView ivIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int groupPosition, boolean isExpanded) {
            CategoryGroupBean bean = groupList.get(groupPosition);
            ImageLoader.downloadImg(context, ivGroupThumb, bean.getImageUrl());
            tvGroupName.setText(bean.getName());
            ivIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
    }

    class ChildViewHolder {
        @BindView(R.id.iv_child_thumb)
        ImageView ivChildThumb;
        @BindView(R.id.tv_child_name)
        TextView tvChildName;
        @BindView(R.id.layout_child_category)
        RelativeLayout mLayoutChildCategory;


        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(final int groupPosition, final int childPosition) {
            final CategoryChildBean bean = getChild(groupPosition, childPosition);
            if (bean != null) {
                ImageLoader.downloadImg(context, ivChildThumb, bean.getImageUrl());
                tvChildName.setText(bean.getName());
                mLayoutChildCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, CategoryChildActivity.class)
                                .putExtra(I.CategoryChild.CAT_ID, bean.getId())
                                .putExtra(I.CategoryGroup.NAME, groupList.get(groupPosition).getName())
                                .putExtra(I.CategoryChild.ID, childList.get(groupPosition))

                        );
                    }
                });

            }
        }
    }

}
