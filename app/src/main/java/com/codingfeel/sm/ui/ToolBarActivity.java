package com.codingfeel.sm.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.interfaces.IBaseActivity;
import com.codingfeel.sm.utils.NetWorkUtils;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Heboot on 16/6/1.
 */
public class ToolBarActivity extends AppCompatActivity implements IBaseActivity {

    public String TAG = this.getClass().getName();
    private SuperToast superToast;
    private LinearLayout rootLayout;
    private TextView toolBarTitle, toolBarSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base_toolbar);
//        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolBarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
            toolBarSend = (TextView) toolbar.findViewById(R.id.tv_toolbar_send);
        }
    }

    protected void setToolBarTitle(String title) {
        if (toolBarTitle != null) {
            toolBarTitle.setText(title);
        }
    }

    protected void showToolBarSend(View.OnClickListener listener) {
        toolBarSend.setVisibility(View.VISIBLE);
        toolBarSend.setOnClickListener(listener);
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }

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
     * 显示ToolBar
     */
    protected void showToolBar(String title, boolean displayHomeAsUpEnabled, Integer homeAsUpResId) {
        setToolBarTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (displayHomeAsUpEnabled) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (homeAsUpResId != null) {
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpResId);
            }
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
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
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
