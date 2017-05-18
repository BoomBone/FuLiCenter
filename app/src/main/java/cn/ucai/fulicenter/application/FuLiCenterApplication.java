package cn.ucai.fulicenter.application;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;
import cn.ucai.fulicenter.data.bean.User;

/**
 * Created by Administrator on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    User CurrentUser;
    private boolean isLogined = false;

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public User getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
        setLogined(currentUser == null ? false : true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ShareSDK.initSDK(this);
    }
    public static FuLiCenterApplication getInstance(){
        return instance;
    }
}
