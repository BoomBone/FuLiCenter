package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.Result;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.CommonUtils;
import cn.ucai.fulicenter.data.utils.MD5;
import cn.ucai.fulicenter.data.utils.ResultUtils;


public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.usernickname)
    EditText mUsernickname;
    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    String username, usernickname, password;
    IUserModel model;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.backClickArea, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                finish();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        initDialog();
        if (checkInput()) {
            model = new UserModel();
            model.register(RegisterActivity.this, username, usernickname, MD5.getMessageDigest(password), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(s!=null){
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        if(result!=null){
                            if(result.getRetCode()==I.MSG_REGISTER_USERNAME_EXISTS){
                                mUsername.requestFocus();
                                mUsername.setError(getString(R.string.register_fail_exists));
                            }
                            else if(result.getRetCode()==I.MSG_REGISTER_FAIL){
                                mUsername.requestFocus();
                                mUsername.setError(getString(R.string.register_fail));
                            }else{
                                registerSuccess();
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
        }else {
            dismissDialog();
        }
    }

    private boolean checkInput() {
        //去除空格trim
        username = mUsername.getText().toString().trim();
        usernickname = mUsernickname.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        String cpwd = mConfirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            mUsername.requestFocus();
            mUsername.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if(!username.matches("[a-zA-Z]\\w{5,15}")){
            mUsername.requestFocus();
            mUsername.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if(TextUtils.isEmpty(usernickname)){
            mUsernickname.requestFocus();
            mUsernickname.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if(TextUtils.isEmpty(password)){
            mPassword.requestFocus();
            mPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if(TextUtils.isEmpty(cpwd)){
            mConfirmPassword.requestFocus();
            mConfirmPassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }

        return true;
    }
    private void initDialog(){
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage(getString(R.string.registering));
        pd.show();
    }
    private void dismissDialog(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }
    private void registerSuccess(){
        CommonUtils.showLongToast(R.string.register_success);
        setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, username));
        finish();
    }
}
