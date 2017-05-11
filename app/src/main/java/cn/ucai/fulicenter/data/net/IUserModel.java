package cn.ucai.fulicenter.data.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.User;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface IUserModel {
    void register(Context context, String username, String usernick, String password,
                  OnCompleteListener<String> listener);

    void loginin(Context context, String username, String password, OnCompleteListener<String> listener);

    void updateNick(Context context, String username, String nickname, OnCompleteListener<String> listener);
}
