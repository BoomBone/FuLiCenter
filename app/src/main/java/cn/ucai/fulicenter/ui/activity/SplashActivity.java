package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;


public class SplashActivity extends AppCompatActivity {
    //TextView mtvSkip;
    private final static int time = 5000;
    MyCountDownTimer cdt;
    @BindView(R.id.tvSkip)
    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //tvSkip = (TextView) findViewById(R.id.tvSkip);
        cdt = new MyCountDownTimer(time, 1000);
        cdt.start();
    }

    @OnClick(R.id.tvSkip) void skip(){
        cdt.cancel();
        cdt.onFinish();
    }


    class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvSkip.setText(getString(R.string.skip) + "" + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}
