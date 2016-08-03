package com.codingfeel.sm.ui;

import android.support.v7.app.AppCompatActivity;

import com.codingfeel.sm.interfaces.IBaseActivity;
import com.codingfeel.sm.utils.NetWorkUtils;
import com.github.johnpersano.supertoasts.SuperToast;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Heboot on 16/6/1.
 */
public class BaseActivity extends AppCompatActivity implements IBaseActivity {

    public String TAG = this.getClass().getName();
    private SuperToast superToast;


    /**
     * 判断是否有网络连接
     */
    @Override
    public boolean validateInternet() {
        return NetWorkUtils.isConnectedByState(this);
    }

    /**
     * 显示Toast
     */
    @Override
    public void showToast(String content, int duration) {
        initSuperToast();
        superToast.setDuration(duration);
        superToast.setText(content);
        superToast.show();

    }


    /**
     * 初始化SuperToast
     */
    private void initSuperToast() {
        if (superToast == null) {
            superToast = new SuperToast(this);
            superToast.setAnimations(SuperToast.Animations.FADE);
            superToast.setTextSize(SuperToast.TextSize.SMALL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
