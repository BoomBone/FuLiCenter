package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.AlbumsBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.bean.PropertiesBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {
    IGoodsModel model;
    String TAG = "GoodsDetailActivity";
    int goodsId;
    Unbinder unbinder;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_good_share)
    ImageView ivGoodShare;
    @BindView(R.id.iv_good_collect)
    ImageView ivGoodCollect;
    @BindView(R.id.tvGoodNameEnglish)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tvGoodName)
    TextView tvGoodName;
    @BindView(R.id.tvGoodPrice)
    TextView tvGoodPrice;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.layout_banner)
    LinearLayout layoutGoodsdetail;
    @BindView(R.id.activity_goods_detail)
    RelativeLayout activityGoodsDetail;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail_activity);
        unbinder = ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e(TAG, "goodsId" + goodsId);
        initData();
    }

    private void initData() {
        if (goodsId == 0) {
            finish();
        } else {
            model = new GoodsModel();
            loadData();
        }

    }

    private void loadData() {
        model.loadGoodsDetailData(GoodsDetailActivity.this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.e(TAG, "result=" + result);
                if (result != null) {
                    showData(result);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showData(GoodsDetailsBean bean) {
        tvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        tvGoodName.setText(bean.getGoodsName());
        tvGoodPrice.setText(bean.getCurrencyPrice());
        tvGoodPriceShop.setText(bean.getShopPrice());
        salv.startPlayLoop(indicator, getAlbunImageUrl(bean), getAlbumImgCount(bean));
        wvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean bean) {
        AlbumsBean[] img = getAlbumImg(bean);
        if (img != null) {
            return img.length;
        }
        return 0;
    }

    private String[] getAlbunImageUrl(GoodsDetailsBean bean) {
        AlbumsBean[] img = getAlbumImg(bean);
        if (img != null) {
            String[] strings = new String[img.length];
            for (int i = 0; i < img.length; i++) {
                strings[i] = img[i].getImgUrl();
            }
            return strings;
        }
        return null;
    }

    private AlbumsBean[] getAlbumImg(GoodsDetailsBean bean) {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            PropertiesBean propertiesBean = bean.getProperties()[0];
            if (propertiesBean != null && propertiesBean.getAlbums() != null) {
                return propertiesBean.getAlbums();
            }
        }
        return null;
    }

    @OnClick(R.id.backClickArea)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (salv != null) {
            salv.stopPlayLoop();
        }

    }
}
