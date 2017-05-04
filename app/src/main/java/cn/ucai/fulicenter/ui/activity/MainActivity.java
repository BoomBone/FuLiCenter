package cn.ucai.fulicenter.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onCheckedChange(View view){
        testDownloadNewGoods();
    }
    public void testDownloadNewGoods(){
        GoodsModel model = new GoodsModel();
        model.loadNewGoodsData(MainActivity.this, 0, 1, 10, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.e("main","result"+result);
                if(result!=null){
                    L.e("main","result.length"+result.length);
                    for(NewGoodsBean bean:result){
                        L.e("main","bean"+bean);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("main","error"+error);
            }
        });
    }
}
