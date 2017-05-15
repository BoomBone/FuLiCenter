package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
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
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.AlbumsBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.bean.PropertiesBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.ConvertUtils;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {

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
    User user;
    IGoodsModel model;
    IUserModel iUserModel;
    boolean isCollect = false;

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
            iUserModel = new UserModel();
            loadData();
        }

    }

    private void loadData() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
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
        //判断是否收藏
        loadCollectStatus();

    }

    private void loadCollectStatus() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            iUserModel.isCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            isCollect = result != null && result.isSuccess() ? true : false;
                            updataCollectUI();
                        }

                        @Override
                        public void onError(String error) {
                            isCollect = false;
                            updataCollectUI();
                        }
                    });

        }
    }

    private void updataCollectUI() {
        ivGoodCollect.setImageResource(isCollect ? R.mipmap.bg_collect_out : R.mipmap.bg_collect_in);
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
        setResult(RESULT_OK,new Intent()
                .putExtra(I.Goods.KEY_GOODS_ID,goodsId)
                .putExtra(I.Goods.KEY_IS_COLLECT,isCollect));
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,new Intent()
                .putExtra(I.Goods.KEY_GOODS_ID,goodsId)
                .putExtra(I.Goods.KEY_IS_COLLECT,isCollect));
        super.onBackPressed();
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

    @OnClick(R.id.iv_good_collect)
    public void onCollectClick() {
        if(user==null){
            startActivityForResult(new Intent(GoodsDetailActivity.this, LoginActivity.class), 0);
        }else {
            if(isCollect){
                iUserModel.removeCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(), mListener);
            }else{
                iUserModel.addCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(), mListener);

            }
        }
    }
    OnCompleteListener<MessageBean> mListener=new OnCompleteListener<MessageBean>() {
        @Override
        public void onSuccess(MessageBean result) {
            isCollect = !isCollect;
            updataCollectUI();
            CommonUtils.showLongToast(result.getMsg());
        }

        @Override
        public void onError(String error) {
            CommonUtils.showLongToast(error);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK){
            loadCollectStatus();
        }
    }
}
