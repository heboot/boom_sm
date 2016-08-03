package com.codingfeel.sm.ui;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.interfaces.IBaseActivity;
import com.codingfeel.sm.utils.NetWorkUtils;
import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Created by Heboot on 16/7/10.
 */
public class BaseFragment extends Fragment implements IBaseActivity {

    public String TAG = this.getClass().getName();
    private SuperToast superToast;
    private LinearLayout rootLayout;
    private TextView toolBarTitle;


    protected void setToolBarTitle(String title) {
        if (toolBarTitle != null) {
            toolBarTitle.setText(title);
        }
    }


    @Override
    public Application getApplication() {
        return MyApplication.getInstance();
    }

    /**
     * 判断是否有网络连接
     */
    @Override
    public boolean validateInternet() {
        return NetWorkUtils.isConnectedByState(getApplication());
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
            superToast = new SuperToast(getActivity());
            superToast.setAnimations(SuperToast.Animations.FADE);
            superToast.setTextSize(SuperToast.TextSize.SMALL);
        }
    }


}
