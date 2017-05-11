package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.Result;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.local.UserDao;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.ResultUtils;


public class UpdateNickActivity extends AppCompatActivity {
    User user;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.et_update_user_nick)
    EditText mEtUpdateUserNick;

    IUserModel model;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        model = new UserModel();
        mTvCommonTitle.setText(R.string.update_user_nick);
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            mEtUpdateUserNick.setText(user.getMuserNick());
            mEtUpdateUserNick.selectAll();
        } else {
            finish();
        }

    }

    @OnClick(R.id.backClickArea)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        initDialog();
        String newnick = mEtUpdateUserNick.getText().toString().trim();
        if(checkInput()){
            model.updateNick(UpdateNickActivity.this, user.getMuserName(), newnick, new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(s!=null){
                        Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                        if(result!=null){
                            if(result.getRetCode()== I.MSG_USER_SAME_NICK){
                                CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
                            }else if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                                CommonUtils.showLongToast(R.string.update_fail);
                            }else{
                                updateSuccess(result.getRetData());
                            }
                        }
                    }
                    dismissDialog();

                }

                @Override
                public void onError(String error) {
                    dismissDialog();
                }
            });
        }else{
            dismissDialog();
        }

    }

    private boolean checkInput() {
        String newnick = mEtUpdateUserNick.getText().toString().trim();
        if(TextUtils.isEmpty(newnick)){
            CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            return false;
        }else if(newnick.equals(user.getMuserNick())){
            CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
            return false;
        }
        return true;
    }
    private void initDialog(){
        pd = new ProgressDialog(UpdateNickActivity.this);
        pd.setMessage(getString(R.string.update_user_nick));
        pd.show();
    }
    private void dismissDialog(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    private void updateSuccess(User retData) {
        User user1 = retData;
        CommonUtils.showLongToast(R.string.update_user_nick_success);
        UserDao dao = new UserDao(UpdateNickActivity.this);
        dao.saveUser(user1);
        FuLiCenterApplication.getInstance().setCurrentUser(user1);
        setResult(RESULT_OK);
        finish();
    }
}
