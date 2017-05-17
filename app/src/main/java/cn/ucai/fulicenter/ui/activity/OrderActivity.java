package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;

/**
 * Created by Administrator on 2017/5/17.
 */

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_order_name)
    TextView tvOrderName;
    @BindView(R.id.ed_order_name)
    EditText edOrderName;
    @BindView(R.id.tv_order_phone)
    TextView tvOrderPhone;
    @BindView(R.id.ed_order_phone)
    EditText edOrderPhone;
    @BindView(R.id.tv_order_province)
    TextView tvOrderProvince;
    @BindView(R.id.spin_order_province)
    Spinner spinOrderProvince;
    @BindView(R.id.tv_order_street)
    TextView tvOrderStreet;
    @BindView(R.id.ed_order_street)
    EditText edOrderStreet;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    int payPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        initView();
    }

    private void initView() {
        tvCommonTitle.setText(R.string.order_title);
        tvOrderPrice.setText("合计 ：￥"+payPrice);
    }

    @OnClick(R.id.backClickArea)
    public void onBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_order_buy)
    public void onBuyClicked() {
        if(checkInput()){

        }
    }
    private boolean checkInput() {
        String receiveName=edOrderName.getText().toString();
        if(TextUtils.isEmpty(receiveName)){
            edOrderName.setError("收货人姓名不能为空");
            edOrderName.requestFocus();
            return false;
        }
        String mobile=edOrderPhone.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            edOrderPhone.setError("手机号码不能为空");
            edOrderPhone.requestFocus();
            return false;
        }
        if(!mobile.matches("[\\d]{11}")){
            edOrderPhone.setError("手机号码格式错误");
            edOrderPhone.requestFocus();
            return false;
        }
        String area=spinOrderProvince.getSelectedItem().toString();
        if(TextUtils.isEmpty(area)){
            Toast.makeText(OrderActivity.this,"收货地区不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        String address=edOrderStreet.getText().toString();
        if(TextUtils.isEmpty(address)){
            edOrderStreet.setError("街道地址不能为空");
            edOrderStreet.requestFocus();
            return false;
        }
        return true;
    }
}
