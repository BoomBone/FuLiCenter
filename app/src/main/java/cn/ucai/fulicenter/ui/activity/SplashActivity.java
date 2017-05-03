package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.ucai.fulicenter.R;


public class SplashActivity extends AppCompatActivity {
    TextView mtvSkip;
    private final static int time = 5000;
    MyCountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mtvSkip = (TextView) findViewById(R.id.tvSkip);
        cdt=new MyCountDownTimer(time,1000);
        cdt.start();

    }

    class MyCountDownTimer extends CountDownTimer{

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
            mtvSkip.setText(getString(R.string.skip)+""+millisUntilFinished/1000+"s");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
    }
}
