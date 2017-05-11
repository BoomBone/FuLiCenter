package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.local.UserDao;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.SharePrefrenceUtils;


public class SplashActivity extends AppCompatActivity {
    //TextView mtvSkip;
    private final static int time = 5000;
    MyCountDownTimer cdt;
    @BindView(R.id.tvSkip)
    TextView tvSkip;
    Unbinder bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        //tvSkip = (TextView) findViewById(R.id.tvSkip);
        cdt = new MyCountDownTimer(time, 1000);
        cdt.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //如果软件内存中没有用户
                if (FuLiCenterApplication.getInstance().getCurrentUser() == null) {
                    //从首选项中获取姓名
                    String userName = SharePrefrenceUtils.getInstance().getUserName();
                    if (userName != null) {
                        UserDao dao = new UserDao(SplashActivity.this);
                        User user = dao.getUser(userName);
                        L.e("SplashActivity.onStart()"+user);
                        if (user != null) {
                            FuLiCenterApplication.getInstance().setCurrentUser(user);

                        }
                    }
                }
            }
        }).start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    @OnClick(R.id.tvSkip)
    void skip() {
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
