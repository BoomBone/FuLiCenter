package cn.ucai.fulicenter.application;

import android.app.Application;

import cn.ucai.fulicenter.data.bean.User;

/**
 * Created by Administrator on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    User CurrentUser;

    public User getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static FuLiCenterApplication getInstance(){
        return instance;
    }
}
