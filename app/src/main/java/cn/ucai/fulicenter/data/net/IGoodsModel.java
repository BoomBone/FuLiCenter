package cn.ucai.fulicenter.data.net;

import android.content.Context;

import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface IGoodsModel {
    void loadNewGoodsData(Context context, int catId , int pageId , int pageSize ,
                          OnCompleteListener<NewGoodsBean[]> listener);
}
