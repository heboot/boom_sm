package com.codingfeel.sm.ui.my;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.model.UserModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.BaseFragment;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.CircleImageView;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/12.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_my_head)
    CircleImageView ivMyHead;
    @BindView(R.id.tv_my_name)
    TextView tvMyName;
    @BindView(R.id.cv_myinfo)
    CardView cvMyinfo;
    @BindView(R.id.rlyt_my_nickname)
    RelativeLayout rlytMyNickname;
    @BindView(R.id.cv_my_setting)
    CardView cvMySetting;
    @BindView(R.id.llyt_my_info)
    LinearLayout llytMyInfo;
    @BindView(R.id.llyt_my_post)
    LinearLayout llytMyPost;
    @BindView(R.id.llyt_my_fav)
    LinearLayout llytMyFav;


    private UserModel userModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);


        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initListener();
        if (UserService.getInstance().isLogin()) {
            initData(UserService.getInstance().getLoginUser());
        }
        return view;
    }


    private void initListener() {
        cvMyinfo.setOnClickListener(this);
        rlytMyNickname.setOnClickListener(this);
        cvMySetting.setOnClickListener(this);
        llytMyInfo.setOnClickListener(this);
        llytMyPost.setOnClickListener(this);
        llytMyFav.setOnClickListener(this);
    }

    private void initData(UserModel userModel) {
        tvMyName.setText(userModel.getNickName());
        ImageUtils.displayImage(getActivity(), userModel.getAvatar(), ivMyHead, R.mipmap.head_system);
    }

    @Subscribe
    public void onEvent(final UserEvent.UserLoginEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = event.getUserModel();
                if (ValidateUtil.hasError(userModel)) {
                    showToast(userModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    userModel = event.getUserModel();
                    initData(userModel);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getBasicEventEnum() == BasicEventEnum.USER_INFO) {
                    tvMyName.setText(UserService.getInstance().getLoginUser().getNickName());
                    ImageUtils.displayImage(getActivity(), UserService.getInstance().getLoginUser().getAvatar(), ivMyHead);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.cv_myinfo:
//                if (!UserService.getInstance().isLogin()) {
//                    IntentUtils.intent2LoginActivity(getActivity());
//                    return;
//                }
//                IntentUtils.intent2MyInfoActivity(getActivity());
//                break;
            case R.id.rlyt_my_nickname:
                if (!UserService.getInstance().isLogin()) {
                    IntentUtils.intent2LoginActivity(getActivity());
                    return;
                }
                IntentUtils.intent2MyInformationActivity(getActivity());
                return;
            case R.id.cv_my_setting:
                IntentUtils.intent2SettingActivity(getActivity());
                break;
            case R.id.llyt_my_info:
                if (!UserService.getInstance().isLogin()) {
                    IntentUtils.intent2LoginActivity(getActivity());
                    return;
                }
                IntentUtils.intent2MyInfoActivity(getActivity());
                break;
            case R.id.llyt_my_post:
                if (!UserService.getInstance().isLogin()) {
                    IntentUtils.intent2LoginActivity(getActivity());
                    return;
                }
                IntentUtils.intent2MyPostActivity(getActivity());
                break;
            case R.id.llyt_my_fav:
                if (!UserService.getInstance().isLogin()) {
                    IntentUtils.intent2LoginActivity(getActivity());
                    return;
                }
                IntentUtils.intent2MyFavActivity(getActivity());
                break;
        }
    }
}
