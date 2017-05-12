package cn.ucai.fulicenter.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.SettingActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    User user;
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    Unbinder unbinder;
    @BindView(R.id.tv_collect_count)
    TextView mTvCollectCount;
    int collectCount = 0;
    IUserModel model;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        model = new UserModel();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTvCollectCount.setText(String.valueOf(collectCount));
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        Log.i("main", "PersonalFragment.user:" + user);
        if (user != null) {
            mTvUserName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), mIvUserAvatar);
            initCollectCount();
        }
    }

    private void initCollectCount() {
        model.loadCollectsCount(getContext(), user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                       if(result!=null&&result.isSuccess()){
                           collectCount = Integer.parseInt(result.getMsg());
                       }else{
                           collectCount = 0;
                       }
                       mTvCollectCount.setText(String.valueOf(collectCount));
                    }

                    @Override
                    public void onError(String error) {
                        collectCount = 0;
                        mTvCollectCount.setText(String.valueOf(collectCount));
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }

    }

    @OnClick({R.id.center_top, R.id.center_user_info})
    public void onSetting(View view) {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }
}
