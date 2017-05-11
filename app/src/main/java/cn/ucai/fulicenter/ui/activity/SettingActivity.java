package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.SharePrefrenceUtils;


public class SettingActivity extends AppCompatActivity {
    String TAG = "SettingActivity";
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.username)
    TextView mTvusername;
    @BindView(R.id.nickname)
    TextView mTvnickname;
    IUserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mTvCommonTitle.setText(R.string.setting);
        initData();
        model = new UserModel();
    }

    private void initData() {
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            mTvusername.setText(user.getMuserName());
            mTvnickname.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), SettingActivity.this,
                    mIvUserAvatar);
        }
        L.e(TAG,"initData().user.getMuserName()"+user.getMuserName()+"user.getMuserNick()"+user.getMuserNick());
    }

    @OnClick({R.id.backClickArea, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                finish();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        //将原来的清空
        FuLiCenterApplication.getInstance().setCurrentUser(null);
        SharePrefrenceUtils.getInstance().removeUser();
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.rl_nickname)
    public void updateNick() {
        startActivityForResult(new Intent(SettingActivity.this,UpdateNickActivity.class),I.REQUEST_CODE_NICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode== I.REQUEST_CODE_NICK){
            initData();
        }
    }
}
