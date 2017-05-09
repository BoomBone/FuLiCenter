package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.utils.L;

/**
 * Created by Administrator on 2017/5/9.
 */

public class CatFiterCategoryButton extends Button {
    PopupWindow mPopupWindow;
    Context context;
    boolean isExpand=false;



    public CatFiterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setCatFiterListener();
    }

    private void setCatFiterListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpand){
                    if(mPopupWindow!=null&&mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                }
                else {
                    initPopWin();
                }
                setArrow();
            }
        });

    }

    private void initPopWin() {
        if(mPopupWindow==null){
            mPopupWindow = new PopupWindow(context);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            TextView tv = new TextView(context);
            tv.setTextColor(getResources().getColor(R.color.red));
            tv.setTextSize(30);
            tv.setText("PPPPP");
            mPopupWindow.setContentView(tv);
        }
        mPopupWindow.showAsDropDown(this);
    }

    private void setArrow() {
        L.e("CatFiterCategoryButton","setArrow"+isExpand);
        Drawable end = context.getDrawable(!isExpand ? R.drawable.arrow2_down : R.drawable.arrow2_up);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end,null);
        isExpand = !isExpand;
    }
}
