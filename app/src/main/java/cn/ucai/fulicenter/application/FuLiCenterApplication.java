package cn.ucai.fulicenter.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    String CurrentUser;

    public String getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(String currentUser) {
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
