package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.CategoryChildBean;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.adapter.CatFiterAdapter;

/**
 * Created by Administrator on 2017/5/9.
 */

public class CatFiterCategoryButton extends Button {
    PopupWindow mPopupWindow;
    Context context;
    boolean isExpand = false;
    GridView gv;
    CatFiterAdapter adapter;


    public CatFiterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setCatFiterListener();
    }

    private void setCatFiterListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                } else {
                    initPopWin();
                }
                setArrow();
            }
        });

    }

    private void initPopWin() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(context);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
           /* TextView tv = new TextView(context);
            tv.setTextColor(getResources().getColor(R.color.red));
            tv.setTextSize(30);
            tv.setText("PPPPP");*/
            mPopupWindow.setContentView(gv);
        }
        mPopupWindow.showAsDropDown(this);
    }

    private void setArrow() {
        L.e("CatFiterCategoryButton", "setArrow" + isExpand);
        Drawable end = context.getDrawable(!isExpand ? R.drawable.arrow2_down : R.drawable.arrow2_up);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
        isExpand = !isExpand;
    }

    public void initView(String groupName, List<CategoryChildBean> list) {
        if (groupName == null || list == null || list.size() == 0) {
            CommonUtils.showLongToast("数据获取异常，请重试！");
            return;
        }
        this.setText(groupName);
        adapter = new CatFiterAdapter(context, list);
        gv = new GridView(context);
        gv.setNumColumns(GridView.AUTO_FIT);
        gv.setHorizontalSpacing(10);
        gv.setVerticalSpacing(10);
        gv.setAdapter(adapter);
    }
    public void release(){
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }
}
